package com.employeedata.service.app.consumer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.employeedata.service.app.constants.EmployeeDataConstants;
import com.employeedata.service.app.dtos.EmployeeDataDto;
import com.employeedata.service.app.dtos.EmployeeDataOutputDto;
import com.employeedata.service.app.dtos.EmployeeDataXmlDto;
import com.employeedata.service.app.protobuf.EmployeeDataProto.EmployeeDataMessage;
import com.employeedata.service.app.protobuf.EmployeeDataProto.UpdatedEmployeeDataMessage;
import com.google.protobuf.InvalidProtocolBufferException;

@Component
public class EmployeeDataConsumer {

  private static final Logger logger = LoggerFactory.getLogger(EmployeeDataConsumer.class);

  private static final String CSV_FILE_FOR_EMP_DATA = "./EmployeeData.csv";

  private static final String XML_FILE_FOR_EMP_DATA = "./EmployeeData.xml";

  private static final String DATE_FORMAT = "yyyy-MM-dd";

  /**
   * 
   * @param message
   */
  @KafkaListener(topics = EmployeeDataConstants.KAFKA_SAVE_DATA_CONSUMER_TOPIC, groupId = EmployeeDataConstants.KAFKA_SAVE_DATA_CONSUMER_GROUP, containerFactory = "kafkaListenerContainerFactory")
  public void receiveSaveEmployeeData(String message) {
    logger.trace("Entered inside receiveSaveEmployeeData");
    try {
      if (message == null) {
        logger.error("Message is null");
        return;
      }
      EmployeeDataMessage employeeDataMessage = processEmployeeMessageData(message);
      if (employeeDataMessage == null) {
        return;
      }
      Date date = new SimpleDateFormat(DATE_FORMAT).parse(employeeDataMessage.getDob());

      EmployeeDataDto employeeDataDto = new EmployeeDataDto(employeeDataMessage.getName(), date,
          employeeDataMessage.getSalary(), employeeDataMessage.getAge(), employeeDataMessage.getFileType().name());
      List<EmployeeDataDto> employeeDataDtoList = new ArrayList<>();
      employeeDataDtoList.add(employeeDataDto);
      if (employeeDataDto.getFileType().equalsIgnoreCase(EmployeeDataConstants.FILE_TYPE_CSV))
        saveEmployeeDataIntoCSVFile(employeeDataDtoList, true);
      else
        saveEmployeeDataIntoXMLFile(employeeDataDtoList, true);
    } catch (ParseException e) {
      logger.error("[receiveSaveEmployeeData] ParseException : {}", e.getMessage());
    }
    logger.trace("Exit from receiveSaveEmployeeData");
  }

  /**
   * 
   * @param message
   */
  @KafkaListener(topics = EmployeeDataConstants.KAFKA_UPDATE_DATA_CONSUMER_TOPIC, groupId = EmployeeDataConstants.KAFKA_UPDATE_DATA_CONSUMER_GROUP, containerFactory = "kafkaListenerContainerFactory")
  public void receiveUpdateEmployeeData(String message) {
    logger.trace("Entered inside receiveUpdateEmployeeData");
    if (message == null) {
      logger.error("Message is null");
      return;
    }

    UpdatedEmployeeDataMessage updatedEmployeeDataMessage = processUpdateEmployeeMessageData(message);
    if (updatedEmployeeDataMessage == null) {
      return;
    }
    if (updatedEmployeeDataMessage.getFileType().name().equalsIgnoreCase(EmployeeDataConstants.FILE_TYPE_CSV))
      readCsvFileAndUpdateEmployeeData(updatedEmployeeDataMessage);
    else
      readXmlFileAndUpdateEmployeeData(updatedEmployeeDataMessage);
    logger.trace("Exit from receiveUpdateEmployeeData");
  }

  /**
   * 
   * @param message
   * @return
   */
  private EmployeeDataMessage processEmployeeMessageData(String message) {
    logger.trace("Entered inside processEmployeeMessageData");
    EmployeeDataMessage employeeDataMessage = null;
    try {
      byte[] payload = Base64.getDecoder().decode(message);

      employeeDataMessage = EmployeeDataMessage.newBuilder().mergeFrom(payload).build();
    } catch (InvalidProtocolBufferException e) {
      logger.error("InvalidProtocolBufferException : {}".concat(e.getMessage()));
    }
    logger.trace("Exit from processEmployeeMessageData");
    return employeeDataMessage;
  }

