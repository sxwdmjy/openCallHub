package com.och.ivr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.och.ivr.domain.entity.FlowNodeExecutionHistory;

/**
 * 记录每次节点执行的历史记录(FlowNodeExecutionHistory)表数据库访问层
 *
 * @author danmo
 * @since 2024-12-17 10:55:17
 */
@Repository()
@Mapper
public interface FlowNodeExecutionHistoryMapper extends BaseMapper<FlowNodeExecutionHistory> {

}

