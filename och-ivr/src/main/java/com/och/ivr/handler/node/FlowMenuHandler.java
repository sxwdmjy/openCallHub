package com.och.ivr.handler.node;

import com.alibaba.fastjson.JSONObject;
import com.och.common.config.redis.RedisService;
import com.och.common.constant.EslConstant;
import com.och.common.constant.FlowDataContext;
import com.och.common.exception.FlowNodeException;
import com.och.common.utils.StringUtils;
import com.och.esl.client.FsClient;
import com.och.esl.service.IFlowNoticeService;
import com.och.esl.service.IFsCallCacheService;
import com.och.ivr.domain.vo.FlowNodeVo;
import com.och.ivr.properties.FlowMenuNodeProperties;
import com.och.ivr.service.IFlowInfoService;
import com.och.ivr.service.IFlowInstancesService;
import com.och.system.domain.entity.SysFile;
import com.och.system.domain.entity.VoiceFile;
import com.och.system.service.ISysFileService;
import com.och.system.service.IVoiceFileService;
import org.springframework.statemachine.data.redis.RedisStateMachinePersister;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 菜单节点处理
 *
 * @author danmo
 * @date 2024-12-26
 */
@Component
public class FlowMenuHandler extends AbstractIFlowNodeHandler {

    private final ISysFileService fileService;

    public FlowMenuHandler(RedisStateMachinePersister<Object, Object> persister, IFsCallCacheService fsCallCacheService, IFlowNoticeService iFlowNoticeService, IFlowInfoService iFlowInfoService, IFlowInstancesService iFlowInstancesService, FsClient fsClient, RedisService redisService, ISysFileService fileService) {
        super(persister, fsCallCacheService, iFlowNoticeService, iFlowInfoService, iFlowInstancesService, fsClient, redisService);
        this.fileService = fileService;
    }

    @Override
    public void execute(FlowDataContext flowData) {
        FlowNodeVo flowNode = getFlowNode(flowData.getFlowId(), flowData.getCurrentNodeId());
        if (Objects.isNull(flowNode)) {
            throw new FlowNodeException("节点配置错误");
        }
        FlowMenuNodeProperties flowMenuNodeProperties = JSONObject.parseObject(flowNode.getProperties(), FlowMenuNodeProperties.class);
        if (Objects.isNull(flowMenuNodeProperties)) {
            throw new FlowNodeException("节点配置条件错误");
        }
        if (flowMenuNodeProperties.getInterrupt()) {
            fsClient.sendArgs(flowData.getAddress(), flowData.getUniqueId(), EslConstant.SET, EslConstant.PLAYBACK_TERMINATORS_ANY);
        }else {
            fsClient.sendArgs(flowData.getAddress(), flowData.getUniqueId(), EslConstant.SET, EslConstant.PLAYBACK_TERMINATORS);
        }
        String fileName = "";
        String errorFileName = "";
        if (flowMenuNodeProperties.getPlaybackType() == 1) {
            SysFile file = fileService.getById(flowMenuNodeProperties.getFileId());
            fileName = file.getFileName();
            SysFile errorFile = fileService.getById(flowMenuNodeProperties.getErrorFileId());
            errorFileName = errorFile.getFileName();
        } else if (flowMenuNodeProperties.getPlaybackType() == 2) {
            //tts 播放
            String ttsEngine = flowData.getTtsEngine();
            if(StringUtils.isNotBlank(ttsEngine)){
                String ttsVoice = flowData.getTtsVoice();
                fsClient.sendArgs(flowData.getAddress(), flowData.getUniqueId(), EslConstant.SET, EslConstant.TTS_ENGINE+ ttsEngine);
                fsClient.sendArgs(flowData.getAddress(), flowData.getUniqueId(), EslConstant.SET, EslConstant.TTS_VOICE+ ttsVoice);
            }
            fileName = "say:" + flowMenuNodeProperties.getContent() + "'";
            errorFileName = "say:" + flowMenuNodeProperties.getErrorContent() + "'";
        }
        if(StringUtils.isBlank(errorFileName)){
            errorFileName = "silence_stream://250";
        }
        //收号
        fsClient.playAndGetDigits(flowData.getAddress(), flowData.getUniqueId(), 1, 1, flowMenuNodeProperties.getMaxRetries(),
                flowMenuNodeProperties.getTimeout(), "#",
                fileName, errorFileName, "MENU_DTMF_RETURN",
                "\"[\\\\*0-9#]+\"", flowMenuNodeProperties.getTimeout(), null);
    }
}