  /**
   * 
   * @param message
   * @return
   */
  private UpdatedEmployeeDataMessage processUpdateEmployeeMessageData(String message) {
    logger.trace("Entered inside processEmployeeMessageData");
    UpdatedEmployeeDataMessage updatedEmployeeDataMessage = null;
    try {
      byte[] payload = Base64.getDecoder().decode(message);

      updatedEmployeeDataMessage = UpdatedEmployeeDataMessage.newBuilder().mergeFrom(payload).build();
    } catch (InvalidProtocolBufferException e) {
      logger.error("InvalidProtocolBufferException : {}".concat(e.getMessage()));
    }
    logger.trace("Exit from processEmployeeMessageData");
    return updatedEmployeeDataMessage;
  }

  /**
   * 
   * @param employeeDataDtoList
   * @param isRetain
   */
  private void saveEmployeeDataIntoCSVFile(List<EmployeeDataDto> employeeDataDtoList, boolean isRetain) {
    logger.trace("Entered inside saveEmployeeDataIntoCSVFile");
    ICsvBeanWriter beanWriter = null;
    CellProcessor[] processors = new CellProcessor[] { new NotNull(), // name
        new FmtDate(DATE_FORMAT), // dob
        new ParseDouble(), // salary
        new NotNull(), // age
        new NotNull() // fileType
    };

    try {
      File file = new File(CSV_FILE_FOR_EMP_DATA);
      String[] header = { "name", "dob", "salary", "age", "fileType" };

      if (!file.exists() || !isRetain) {
        beanWriter = new CsvBeanWriter(new FileWriter(CSV_FILE_FOR_EMP_DATA), CsvPreference.STANDARD_PREFERENCE);
        beanWriter.writeHeader(header);
      } else {
        FileWriter fileWritter = new FileWriter(file.getName(), true);
        beanWriter = new CsvBeanWriter(fileWritter, CsvPreference.STANDARD_PREFERENCE);
      }

      for (EmployeeDataDto employeeDataDto : employeeDataDtoList) {
        beanWriter.write(employeeDataDto, header, processors);
      }
      logger.info("[saveEmployeeDataIntoCSVFile] Successfully saved the employee data into csv file");

    } catch (IOException e) {
      logger.error("[saveEmployeeDataIntoCSVFile] CsvRequiredFieldEmptyException : {}", e.getMessage());
    } finally {
      if (beanWriter != null) {
        try {
          beanWriter.close();
        } catch (IOException e) {
          logger.error("[saveEmployeeDataIntoCSVFile] IOException : {}", e.getMessage());
        }
      }
    }
    logger.trace("Exit from persistTheDataIntoCSVFileUsingSuperCsv");
  }

  /**
   * 
   * @param updatedEmployeeDataMessage
   */
  private void readCsvFileAndUpdateEmployeeData(UpdatedEmployeeDataMessage updatedEmployeeDataMessage) {
    logger.trace("Entered inside readCsvFileAndUpdateEmployeeData");
    ICsvBeanReader beanReader = null;
    try {
      CellProcessor[] processors = new CellProcessor[] { new NotNull(), // name
          new NotNull(), // dob
          new NotNull(), // salary
          new NotNull(), // age
          new NotNull() // fileType
      };
      beanReader = new CsvBeanReader(new FileReader(CSV_FILE_FOR_EMP_DATA), CsvPreference.STANDARD_PREFERENCE);
      final String[] header = beanReader.getHeader(true);
      EmployeeDataOutputDto employeeDataOutputDto;
      EmployeeDataDto employeeDataDto = null;
      List<EmployeeDataDto> employeeDataDtoList = new ArrayList<>();
      while ((employeeDataOutputDto = beanReader.read(EmployeeDataOutputDto.class, header, processors)) != null) {
        if (employeeDataOutputDto.getName().equalsIgnoreCase(updatedEmployeeDataMessage.getUpdateName())) {
          Date date = new SimpleDateFormat(DATE_FORMAT).parse(updatedEmployeeDataMessage.getDob());
          employeeDataDto = new EmployeeDataDto(updatedEmployeeDataMessage.getName(), date,
              updatedEmployeeDataMessage.getSalary(), updatedEmployeeDataMessage.getAge(),
              updatedEmployeeDataMessage.getFileType().name());
        } else {
          Date date = new SimpleDateFormat(DATE_FORMAT).parse(employeeDataOutputDto.getDob());
          employeeDataDto = new EmployeeDataDto(employeeDataOutputDto.getName(), date,
              Double.parseDouble(employeeDataOutputDto.getSalary()), Integer.parseInt(employeeDataOutputDto.getAge()),
              employeeDataOutputDto.getFileType());
        }
        employeeDataDtoList.add(employeeDataDto);
      }
      saveEmployeeDataIntoCSVFile(employeeDataDtoList, false);
      logger.info("Successfully updated the data for employee : {}", updatedEmployeeDataMessage.getName());
    } catch (IOException e) {
      logger.error("[readCsvFileAndUpdateEmployeeData] IOException : {}", e.getMessage());
    } catch (ParseException e) {
      logger.error("[readCsvFileAndUpdateEmployeeData] ParseException : {}", e.getMessage());
    } finally {
      if (beanReader != null) {
        try {
          beanReader.close();
        } catch (IOException e) {
          logger.error("[saveEmployeeDataIntoCSVFile] IOException : {}", e.getMessage());
        }
      }
    }
    logger.trace("Exit from readCsvFileAndUpdateEmployeeData");
  }

