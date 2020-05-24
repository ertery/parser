package com.vtb.parser.entity;


import javax.persistence.*;

@Entity
@Table(name = "department", schema = "parser")
public class Department {

    @Id
    @SequenceGenerator(name = "department_generator", sequenceName = "department_id_seq", schema = "parser", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_generator")
    private Integer id;

    @Embedded
    private DepartmentNaturalKey naturalKey;

    @Column(name = "description", nullable = false)
    private String description;

    public DepartmentNaturalKey getNaturalKey() {
        return naturalKey;
    }

    public void setNaturalKey(DepartmentNaturalKey naturalKey) {
        this.naturalKey = naturalKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("XMLDepartment{");
        sb.append("id=").append(id);
        sb.append(", naturalKey=").append(naturalKey);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
