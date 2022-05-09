package com.didichuxing.datachannel.arius.admin.biz.workorder.handler;

import static com.didichuxing.datachannel.arius.admin.common.constant.resource.ESClusterTypeEnum.ES_HOST;

import com.alibaba.fastjson.JSON;
import com.didichuxing.datachannel.arius.admin.biz.workorder.BaseWorkOrderHandler;
import com.didichuxing.datachannel.arius.admin.biz.workorder.content.ClusterOpUpdateContent;
import com.didichuxing.datachannel.arius.admin.biz.worktask.AriusOpTaskManager;
import com.didichuxing.datachannel.arius.admin.common.bean.common.Result;
import com.didichuxing.datachannel.arius.admin.common.bean.common.ecm.EcmParamBase;
import com.didichuxing.datachannel.arius.admin.common.bean.common.ecm.elasticcloud.ElasticCloudCommonActionParam;
import com.didichuxing.datachannel.arius.admin.common.bean.common.ecm.host.HostsParamBase;
import com.didichuxing.datachannel.arius.admin.common.bean.dto.task.AriusOpTaskDTO;
import com.didichuxing.datachannel.arius.admin.common.bean.dto.task.ecm.EcmTaskDTO;
import com.didichuxing.datachannel.arius.admin.common.bean.entity.arius.AriusUserInfo;
import com.didichuxing.datachannel.arius.admin.common.bean.entity.cluster.ClusterPhy;
import com.didichuxing.datachannel.arius.admin.common.bean.entity.espackage.ESPackage;
import com.didichuxing.datachannel.arius.admin.common.bean.entity.task.AriusOpTask;
import com.didichuxing.datachannel.arius.admin.common.bean.entity.workorder.WorkOrder;
import com.didichuxing.datachannel.arius.admin.common.bean.entity.workorder.detail.AbstractOrderDetail;
import com.didichuxing.datachannel.arius.admin.common.bean.entity.workorder.detail.ClusterOpUpdateOrderDetail;
import com.didichuxing.datachannel.arius.admin.common.bean.po.order.WorkOrderPO;
import com.didichuxing.datachannel.arius.admin.common.constant.ecm.EcmTaskTypeEnum;
import com.didichuxing.datachannel.arius.admin.common.constant.result.ResultType;
import com.didichuxing.datachannel.arius.admin.common.constant.task.AriusOpTaskTypeEnum;
import com.didichuxing.datachannel.arius.admin.common.constant.workorder.WorkOrderTypeEnum;
import com.didichuxing.datachannel.arius.admin.common.util.AriusObjUtils;
import com.didichuxing.datachannel.arius.admin.common.util.ConvertUtil;
import com.didichuxing.datachannel.arius.admin.core.service.cluster.ecm.ESPackageService;
import com.didichuxing.datachannel.arius.admin.core.service.cluster.ecm.EcmHandleService;
import com.didichuxing.datachannel.arius.admin.core.service.cluster.physic.ClusterPhyService;
import com.didichuxing.datachannel.arius.admin.core.service.common.AriusUserInfoService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 集群op更新处理程序
 *
 * @author
 * @date 2022/05/09
 */
@Service("clusterOpUpdateHandler")
public class ClusterOpUpdateHandler extends BaseWorkOrderHandler {
    @Autowired
    private AriusUserInfoService ariusUserInfoService;

    @Autowired
    private ClusterPhyService esClusterPhyService;

    @Autowired
    private ESPackageService     esPackageService;

    @Autowired
    private EcmHandleService ecmHandleService;

    @Autowired
    private AriusOpTaskManager ariusOpTaskManager;

    @Override
    protected Result<Void> validateConsoleParam(WorkOrder workOrder) {
        ClusterOpUpdateContent content = ConvertUtil.obj2ObjByJSON(workOrder.getContentObj(),
            ClusterOpUpdateContent.class);

        if (AriusObjUtils.isNull(content.getPhyClusterId())) {
            return Result.buildParamIllegal("物理集群id为空");
        }

        if (StringUtils.isBlank(content.getRoleOrder())) {
            return Result.buildParamIllegal("物理集群升级角色顺序为空");
        }

        ClusterPhy clusterPhy = esClusterPhyService.getClusterById(content.getPhyClusterId().intValue());
        if (AriusObjUtils.isNull(clusterPhy)) {
            return Result.buildParamIllegal("物理集群不存在");
        }

        if (ariusOpTaskManager.existUnClosedTask(content.getPhyClusterId().intValue(),
            AriusOpTaskTypeEnum.CLUSTER_UPGRADE.getType())) {
            return Result.buildParamIllegal("该集群上存在未完成的任务");
        }

        return Result.buildSucc();
    }