  /**
   * 
   * @param employeeDataDtoList
   * @param isRetain
   */
  private void saveEmployeeDataIntoXMLFile(List<EmployeeDataDto> employeeDataDtoList, boolean isRetain) {
    logger.trace("Entered inside saveEmployeeDataIntoXMLFile");
    try {

      JAXBContext jaxbContext = JAXBContext.newInstance(EmployeeDataXmlDto.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      List<EmployeeDataDto> employeeDataXmlDtoList = new ArrayList<>();
      EmployeeDataXmlDto employeeDataXmlDto = new EmployeeDataXmlDto();

      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      File file = new File(XML_FILE_FOR_EMP_DATA);

      if (file.exists() && isRetain) {
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        employeeDataXmlDto = (EmployeeDataXmlDto) jaxbUnmarshaller.unmarshal(file);
        employeeDataXmlDtoList = employeeDataXmlDto.getEmployeeDataDto();
      }

      for (EmployeeDataDto employeeDataDto : employeeDataDtoList) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        String strDate = formatter.format(employeeDataDto.getDob());
        Date date = new SimpleDateFormat(DATE_FORMAT).parse(strDate);
        employeeDataDto.setDob(date);
        employeeDataXmlDtoList.add(employeeDataDto);
      }
      employeeDataXmlDto.setEmployeeDataDto(employeeDataXmlDtoList);
      jaxbMarshaller.marshal(employeeDataXmlDto, file);
      logger.info("[saveEmployeeDataIntoXMLFile] Successfully saved the employee data into xml file");
    } catch (JAXBException e) {
      logger.error("[saveEmployeeDataIntoXMLFile] JAXBException : {}", e.getMessage());
    } catch (ParseException e) {
      logger.error("[saveEmployeeDataIntoXMLFile] ParseException : {}", e.getMessage());
    }
    logger.trace("Exit from saveEmployeeDataIntoXMLFile");
  }

  /**
   * 
   * @param updatedEmployeeDataMessage
   */
  private void readXmlFileAndUpdateEmployeeData(UpdatedEmployeeDataMessage updatedEmployeeDataMessage) {
    logger.trace("Entered inside readXmlFileAndUpdateEmployeeData");
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(EmployeeDataXmlDto.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

      File file = new File(XML_FILE_FOR_EMP_DATA);

      EmployeeDataXmlDto employeeDataXmlDto = (EmployeeDataXmlDto) jaxbUnmarshaller.unmarshal(file);
      List<EmployeeDataDto> employeeDataXmlDtoList = employeeDataXmlDto.getEmployeeDataDto();
      List<EmployeeDataDto> employeeDataXmlDtoListNew = new ArrayList<>();
      for (EmployeeDataDto employeeDataDto : employeeDataXmlDtoList) {
        if (employeeDataDto.getName().equalsIgnoreCase(updatedEmployeeDataMessage.getName())) {
          Date date = new SimpleDateFormat(DATE_FORMAT).parse(updatedEmployeeDataMessage.getDob());
          employeeDataDto.setDob(date);
          employeeDataDto.setSalary(updatedEmployeeDataMessage.getSalary());
          employeeDataDto.setAge(updatedEmployeeDataMessage.getAge());
          employeeDataDto.setFileType(updatedEmployeeDataMessage.getFileType().name());
        }
        employeeDataXmlDtoListNew.add(employeeDataDto);
      }
      saveEmployeeDataIntoXMLFile(employeeDataXmlDtoListNew, false);
      logger.info("[saveEmployeeDataIntoXMLFile] Successfully updated the employee data for : {}",
          updatedEmployeeDataMessage.getName());
    } catch (JAXBException e) {
      logger.error("[saveEmployeeDataIntoXMLFile] JAXBException : {}", e.getMessage());
    } catch (ParseException e) {
      logger.error("[saveEmployeeDataIntoXMLFile] ParseException : {}", e.getMessage());
    }
    logger.trace("Exit from readXmlFileAndUpdateEmployeeData");
  }
}
