package com.vtb.parser;

import com.vtb.parser.dto.json.JSONDepartment;
import com.vtb.parser.dto.xml.XMLDepartment;
import com.vtb.parser.entity.Department;
import com.vtb.parser.entity.DepartmentNaturalKey;

import java.util.List;

public class TestDataFactory {


    public static List<XMLDepartment> getXMLDepartmentList() {
        XMLDepartment department1 = createXMLDepartment("Job1", "Code1", "XMLDescription1");
        XMLDepartment department2 = createXMLDepartment("Job2", "Code2", "XMLDescription2");
        XMLDepartment department3 = createXMLDepartment("Job3", "Code3", "XMLDescription3");
        XMLDepartment department4 = createXMLDepartment("Job4", "Code4", "XMLDescription4");

        return List.of(department1, department2, department3, department4);
    }

    public static List<Department> getDataBaseDepartmentList() {
        Department department1 = createDepartment("Job1", "Code1", "XMLDescription1");
        Department department2 = createDepartment("Job2", "Code2", "XMLDescription2");
        Department department3 = createDepartment("Job3", "Code3", "XMLDescription3");
        Department department4 = createDepartment("Job4", "Code4", "XMLDescription4");

        return List.of(department1, department2, department3, department4);
    }

    public static List<Department> getDataBaseDepartmentListForSync() {
        Department department1 = createDepartment("Job1", "Code1", "Description1");
        Department department2 = createDepartment("Job2", "Code2", "Description2");

        return List.of(department1, department2);
    }

    public static List<Department> getDataBaseDepartmentListForSyncDelete() {
        Department department1 = createDepartment("JobDel1", "Code1", "Description1");
        Department department2 = createDepartment("JobDel2", "Code2", "Description2");

        return List.of(department1, department2);
    }


    private static Department createDepartment(String job, String code, String description) {
        Department department = new Department();

        DepartmentNaturalKey dnk = new DepartmentNaturalKey();
        dnk.setDepartmentCode(code);
        dnk.setDepartmentJob(job);
        department.setNaturalKey(dnk);

        department.setDescription(description);
        return department;
    }

    private static XMLDepartment createXMLDepartment(String job, String code, String description) {
        XMLDepartment department = new XMLDepartment();
        department.setDescription(description);
        department.setCode(code);
        department.setJob(job);

        return department;
    }

    private static JSONDepartment createJSONDepartment(String job, String code, String description) {
        JSONDepartment department = new JSONDepartment();
        department.setDescription(description);
        department.setCode(code);
        department.setJob(job);

        return department;
    }

    public static List<JSONDepartment> getJSONDepartmentList() {
        JSONDepartment department1 = createJSONDepartment("Job1", "Code1", "JSONDescription1");
        JSONDepartment department2 = createJSONDepartment("Job2", "Code2", "JSONDescription2");
        JSONDepartment department3 = createJSONDepartment("Job3", "Code3", "JSONDescription3");

        return List.of(department1, department2, department3);
    }

    public static List<JSONDepartment> getDuplicatedJSONDepartmentList() {
        JSONDepartment department1 = createJSONDepartment("Job1", "Code1", "JSONDescription1");
        JSONDepartment department2 = createJSONDepartment("Job1", "Code1", "JSONDescription2");
        JSONDepartment department3 = createJSONDepartment("Job3", "Code3", "JSONDescription3");

        return List.of(department1, department2, department3);
    }
}
