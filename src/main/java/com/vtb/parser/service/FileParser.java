package com.vtb.parser.service;

import com.vtb.parser.enums.FileType;
import com.vtb.parser.enums.OperationType;

import java.io.File;

public interface FileParser {

    FileType getParseType();

    void process(OperationType type, File file);

}
