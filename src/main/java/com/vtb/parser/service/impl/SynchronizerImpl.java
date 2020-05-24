package com.vtb.parser.service.impl;

import com.vtb.parser.dto.common.CommonDepartment;
import com.vtb.parser.entity.Department;
import com.vtb.parser.entity.DepartmentNaturalKey;
import com.vtb.parser.enums.FileType;
import com.vtb.parser.repository.DepartmentRepository;
import com.vtb.parser.service.Synchronizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class SynchronizerImpl implements Synchronizer {

    private static final Logger logger = LoggerFactory.getLogger(SynchronizerImpl.class);
    private static final Logger importantInfoLogger = LoggerFactory.getLogger("ImportantInfoMessages");

    private final DepartmentRepository repository;
    private final ServiceUtil serviceUtil;

    public SynchronizerImpl(DepartmentRepository repository, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.serviceUtil = serviceUtil;
    }

    @Override
    @Transactional
    public <T extends CommonDepartment> void sync(FileType fileType, List<T> departsFromFile) {

        if (CollectionUtils.isEmpty(departsFromFile)) {
            serviceUtil.sendExitCode("Empty synchronization list... exit");
        }

        Map<DepartmentNaturalKey, Department> departmentFromFileMap = departsFromFile.stream()
                .collect(Collectors.toMap(this::mapToDepartmentNaturalKey, this::mapToDepartmentEntity, (k1, k2) -> {
                    serviceUtil.sendExitCode("There are duplicated record in provided {} file with {}", fileType.name(), k1.getNaturalKey());
                    return null;
                }));


        List<Department> allDepartmentsFromDb = repository.findAll();

        List<Department> valuesForUpdate = new ArrayList<>();
        List<Department> valuesForDelete = new ArrayList<>();

        Set<DepartmentNaturalKey> naturalKeys = new HashSet<>();

        logger.info("Prepare records for update and delete...");

        allDepartmentsFromDb.forEach(dbDepartment -> {
            if (departmentFromFileMap.containsKey(dbDepartment.getNaturalKey())) {
                String description = departmentFromFileMap.get(dbDepartment.getNaturalKey()).getDescription();
                if (!dbDepartment.getDescription().equals(description)) {
                    dbDepartment.setDescription(description);
                    valuesForUpdate.add(dbDepartment);
                }
            } else {
                valuesForDelete.add(dbDepartment);
            }
            naturalKeys.add(dbDepartment.getNaturalKey());
        });

        logger.info("Records prepared. For update {}. For Delete {}", valuesForUpdate.size(), valuesForDelete.size());

        List<Department> newValuesForInsert = departmentFromFileMap.values()
                .stream()
                .filter(fileDep -> !naturalKeys.contains(fileDep.getNaturalKey()))
                .collect(Collectors.toList());

        valuesForUpdate.addAll(newValuesForInsert);

        if (!CollectionUtils.isEmpty(valuesForUpdate)) {
            repository.saveAll(valuesForUpdate);
            logger.info("{} records was updated or created in department table", valuesForUpdate.size());
        }

        if (!CollectionUtils.isEmpty(valuesForDelete)) {
            repository.deleteAll(valuesForDelete);
            logger.info("Deleted {} records from department table", valuesForDelete.size());
        }

        importantInfoLogger.info("Synchronization from file was successfully completed");
        importantInfoLogger.info("Totally created: {}", newValuesForInsert.size());
        importantInfoLogger.info("Totally updated: {}", valuesForUpdate.size() - newValuesForInsert.size());
        importantInfoLogger.info("Totally deleted: {}", valuesForDelete.size());
    }

    private DepartmentNaturalKey mapToDepartmentNaturalKey(CommonDepartment commonDepartment) {
        DepartmentNaturalKey key = new DepartmentNaturalKey();
        key.setDepartmentJob(commonDepartment.getJob());
        key.setDepartmentCode(commonDepartment.getCode());
        return key;
    }

    private Department mapToDepartmentEntity(CommonDepartment commonDepartment) {
        Department department = new Department();
        department.setDescription(commonDepartment.getDescription());

        DepartmentNaturalKey key = new DepartmentNaturalKey();
        key.setDepartmentCode(commonDepartment.getCode());
        key.setDepartmentJob(commonDepartment.getJob());

        department.setNaturalKey(key);
        return department;
    }
}
