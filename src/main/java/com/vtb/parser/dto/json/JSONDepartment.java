package com.vtb.parser.dto.json;

import com.vtb.parser.dto.common.CommonDepartment;

public class JSONDepartment extends CommonDepartment {

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JSONDepartment{");
        sb.append("code='").append(super.getCode()).append('\'');
        sb.append(", job='").append(super.getJob()).append('\'');
        sb.append(", description='").append(super.getDescription()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
