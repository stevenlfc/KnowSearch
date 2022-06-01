package com.didichuxing.datachannel.arius.admin.biz.template.new_srv.precreate;

import com.didichuxing.datachannel.arius.admin.common.bean.common.Result;

/**
 * @author chengxiang
 * @date 2022/5/11
 */
public interface PreCreateManager {

    /**
     * 索引预先创建
     * @param logicTemplateId 逻辑模板id
     * @return Result 创建是否成功
     */
    Result<Void> preCreateIndex(Integer logicTemplateId);

    /**
     * 重建明天索引
      * @param logicTemplateId 逻辑模板id
     * @return Result 重建是否成功
     */
    Result<Void> reBuildTomorrowIndex(Integer logicTemplateId);

    /**
     * 异步创建今明天索引
     * @param physicalId 物理模板id
     */
    void asyncCreateTodayAndTomorrowIndexByPhysicalId(Long physicalId);
}