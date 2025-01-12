package com.didichuxing.datachannel.arius.admin.rest.controller.v3.white.thirdpart;

import static com.didichuxing.datachannel.arius.admin.common.constant.ApiVersion.V2_THIRD_PART;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.didichuxing.datachannel.arius.admin.biz.task.ecm.EcmTaskManager;
import com.didichuxing.datachannel.arius.admin.common.bean.common.Result;
import com.didichuxing.datachannel.arius.admin.common.bean.dto.cluster.ESZeusConfigDTO;
import com.didichuxing.datachannel.arius.admin.common.bean.vo.cluster.PluginVO;
import com.didichuxing.datachannel.arius.admin.common.util.ConvertUtil;
import com.didichuxing.datachannel.arius.admin.core.service.cluster.ecm.ESClusterConfigService;
import com.didichuxing.datachannel.arius.admin.core.service.cluster.ecm.ESPluginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * //todo ecm部署相关，0.3.1重构替换
 *
 * @author didi
 * @date 2019/3/13
 */
@RestController
@RequestMapping(V2_THIRD_PART + "/zeus")
@Api(tags = "第三方Zeus接口(REST)")
public class ThirdpartZeusController {

    @Autowired
    private ESPluginService  esPluginService;

    @Autowired
    private EcmTaskManager ecmTaskManager;

    @Autowired
    private ESClusterConfigService esClusterConfigService;

    @GetMapping("/plugins/{clusterName}")
    @ResponseBody
    @ApiOperation(value = "获取集群插件信息", notes = "")
    public List<PluginVO> getPluginsByClusterName(@PathVariable String clusterName)  {
       return ConvertUtil.list2List(esPluginService.getPluginsByClusterName(clusterName), PluginVO.class);
    }

    @GetMapping(path = "/plugin/info")
    @ResponseBody
    @ApiOperation(value = "获取宙斯ES执行脚本插件信息", notes = "")
    @ApiImplicitParam(paramType = "query", dataType = "String", name = "cluster_name", value = "集群名称", required = true)
    public String getPluginDetail(@RequestParam(value = "cluster_name") String cluster) {
        Result<String> ecmTaskOrderDetailInfo = ecmTaskManager.getEcmTaskOrderDetailInfo(cluster);
        if (ecmTaskOrderDetailInfo.failed()) {
            return "";
        }

        return ecmTaskOrderDetailInfo.getData();
    }

    @GetMapping("/cluster/config/file")
    @ResponseBody
    @ApiOperation(value = "获取宙斯ES执行脚本配置内容", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "cluster_name", value = "集群名称", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "engin_name", value = "组件名称", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "type_name", value = "配置名称", required = true)})
    public String getConfigDetail(@RequestParam(value = "cluster_name") String cluster,
                                  @RequestParam(value = "engin_name") String engin,
                                  @RequestParam(value = "type_name") String type,
                                  @RequestParam(value = "config_action")Integer configAction) {
        return esClusterConfigService.getZeusConfigContent(buildESConfigZeus(cluster, engin, type),configAction).getData();
    }

    /***********************************************private********************************************************/

    private ESZeusConfigDTO buildESConfigZeus(String cluster, String engin, String type) {
        ESZeusConfigDTO esZeusConfigDTO = new ESZeusConfigDTO();
        esZeusConfigDTO.setClusterName(cluster);
        esZeusConfigDTO.setEnginName(engin);
        esZeusConfigDTO.setTypeName(type);
        return esZeusConfigDTO;
    }
}
