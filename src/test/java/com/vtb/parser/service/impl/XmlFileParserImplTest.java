package com.vtb.parser.service.impl;

import com.vtb.parser.TestDataFactory;
import com.vtb.parser.enums.FileType;
import com.vtb.parser.enums.OperationType;
import com.vtb.parser.exceptions.TestSystemExitException;
import com.vtb.parser.repository.DepartmentRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.util.ReflectionTestUtils;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import javax.xml.bind.JAXBException;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class XmlFileParserImplTest {

    private static final String SCHEMA_FILE_URL = "src/test/resources/xml/shema.xsd";
    private static final String PATTERN_FILE_URL = "src/test/resources/xml/pattern.xml";
    private static final String EMPTY_FILE_URL = "src/test/resources/xml/empty.xml";
    private static final String CORRUPTED_FILE_URL = "src/test/resources/xml/corrupted.xml";

    @Mock
    private DepartmentRepository repository;
    @Mock
    private SynchronizerImpl synchronizer;
    @Mock
    private ResourceLoader loader;
    @Mock
    private ServiceUtil serviceUtil;

    @InjectMocks
    private XmlFileParserImpl parserService;

    @AfterAll
    static void clear() {
        File empty = new File(EMPTY_FILE_URL);
        empty.delete();
    }

    @Test
    void processLoad() {
        when(repository.findAll()).thenReturn(TestDataFactory.getDataBaseDepartmentList());
        File empty = new File(EMPTY_FILE_URL);
        File pattern = new File(PATTERN_FILE_URL);

        parserService.process(OperationType.LOAD, empty);

        Diff diff = DiffBuilder.compare(Input.from(empty).build())
                .withTest(Input.from(pattern).build())
                .checkForIdentical()
                .ignoreWhitespace()
                .build();

        assertFalse(diff.hasDifferences());
    }

    @Test
    void processSyncOperation() {
        File pattern = new File(PATTERN_FILE_URL);
        File schema = new File(SCHEMA_FILE_URL);

        Resource fsr = new FileSystemResource(schema);

        when(loader.getResource(SCHEMA_FILE_URL)).thenReturn(fsr);
        doNothing().when(synchronizer).sync(any(), any());

        ReflectionTestUtils.setField(parserService, "schemaFileName", SCHEMA_FILE_URL);

        parserService.process(OperationType.SYNC, pattern);

        verify(synchronizer, times(1)).sync(FileType.XML, TestDataFactory.getXMLDepartmentList());
    }

    @Test
    void processSyncOperationWithCorruptedFile() {
        File pattern = new File(CORRUPTED_FILE_URL);
        File schema = new File(SCHEMA_FILE_URL);

        Resource fsr = new FileSystemResource(schema);

        when(loader.getResource(SCHEMA_FILE_URL)).thenReturn(fsr);
        doThrow(new TestSystemExitException()).when(serviceUtil).sendExitCode(anyString(), any(JAXBException.class));

        ReflectionTestUtils.setField(parserService, "schemaFileName", SCHEMA_FILE_URL);
        assertThrows(TestSystemExitException.class, () -> parserService.process(OperationType.SYNC, pattern));
    }

}