    @Override
    protected String getTitle(WorkOrder workOrder) {
        ClusterOpUpdateContent content = ConvertUtil.obj2ObjByJSON(workOrder.getContentObj(),
            ClusterOpUpdateContent.class);
        WorkOrderTypeEnum workOrderTypeEnum = WorkOrderTypeEnum.valueOfName(workOrder.getType());
        if (workOrderTypeEnum == null) {
            return "";
        }
        return content.getPhyClusterName() + workOrderTypeEnum.getMessage();
    }

    @Override
    protected Result<Void> validateConsoleAuth(WorkOrder workOrder) {
        if (!ariusUserInfoService.isOPByDomainAccount(workOrder.getSubmitor())) {
            return Result.buildOpForBidden("非运维人员不能操作物理集群升级！");
        }

        return Result.buildSucc();
    }

    @Override
    protected Result<Void> validateParam(WorkOrder workOrder) {
        return Result.buildSucc();
    }

    @Override
    protected Result<Void> doProcessAgree(WorkOrder workOrder, String approver) {
        ClusterOpUpdateContent content = ConvertUtil.obj2ObjByJSON(workOrder.getContentObj(),
            ClusterOpUpdateContent.class);

        EcmTaskDTO ecmTaskDTO = new EcmTaskDTO();
        ecmTaskDTO.setPhysicClusterId(content.getPhyClusterId());
        ecmTaskDTO.setWorkOrderId(workOrder.getId());
        ecmTaskDTO.setTitle(workOrder.getTitle());
        ecmTaskDTO.setOrderType(EcmTaskTypeEnum.UPGRADE.getCode());
        ecmTaskDTO.setCreator(workOrder.getSubmitor());

        Result<List<EcmParamBase>> ecmParamBaseResult = ecmHandleService.buildEcmParamBaseList(
            content.getPhyClusterId().intValue(), ConvertUtil.str2ObjArrayByJson(content.getRoleOrder(), String.class));

        if (ecmParamBaseResult.failed()) {
            return Result.buildFail(ecmParamBaseResult.getMessage());
        }

        List<EcmParamBase> ecmParamBaseList = ecmParamBaseResult.getData();
        for (EcmParamBase ecmParamBase : ecmParamBaseList) {
            // 补充version信息
            ESPackage esPackage = esPackageService.getByVersionAndType(content.getEsVersion(), ecmParamBase.getType());

            if (ecmParamBase.getType().equals(ES_HOST.getCode())) {
                ((HostsParamBase) ecmParamBase).setEsVersion(content.getEsVersion());
                if (!AriusObjUtils.isNull(esPackage) && !AriusObjUtils.isBlack(esPackage.getUrl())) {
                    ((HostsParamBase) ecmParamBase).setImageName(esPackage.getUrl());
                }
            } else {
                ((ElasticCloudCommonActionParam) ecmParamBase).setEsVersion(content.getEsVersion());
                if (!AriusObjUtils.isNull(esPackage) && !AriusObjUtils.isBlack(esPackage.getUrl())) {
                    ((ElasticCloudCommonActionParam) ecmParamBase).setImageName(esPackage.getUrl());
                }
            }
        }

        ecmTaskDTO.setType(ecmParamBaseList.get(0).getType());
        ecmTaskDTO.setEcmParamBaseList(ecmParamBaseList);

        AriusOpTaskDTO ariusOpTaskDTO = new AriusOpTaskDTO();
        ariusOpTaskDTO.setCreator(workOrder.getSubmitor());
        ariusOpTaskDTO.setTaskType(AriusOpTaskTypeEnum.CLUSTER_UPGRADE.getType());
        ariusOpTaskDTO.setExpandData(JSON.toJSONString(ecmTaskDTO));
        Result<AriusOpTask> result = ariusOpTaskManager.addTask(ariusOpTaskDTO);
        if (null == result || result.failed()) {
            return Result.buildFail("生成集群新建操作任务失败!");
        }

        return Result.buildSucc();
    }

    @Override
    public boolean canAutoReview(WorkOrder workOrder) {
        return false;
    }

    @Override
    public AbstractOrderDetail getOrderDetail(String extensions) {
        ClusterOpUpdateContent content = JSON.parseObject(extensions, ClusterOpUpdateContent.class);

        return ConvertUtil.obj2Obj(content, ClusterOpUpdateOrderDetail.class);
    }

    @Override
    public List<AriusUserInfo> getApproverList(AbstractOrderDetail detail) {
        return getOPList();
    }

    @Override
    public Result checkAuthority(WorkOrderPO orderPO, String userName) {
        if (isOP(userName)) {
            return Result.buildSucc(true);
        }
        return Result.buildFail(ResultType.OPERATE_FORBIDDEN_ERROR.getMessage());
    }
}