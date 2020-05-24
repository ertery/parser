package com.vtb.parser.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vtb.parser.dto.json.JSONDepartment;
import com.vtb.parser.dto.json.JSONDepartments;
import com.vtb.parser.entity.Department;
import com.vtb.parser.enums.FileType;
import com.vtb.parser.enums.OperationType;
import com.vtb.parser.repository.DepartmentRepository;
import com.vtb.parser.service.FileParser;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JsonFileParserImpl implements FileParser {

    private static final Logger logger = LoggerFactory.getLogger(JsonFileParserImpl.class);
    private static final Logger importantInfoLogger = LoggerFactory.getLogger("ImportantInfoMessages");

    private final DepartmentRepository repository;
    private final ObjectMapper objectMapper;
    private final SynchronizerImpl synchronizer;

    public JsonFileParserImpl(DepartmentRepository repository, ObjectMapper objectMapper, SynchronizerImpl synchronizer) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.synchronizer = synchronizer;
    }

    @Override
    public FileType getParseType() {
        return FileType.JSON;
    }

    @Override
    @Transactional
    public void process(OperationType type, File file) {
        switch (type) {
            case LOAD:
                processLoad(file);
                return;
            case SYNC:
                processSync(file);
        }
    }

    @SneakyThrows
    private void processSync(File file) {
        importantInfoLogger.info("Start synchronization procedure for file {}", file.getName());
        JSONDepartments jsonDepartments = objectMapper.readValue(file, JSONDepartments.class);
        synchronizer.sync(this.getParseType(), jsonDepartments.getDepartments());
    }

    @SneakyThrows
    private void processLoad(File file) {
        importantInfoLogger.info("Starting load data from database in JSON file {}", file.getName());
        List<Department> departmentList = repository.findAll();

        JSONDepartments departments = new JSONDepartments();
        List<JSONDepartment> jsonDepartmentList = departmentList
                .stream()
                .map(this::mapToJSONDepartment)
                .collect(Collectors.toList());

        departments.setDepartments(jsonDepartmentList);

        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(file, departments);
        importantInfoLogger.info("All data was successfully loaded to file {}", file.getPath());
    }

    private JSONDepartment mapToJSONDepartment(Department dep) {
        JSONDepartment department = new JSONDepartment();
        department.setCode(dep.getNaturalKey().getDepartmentCode());
        department.setDescription(dep.getDescription());
        department.setJob(dep.getNaturalKey().getDepartmentJob());

        return department;
    }
}
