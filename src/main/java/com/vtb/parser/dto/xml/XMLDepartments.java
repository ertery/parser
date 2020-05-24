package com.vtb.parser.dto.xml;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "department"
})
@XmlRootElement(name = "departments")
public class XMLDepartments {

    @XmlElement(required = true)
    protected List<XMLDepartment> department;

    public List<XMLDepartment> getDepartments() {
        if (department == null) {
            department = new ArrayList<>();
        }
        return this.department;
    }

    public void setDepartment(List<XMLDepartment> department) {
        this.department = department;
    }
}
