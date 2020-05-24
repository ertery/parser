package com.vtb.parser.repository;

import com.vtb.parser.entity.Department;
import com.vtb.parser.entity.DepartmentNaturalKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, DepartmentNaturalKey> {
}
