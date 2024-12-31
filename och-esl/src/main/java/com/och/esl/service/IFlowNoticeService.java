package com.och.esl.service;

import com.och.common.constant.FlowDataContext;

public interface IFlowNoticeService {

    void notice(String address, Long callId, Long flowId);

    void notice(Integer type, String event, FlowDataContext data);
}
