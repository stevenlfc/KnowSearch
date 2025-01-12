package com.didichuxing.datachannel.arius.admin.common.constant.project;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作枚举
 *
 * @author d06679
 * @date 2017/7/14
 */
public enum ProjectTemplateAuthEnum {

                                     /**
                                      * Owner权限
                                      */
                                     OWN(1, "own", "管理"),

                                     /**
                                      * 读写权限
                                      */
                                     RW(2, "rw", "读写"),

                                     /**
                                      * 读权限
                                      */
                                     R(3, "r", "读"),

                                     /**
                                      * 没有权限
                                      */
                                     NO_PERMISSION(-1, "", "unknown");

    ProjectTemplateAuthEnum(int code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    private final Integer code;
    private final String  name;
    private final String  desc;

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ProjectTemplateAuthEnum valueOf(Integer code) {
        if (code == null) {
            return ProjectTemplateAuthEnum.NO_PERMISSION;
        }
        for (ProjectTemplateAuthEnum state : ProjectTemplateAuthEnum.values()) {
            if (state.getCode().equals(code)) {
                return state;
            }
        }

        return ProjectTemplateAuthEnum.NO_PERMISSION;
    }

    public static ProjectTemplateAuthEnum valueOfName(String name) {
        if (name == null) {
            return ProjectTemplateAuthEnum.NO_PERMISSION;
        }

        name = name.toLowerCase();

        if ("w".equals(name)) {
            name = RW.getName();
        }

        for (ProjectTemplateAuthEnum state : ProjectTemplateAuthEnum.values()) {
            if (state.getName().equals(name)) {
                return state;
            }
        }

        return ProjectTemplateAuthEnum.NO_PERMISSION;
    }

    public static boolean isTemplateAuthExitByCode(Integer code) {
        if (code == null) {
            return false;
        }
        for (ProjectTemplateAuthEnum state : ProjectTemplateAuthEnum.values()) {
            if (state.getCode().equals(code)) {
                return true;
            }
        }

        return false;
    }

    public static List<Integer> listAppTemplateAuthCodes() {
        return Arrays.stream(ProjectTemplateAuthEnum.values()).map(ProjectTemplateAuthEnum::getCode).distinct()
            .collect(Collectors.toList());
    }
}