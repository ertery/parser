package com.vtb.parser.service.impl;

import com.vtb.parser.TestDataFactory;
import com.vtb.parser.dto.json.JSONDepartment;
import com.vtb.parser.entity.DepartmentNaturalKey;
import com.vtb.parser.enums.FileType;
import com.vtb.parser.exceptions.TestSystemExitException;
import com.vtb.parser.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SynchronizerImplTest {

    @Mock
    private DepartmentRepository repository;

    @Mock
    private ServiceUtil serviceUtil;

    @InjectMocks
    private SynchronizerImpl synchronizer;

    @Test
    void syncFromXMLFile() {
        when(repository.findAll()).thenReturn(TestDataFactory.getDataBaseDepartmentListForSync());
        synchronizer.sync(FileType.XML, TestDataFactory.getXMLDepartmentList());

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        verify(repository, times(0)).deleteAll(anyList());
        verify(repository, times(1)).saveAll(captor.capture());

        assertEquals(4, captor.getValue().size());
    }

    @Test
    void syncFromXMLWithDelete() {
        when(repository.findAll()).thenReturn(TestDataFactory.getDataBaseDepartmentListForSyncDelete());
        synchronizer.sync(FileType.XML, TestDataFactory.getXMLDepartmentList());

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        verify(repository, times(1)).deleteAll(captor.capture());
        verify(repository, times(1)).saveAll(anyList());

        assertEquals(2, captor.getValue().size());
    }

    @Test
    void syncFromJSONFile() {
        when(repository.findAll()).thenReturn(TestDataFactory.getDataBaseDepartmentListForSync());
        synchronizer.sync(FileType.JSON, TestDataFactory.getJSONDepartmentList());

        ArgumentCaptor<List> updateCaptor = ArgumentCaptor.forClass(List.class);

        verify(repository, times(0)).deleteAll(anyList());
        verify(repository, times(1)).saveAll(updateCaptor.capture());

        assertEquals(3, updateCaptor.getValue().size());
    }

    @Test
    void syncFromEmptyJSONFile() {
        doThrow(new TestSystemExitException()).when(serviceUtil).sendExitCode(anyString());

        assertThrows(TestSystemExitException.class, () -> synchronizer.sync(FileType.JSON, new ArrayList<>()));
        verify(serviceUtil, times(1)).sendExitCode(anyString());
        verify(repository, times(0)).findAll();
    }

    @Test
    void syncFromNullValue() {
        doThrow(new TestSystemExitException()).when(serviceUtil).sendExitCode(anyString());

        assertThrows(TestSystemExitException.class, () -> synchronizer.sync(FileType.JSON, null));
        verify(serviceUtil, times(1)).sendExitCode(anyString());
        verify(repository, times(0)).findAll();
    }

    @Test
    void syncFromFileWithDuplicatedValues() {
        List<JSONDepartment> duplicatedJSONDepartmentList = TestDataFactory.getDuplicatedJSONDepartmentList();
        JSONDepartment duplicatedValue = duplicatedJSONDepartmentList.get(1);
        DepartmentNaturalKey key = new DepartmentNaturalKey();
        key.setDepartmentCode(duplicatedValue.getCode());
        key.setDepartmentJob(duplicatedValue.getJob());

        doThrow(new TestSystemExitException()).when(serviceUtil)
                .sendExitCode(anyString(), anyString(), any(DepartmentNaturalKey.class));

        ArgumentCaptor<DepartmentNaturalKey> keyCaptor = ArgumentCaptor.forClass(DepartmentNaturalKey.class);

        assertThrows(TestSystemExitException.class,
                () -> synchronizer.sync(FileType.JSON, duplicatedJSONDepartmentList));

        verify(serviceUtil, times(1)).sendExitCode(anyString(), anyString(), keyCaptor.capture());
        verify(repository, times(0)).findAll();
        assertEquals(key, keyCaptor.getValue());
    }


}
