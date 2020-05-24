package com.vtb.parser.dto.xml;

import com.vtb.parser.dto.common.CommonDepartment;

import javax.xml.bind.annotation.*;
import java.util.Objects;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "code",
        "job",
        "description"
})
@XmlRootElement(name = "department")
public class XMLDepartment extends CommonDepartment {

    @XmlElement(required = true)
    protected String code;
    @XmlElement(required = true)
    protected String job;
    @XmlElement(required = true)
    protected String description;

    public String getCode() {
        return code;
    }

    public void setCode(String value) {
        this.code = value;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String value) {
        this.job = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XMLDepartment that = (XMLDepartment) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(job, that.job) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, job, description);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("XMLDepartment{");
        sb.append("code='").append(code).append('\'');
        sb.append(", job='").append(job).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

