package com.didiglobal.logi.op.manager.infrastructure.common.bean;

import com.didiglobal.logi.op.manager.infrastructure.common.Result;
import com.didiglobal.logi.op.manager.infrastructure.common.ResultCode;
import com.didiglobal.logi.op.manager.interfaces.dto.GeneralGroupConfigDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author didi
 * @date 2022-07-26 3:03 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralBaseOperationComponent {
    protected Integer componentId;

    protected List<GeneralGroupConfig> groupConfigList;

    protected String associationId;

    protected String templateId;

    public Result<Void> checkParam() {
        if (null == componentId) {
            return Result.fail(ResultCode.PARAM_ERROR.getCode(), "id缺失");
        }

        if (groupConfigList.isEmpty()) {
            return Result.fail(ResultCode.PARAM_ERROR.getCode(), "配置组缺失");
        }
        return Result.success();
    }
}
