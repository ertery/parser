package com.vtb.parser.service;

import com.vtb.parser.dto.common.CommonDepartment;
import com.vtb.parser.enums.FileType;

import java.util.List;

public interface Synchronizer {

    <T extends CommonDepartment> void sync(FileType fileType, List<T> depart);
}
