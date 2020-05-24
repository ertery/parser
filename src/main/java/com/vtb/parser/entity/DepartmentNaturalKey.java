package com.vtb.parser.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DepartmentNaturalKey implements Serializable {

    @Column(name = "dep_code")
    private String departmentCode;
    @Column(name = "dep_job")
    private String departmentJob;


    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentJob() {
        return departmentJob;
    }

    public void setDepartmentJob(String departmentJob) {
        this.departmentJob = departmentJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentNaturalKey that = (DepartmentNaturalKey) o;
        return Objects.equals(departmentCode, that.departmentCode) &&
                Objects.equals(departmentJob, that.departmentJob);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentCode, departmentJob);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("key: {");
        sb.append("code='").append(departmentCode).append('\'');
        sb.append(", job='").append(departmentJob).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
