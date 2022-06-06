package com.didichuxing.datachannel.arius.admin.persistence.mysql.region;

import com.didichuxing.datachannel.arius.admin.common.bean.entity.region.ClusterRegion;
import com.didichuxing.datachannel.arius.admin.common.bean.po.cluster.ClusterRegionPO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusterRegionDAO {

    ClusterRegionPO getById(Long regionId);

    ClusterRegionPO getByName(String regionName);

    ClusterRegionPO getByLogicClusterId(Long logicClusterId);

    List<ClusterRegionPO> listByLogicClusterId(Long logicClusterId);

    List<ClusterRegionPO> getByPhyClusterName(String phyClusterName);

    int insert(ClusterRegionPO param);

    int update(ClusterRegionPO param);

    int delete(Long regionId);

    int deleteByClusterPhyName(String clusterPhyName);

    List<ClusterRegionPO> listAll();

    List<ClusterRegionPO> listBoundRegions();

    /**
     * 条件查询
     * @param condt 查询参数，仅仅id，logicClusterId，phyClusterName有效
     * @return
     */
    List<ClusterRegionPO> listBoundRegionsByCondition(ClusterRegionPO condt);
}
