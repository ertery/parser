package com.vtb.parser.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtb.parser.TestDataFactory;
import com.vtb.parser.dto.json.JSONDepartment;
import com.vtb.parser.dto.json.JSONDepartments;
import com.vtb.parser.enums.FileType;
import com.vtb.parser.enums.OperationType;
import com.vtb.parser.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JsonFileParserImplTest {

    private static final String PATTERN_FILE_URL = "src/test/resources/json/pattern.json";
    private static final String EMPTY_FILE_URL = "src/test/resources/json/empty.json";

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private DepartmentRepository repository;
    @Mock
    private SynchronizerImpl synchronizer;

    @InjectMocks
    private JsonFileParserImpl parserService;

    @Test
    void processLoad() throws IOException {
        File empty = new File(EMPTY_FILE_URL);
        when(repository.findAll()).thenReturn(TestDataFactory.getDataBaseDepartmentList());

        ArgumentCaptor<JSONDepartments> captor = ArgumentCaptor.forClass(JSONDepartments.class);
        ArgumentCaptor<File> captorFile = ArgumentCaptor.forClass(File.class);

        parserService.process(OperationType.LOAD, empty);

        verify(objectMapper, times(1)).writeValue(captorFile.capture(), captor.capture());

        assertEquals(4, captor.getValue().getDepartments().size());
    }

    @Test
    void processSyncOperation() throws IOException {
        File pattern = new File(PATTERN_FILE_URL);

        JSONDepartments departments = new JSONDepartments();
        departments.setDepartments(TestDataFactory.getJSONDepartmentList());
        when(objectMapper.readValue(pattern, JSONDepartments.class)).thenReturn(departments);

        doNothing().when(synchronizer).sync(FileType.JSON, departments.getDepartments());

        parserService.process(OperationType.SYNC, pattern);

        verify(synchronizer, times(1)).sync(FileType.JSON, departments.getDepartments());
    }
}
