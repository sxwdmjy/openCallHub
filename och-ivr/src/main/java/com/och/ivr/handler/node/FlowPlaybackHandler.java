package com.och.ivr.handler.node;

import com.alibaba.fastjson.JSONObject;
import com.och.common.config.redis.RedisService;
import com.och.common.constant.CacheConstants;
import com.och.common.constant.FlowDataContext;
import com.och.common.exception.FlowNodeException;
import com.och.common.utils.StringUtils;
import com.och.esl.client.FsClient;
import com.och.esl.service.IFlowNoticeService;
import com.och.esl.service.IFsCallCacheService;
import com.och.ivr.domain.vo.FlowNodeVo;
import com.och.ivr.properties.FlowPlaybackNodeProperties;
import com.och.ivr.service.IFlowInfoService;
import com.och.ivr.service.IFlowInstancesService;
import org.springframework.statemachine.data.redis.RedisStateMachinePersister;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 放音节点处理
 *
 * @author danmo
 * @date 2024-12-26
 */
@Component
public class FlowPlaybackHandler extends AbstractIFlowNodeHandler {


    public FlowPlaybackHandler(RedisStateMachinePersister<Object, Object> persister, IFsCallCacheService fsCallCacheService, IFlowNoticeService iFlowNoticeService, IFlowInfoService iFlowInfoService, IFlowInstancesService iFlowInstancesService, FsClient fsClient, RedisService redisService) {
        super(persister, fsCallCacheService, iFlowNoticeService, iFlowInfoService, iFlowInstancesService, fsClient, redisService);
    }

    @Override
    public void execute(FlowDataContext flowData) {
        FlowNodeVo flowNode = getFlowNode(flowData.getFlowId(), flowData.getCurrentNodeId());
        if (Objects.isNull(flowNode)){
            throw new FlowNodeException("节点配置错误");
        }
        String properties = flowNode.getProperties();
        if (StringUtils.isNotBlank(properties)){
            FlowPlaybackNodeProperties playbackNodeProperties = JSONObject.parseObject(properties, FlowPlaybackNodeProperties.class);
            if (playbackNodeProperties.getInterrupt()){
               // fsClient.detectSpeechResume(flowData.getAddress(), flowData.getCallId(), flowData.getUniqueId());
            }
            if (playbackNodeProperties.getPlaybackType() == 1){
                fsClient.playFile(flowData.getAddress(), flowData.getUniqueId(), playbackNodeProperties.getFile());
            }else if (playbackNodeProperties.getPlaybackType() == 2){
                //tts 播放

            }
        }
        iFlowNoticeService.notice(2, "next", flowData);
    }
}
