package com.vtb.parser.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ServiceUtil {

    private static final Logger logger = LoggerFactory.getLogger("ImportantInfoMessages");

    public void sendExitCode(String errorLog, Object... args) {
        logger.info(errorLog, args);
        System.exit(1);
    }

}
