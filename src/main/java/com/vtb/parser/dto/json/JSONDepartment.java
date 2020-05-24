package com.vtb.parser.dto.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vtb.parser.dto.common.CommonDepartment;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONDepartment extends CommonDepartment {


    private String code;
    private String job;
    private String description;

    public JSONDepartment() {
    }

    @JsonCreator
    public JSONDepartment(@JsonProperty(value = "code", required = true) String code,
                          @JsonProperty(value = "job", required = true) String job,
                          @JsonProperty(value = "description", required = true) String description) {
        this.code = code;
        this.job = job;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
