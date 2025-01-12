package com.didichuxing.datachannel.arius.admin.common.constant.metrics;

/**
 * @author wangpengkai
 */

public enum UserConfigTypeEnum {

                             /**
                              * code表示编号
                              * firstUserConfigType表示一级目录下指标配置信息，如集群看板和网关看板
                              * secondUserConfigType表示二级目录下指标配置信息，如集群看板下的总览指标类型
                              */
                             /**
                              * 未知的分类
                              */
                             UNKNOWN(-1, "unknown", "unknown"),
                             /**
                              * 集群看板下的总览指标
                              */
                             CLUSTER_OVERVIEW(11, MetricsConstant.CLUSTER, "overview"),
                             /**
                              * 集群看板下的节点指标
                              */
                             CLUSTER_NODE(12, MetricsConstant.CLUSTER, "node"),
                             /**
                              * 集群看板下的索引指标
                              */
                             CLUSTER_INDEX(13, MetricsConstant.CLUSTER, "index"),
                             /**
                              * 集群看板下的索引模板指标
                              */
                             CLUSTER_INDEX_TEMPLATE(14, MetricsConstant.CLUSTER, "template"),
                             /**
                              * 网关看板下的总览指标
                              */
                             GATEWAY_OVERVIEW(21, MetricsConstant.GATEWAY, "overview"),
                             /**
                              * 网关看板下的节点指标
                              */
                             GATEWAY_NODE(22, MetricsConstant.GATEWAY, "node"),
                             /**
                              * 网关看板下的索引指标
                              */
                             GATEWAY_INDEX(23, MetricsConstant.GATEWAY, "index"),
                             /**
                              * 网关看板下的项目指标
                              */
                             GATEWAY_APP(24, MetricsConstant.GATEWAY, "app"),
                             /**
                              * 网关看板下的DSL指标
                              */
                             GATEWAY_DSL(25, MetricsConstant.GATEWAY, "dsl"),
                             /**
                              * 字段页展示中的DSL模板配置
                              */
                             USER_CONFIG_SHOW_DSL_TEMPLATE(31, MetricsConstant.USER_SHOW, "dslTemplate"),
                             /**
                              * 字段页展示中的索引查询配置
                              */
                             USER_CONFIG_SHOW_INDEX_SEARCH(32, MetricsConstant.USER_SHOW, "indexSearch"),
                             /**
                              * 网关看板下的clientNode指标
                              */
                             GATEWAY_CLIENT_NODE(33, MetricsConstant.GATEWAY, "clientNode"),

                             /**
                              * dashboard 集群指标配置
                              */
                             DASHBOARD_CLUSTER(41, MetricsConstant.DASHBOARD, "cluster"),

                             /**
                              * dashboard 节点指标配置
                              */
                             DASHBOARD_NODE(42, MetricsConstant.DASHBOARD, "node"),

                             /**
                              * dashboard 索引指标配置
                              */
                             DASHBOARD_INDEX(43, MetricsConstant.DASHBOARD, "index"),

                            /**
                             * 检索查询 查询模板指标配置
                             */
                            QUERY_TEMPLATE(44, "searchQuery", "searchTemplate");

    UserConfigTypeEnum(int code, String firstUserConfigType, String secondUserConfigType) {
        this.code = code;
        this.firstUserConfigType = firstUserConfigType;
        this.secondUserConfigType = secondUserConfigType;
    }

    private int    code;
    private String firstUserConfigType;
    private String secondUserConfigType;

    public int getCode() {
        return code;
    }

    public String getFirstUserConfigType() {
        return firstUserConfigType;
    }

    public String getSecondUserConfigType() {
        return secondUserConfigType;
    }

    public static UserConfigTypeEnum valueOfCode(Integer code) {
        if (null == code) {
            return UserConfigTypeEnum.UNKNOWN;
        }
        for (UserConfigTypeEnum typeEnum : UserConfigTypeEnum.values()) {
            if (code.equals(typeEnum.getCode())) {
                return typeEnum;
            }
        }
        return UserConfigTypeEnum.UNKNOWN;
    }
}
