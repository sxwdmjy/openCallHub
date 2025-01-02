package com.och.ivr.listener;

import com.alibaba.fastjson.JSON;
import com.och.common.config.redis.RedisService;
import com.och.common.constant.CacheConstants;
import com.och.common.constant.FlowDataContext;
import com.och.common.utils.StringUtils;
import com.och.esl.event.FlowEvent;
import com.och.ivr.domain.entity.FlowInstances;
import com.och.ivr.domain.vo.FlowEdgeVo;
import com.och.ivr.domain.vo.FlowInfoVo;
import com.och.ivr.domain.vo.FlowNodeVo;
import com.och.ivr.service.IFlowInfoService;
import com.och.ivr.service.IFlowInstancesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.redis.RedisStateMachinePersister;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class FlowEventListener implements ApplicationListener<FlowEvent> {

    private final RedisStateMachinePersister<Object, Object> persister;
    private final IFlowInfoService iFlowInfoService;
    private final IFlowInstancesService iFlowInstancesService;
    private final RedisService redisService;

    @Override
    public void onApplicationEvent(FlowEvent event) {
        Integer eventType = event.getType();
        switch (eventType) {
            case 1 -> startStateMachine(event);
            case 2 -> transferStateMachine(event);
            case 3 -> endStateMachine(event);
            default -> log.info("未知事件类型 event:{}", event);
        }
    }


    /**
     * 结束
     *
     * @param event
     */
    private void endStateMachine(FlowEvent event) {
        log.info("endStateMachine event:{}", event);
        FlowDataContext data = event.getData();
        redisService.deleteObject(StringUtils.format(CacheConstants.CALL_IVR_INSTANCES_KEY, data.getInstanceId()));
    }

    /**
     * 转发
     *
     * @param event
     */
    private void transferStateMachine(FlowEvent event) {
        log.info("transferStateMachine event:{}", event);
        FlowDataContext flowData = event.getData();
        FlowInfoVo info = iFlowInfoService.getInfo(flowData.getFlowId());
        if (Objects.isNull(info)) {
            log.info("未找到流程信息 event:{}", event);
            return;
        }
        StateMachine<Object, Object> stateMachine = buildStateMachine(info);
        if (Objects.isNull(stateMachine)) {
            log.info("创建状态机失败 event:{}", event);
            return;
        }
        try {
            StateMachine<Object, Object> restore = persister.restore(stateMachine, StringUtils.format(CacheConstants.CALL_IVR_INSTANCES_KEY, flowData.getInstanceId()));
            restore.sendEvent(event.getEvent());
        } catch (Exception e) {
            log.error("transfer 恢复状态机异常:event:{},error:{}", event, e.getMessage(), e);
        }
        try {
            persister.persist(stateMachine, StringUtils.format(CacheConstants.CALL_IVR_INSTANCES_KEY, flowData.getInstanceId()));
        } catch (Exception e) {
            log.error("transfer 持久化状态机异常:event:{},error:{}", event.getEvent(), e.getMessage(), e);
        }
    }

    /**
     * 开始
     *
     * @param event
     */
    private void startStateMachine(FlowEvent event) {
        log.info("startStateMachine event:{}", event);
        FlowDataContext flowData = event.getData();
        FlowInfoVo info = iFlowInfoService.getInfo(flowData.getFlowId());
        if (Objects.isNull(info)) {
            log.info("未找到流程信息 event:{}", event);
            return;
        }
        flowData.setFlowId(flowData.getFlowId());
        StateMachine<Object, Object> stateMachine = buildStateMachine(info);
        if (Objects.isNull(stateMachine)) {
            log.info("创建状态机失败 event:{}", event);
            return;
        }
        //创建流程实例
        FlowInstances instance = FlowInstances.builder()
                .flowId(flowData.getFlowId())
                .callId(flowData.getCallId())
                .status(1)
                .startTime(new Date())
                .build();
        iFlowInstancesService.save(instance);
        flowData.setInstanceId(instance.getId());
        //启动状态机
        stateMachine.getExtendedState().getVariables().put("flowData", event.getData());
        stateMachine.startReactively().subscribe();
        try {
            persister.persist(stateMachine, StringUtils.format(CacheConstants.CALL_IVR_INSTANCES_KEY, instance.getId()));
        } catch (Exception e) {
            log.error("持久化状态机异常:event:{},error:{}", event, e.getMessage(), e);
        }

    }


    private StateMachine<Object, Object> buildStateMachine(FlowInfoVo info) {
        log.info("创建状态机：info:{}", JSON.toJSONString(info));
        try {
            List<FlowNodeVo> nodes = info.getNodes();
            FlowNodeVo startNode = nodes.stream().filter(n -> Objects.equals("0", n.getType())).findFirst().orElseGet(null);
            if (Objects.isNull(startNode)) {
                log.info("未找到流程开始节点 id:{}", info.getId());
                return null;
            }
            FlowNodeVo endNode = nodes.stream().filter(n -> Objects.equals("1", n.getType())).findFirst().orElseGet(null);
            if (Objects.isNull(endNode)) {
                log.info("未找到流程结束节点 id:{}", info.getId());
                return null;
            }
            //配置状态机节点
            StateMachineBuilder.Builder<Object, Object> stateMachineBuilder = StateMachineBuilder.builder();
            stateMachineBuilder.configureConfiguration()
                    .withConfiguration()
                    .listener(new FlowStateMachineListener());
            stateMachineBuilder.configureStates().withStates()
                    .initial(startNode.getId()).states(nodes.stream().map(FlowNodeVo::getId).collect(Collectors.toSet()))
                    .end(endNode.getId());

            //配置状态机边
            List<FlowEdgeVo> edges = info.getEdges();
            StateMachineTransitionConfigurer<Object, Object> transitionConfigurer = stateMachineBuilder.configureTransitions();
            int i = 0;
            for (FlowEdgeVo edge : edges) {
                if (i < edges.size() - 1) {
                    transitionConfigurer.withExternal().source(edge.getSourceNodeId()).target(edge.getTargetNodeId()).event(edge.getEvent()).and();
                } else if (i == edges.size() - 1) {
                    transitionConfigurer.withExternal().source(edge.getSourceNodeId()).target(edge.getTargetNodeId()).event(edge.getEvent());
                }
                i++;
            }
            return stateMachineBuilder.build();
        } catch (Exception e) {
            log.error("创建状态机异常 id:{},error:{}", info.getId(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 是否支持异步
     *
     * @return
     */
    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
