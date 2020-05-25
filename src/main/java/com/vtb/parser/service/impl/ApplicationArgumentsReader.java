package com.vtb.parser.service.impl;

import com.vtb.parser.enums.FileType;
import com.vtb.parser.enums.OperationType;
import com.vtb.parser.service.FileParser;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ApplicationArgumentsReader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(JsonFileParserImpl.class);

    private final Collection<FileParser> parsers;
    private final ServiceUtil serviceUtil;
    private Map<FileType, FileParser> parseFileMap;

    public ApplicationArgumentsReader(Collection<FileParser> parsers, ServiceUtil serviceUtil) {
        this.parsers = parsers;
        this.serviceUtil = serviceUtil;
    }

    @PostConstruct
    private void init() {
        parseFileMap = new HashMap<>();
        parsers.forEach(s -> parseFileMap.put(s.getParseType(), s));
    }

    @Override
    public void run(String... args) {
        if (args.length != 2) {
            serviceUtil.sendExitCode("Wrong number of arguments, have to be 2 but instead get {}", args.length);
        }

        Optional<OperationType> operationType = supportedOperation(args[0]);
        if (operationType.isEmpty()) {
            serviceUtil.sendExitCode("Wrong operation name {}", args[0]);
        }

        String filePath = args[1];
        File file = new File(filePath);

        if (!file.exists() && OperationType.SYNC.equals(operationType.get())) {
            serviceUtil.sendExitCode("File with path {} don't exists", filePath);
        } else {
            createNewFile(file);
        }

        FileType fileType = FileType.valueOf(FilenameUtils.getExtension(filePath).toUpperCase());

        try {
            parseFileMap.get(fileType).process(operationType.get(), file);
        } catch (Exception e) {
            logger.error("Unknown error during operation", e.getCause());
            serviceUtil.sendExitCode("Unknown error during operation, see application log to get more information");
        }
    }

    private void createNewFile(File file) {
        try {
            if (file.createNewFile()) {
                logger.info("File with path {} was created for load operation", file.getAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("", e);
            serviceUtil.sendExitCode("Error during file creation, see application log to get more information");
        }
    }

    private Optional<OperationType> supportedOperation(String operationType) {
        for (OperationType c : OperationType.values()) {
            if (c.name().equalsIgnoreCase(operationType)) {
                return Optional.of(c);
            }
        }

        return Optional.empty();
    }
}
