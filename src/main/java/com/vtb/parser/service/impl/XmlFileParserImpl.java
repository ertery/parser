package com.vtb.parser.service.impl;

import com.vtb.parser.dto.xml.ObjectFactory;
import com.vtb.parser.dto.xml.XMLDepartment;
import com.vtb.parser.dto.xml.XMLDepartments;
import com.vtb.parser.entity.Department;
import com.vtb.parser.enums.FileType;
import com.vtb.parser.enums.OperationType;
import com.vtb.parser.repository.DepartmentRepository;
import com.vtb.parser.service.FileParser;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class XmlFileParserImpl implements FileParser {

    private static final Logger logger = LoggerFactory.getLogger(XmlFileParserImpl.class);
    private static final Logger importantInfoLogger = LoggerFactory.getLogger("ImportantInfoMessages");

    @Value("${schema.file.name}")
    private String schemaFileName;

    private final ResourceLoader resourceLoader;
    private final DepartmentRepository repository;
    private final SynchronizerImpl synchronizer;
    private final ServiceUtil serviceUtil;

    public XmlFileParserImpl(DepartmentRepository repository,
                             SynchronizerImpl synchronizer,
                             ResourceLoader resourceLoader,
                             ServiceUtil serviceUtil) {
        this.repository = repository;
        this.synchronizer = synchronizer;
        this.resourceLoader = resourceLoader;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public FileType getParseType() {
        return FileType.XML;
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
        importantInfoLogger.info("Starting sync process data from XML file {}", file.getName());
        XMLDepartments departments = unMarshalXML(file).orElseThrow();
        synchronizer.sync(this.getParseType(), departments.getDepartments());
    }

    @SneakyThrows
    private void processLoad(File file) {
        importantInfoLogger.info("Starting load data from database in XML file {}", file.getName());
        List<Department> allDepartments = repository.findAll();

        XMLDepartments departments = new XMLDepartments();
        departments.setDepartment(allDepartments.stream().map(this::mapToXMLDepartment).collect(Collectors.toList()));

        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        jaxbMarshaller.marshal(departments, file);
        importantInfoLogger.info("All data was successfully loaded to file {}", file.getPath());
    }

    private XMLDepartment mapToXMLDepartment(Department department) {
        XMLDepartment xmlDepartment = new XMLDepartment();
        xmlDepartment.setCode(department.getNaturalKey().getDepartmentCode());
        xmlDepartment.setDescription(department.getDescription());
        xmlDepartment.setJob(department.getNaturalKey().getDepartmentJob());

        return xmlDepartment;
    }

    @SneakyThrows
    private Optional<XMLDepartments> unMarshalXML(File file) {
        logger.info("Starting unmarshal xml by using scheme {}", schemaFileName);
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Resource resource = resourceLoader.getResource(schemaFileName);

        Source schemaSource = new StreamSource(resource.getInputStream());

        Schema departmentSchema = sf.newSchema(schemaSource);
        unmarshaller.setSchema(departmentSchema);

        try {
            return Optional.ofNullable((XMLDepartments) unmarshaller.unmarshal(file));
        } catch (JAXBException e) {
            logger.info("XML Parsing exception", e);
            serviceUtil.sendExitCode("XML Parsing exception, see more information in log file");
        }

        logger.warn("Smt went wrong during unmarshal xml. Return empty response");

        return Optional.empty();
    }
}
