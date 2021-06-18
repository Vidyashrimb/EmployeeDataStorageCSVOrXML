package com.employeedata.service.app.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.employeedata.service.app.constants.EmployeeDataConstants;
import com.employeedata.service.app.dtos.EmployeeDataDto;
import com.employeedata.service.app.dtos.EmployeeDataOutputDto;
import com.employeedata.service.app.dtos.EmployeeDataReturnDto;
import com.employeedata.service.app.dtos.EmployeeDataXmlDto;
import com.employeedata.service.app.dtos.ResponseDto;
import com.employeedata.service.app.protobuf.EmployeeDataProto.EmployeeDataMessage;

/**
 * @author Vidyashri
 *
 */
@Component
public class EmployeeDataStorageServiceImpl implements EmployeeDataStorageService {

  private static final Logger logger = LoggerFactory.getLogger(EmployeeDataStorageServiceImpl.class);

  private static final String CSV_FILE_FOR_EMP_DATA = "./EmployeeData.csv";

  private static final String XML_FILE_FOR_EMP_DATA = "./EmployeeData.xml";

  @Override
  public ResponseDto getEmployeeData(String empName, String fileType) {
    logger.trace("Entered inside : getEmployeeData");
    ResponseDto responseDTO = new ResponseDto();
    if (empName.isEmpty() || empName.equalsIgnoreCase("")) {
      responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
      responseDTO.setMessage("Invalid Name");
      return responseDTO;
    }
    EmployeeDataReturnDto employeeDataDto;

    if (fileType.equalsIgnoreCase(EmployeeDataConstants.FILE_TYPE_CSV))
      employeeDataDto = readCsvFileAndGetEmpData(empName);
    else
      employeeDataDto = readXmlFileAndGetEmpData(empName);

    if (employeeDataDto == null) {
      responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
      responseDTO.setMessage("Employee Data not found");
      return responseDTO;
    }

    EmployeeDataMessage employeeDataMessage = EmployeeDataMessage.newBuilder().clear()
        .setName(employeeDataDto.getName()).setDob(employeeDataDto.getDob())
        .setSalary(Double.parseDouble(employeeDataDto.getSalary())).setAge(Integer.parseInt(employeeDataDto.getAge()))
        .build();
    String payload = Base64.getEncoder().encodeToString(employeeDataMessage.toByteArray());
    responseDTO.setStatusCode(HttpStatus.OK.value());
    responseDTO.setMessage(payload);
    logger.info("Successfully fetched the employee data for : {}", empName);
    logger.trace("Entered inside readCsvAndUpdateDataUsingSuperCsv");
    return responseDTO;
  }

  /**
   * 
   * @param empName
   * @return
   */
  private EmployeeDataReturnDto readCsvFileAndGetEmpData(String empName) {
    logger.trace("Entered inside readCsvFileAndGetEmpData");
    ICsvBeanReader beanReader = null;
    EmployeeDataReturnDto employeeDataReturnDto = null;
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
      while ((employeeDataOutputDto = beanReader.read(EmployeeDataOutputDto.class, header, processors)) != null) {
        if (employeeDataOutputDto.getName().equalsIgnoreCase(empName)) {
          employeeDataReturnDto = new EmployeeDataReturnDto(employeeDataOutputDto.getName(),
              employeeDataOutputDto.getDob(), employeeDataOutputDto.getSalary(), employeeDataOutputDto.getAge());
          break;
        }
      }
    } catch (IOException e) {
      logger.error("[readCsvAndUpdateDataUsingSuperCsv] IOException : {}", e.getMessage());
    } finally {
      if (beanReader != null) {
        try {
          beanReader.close();
        } catch (IOException e) {
          logger.error("[saveEmployeeDataIntoCSVFile] IOException : {}", e.getMessage());
        }
      }
    }
    logger.trace("Exit from readCsvAndUpdateDataUsingSuperCsv");
    return employeeDataReturnDto;
  }

  /**
   * 
   * @param empName
   * @return
   */
  private EmployeeDataReturnDto readXmlFileAndGetEmpData(String empName) {
    logger.trace("Entered inside readXmlFileAndGetEmpData");
    EmployeeDataReturnDto employeeDataReturnDto = null;
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(EmployeeDataXmlDto.class);

      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

      File file = new File(XML_FILE_FOR_EMP_DATA);

      EmployeeDataXmlDto employeeDataXmlDto = (EmployeeDataXmlDto) jaxbUnmarshaller.unmarshal(file);
      List<EmployeeDataDto> employeeDataXmlDtoList = employeeDataXmlDto.getEmployeeDataDto();
      for (EmployeeDataDto employeeDataDto : employeeDataXmlDtoList) {
        if (employeeDataDto.getName().equalsIgnoreCase(empName)) {
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
          String strDate = formatter.format(employeeDataDto.getDob());
          employeeDataReturnDto = new EmployeeDataReturnDto(employeeDataDto.getName(), strDate,
              String.valueOf(employeeDataDto.getSalary()), String.valueOf(employeeDataDto.getAge()));
        }
      }
    } catch (JAXBException e) {
      logger.error("[readCsvAndUpdateDataUsingSuperCsv] JAXBException : {}", e.getMessage());
    }
    logger.trace("Exit from readXmlFileAndGetEmpData");
    return employeeDataReturnDto;
  }
}
