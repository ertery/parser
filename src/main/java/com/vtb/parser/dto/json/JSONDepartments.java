package com.vtb.parser.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONDepartments {

    private List<JSONDepartment> departments;

    public void setDepartments(List<JSONDepartment> departments) {
        this.departments = departments;
    }

    public List<JSONDepartment> getDepartments() {
        return departments;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JSONDepartments{");
        sb.append("departments=").append(departments);
        sb.append('}');
        return sb.toString();
    }
}
