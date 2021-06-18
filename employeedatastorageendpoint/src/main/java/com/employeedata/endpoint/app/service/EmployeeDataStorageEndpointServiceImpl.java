package com.employeedata.endpoint.app.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.employeedata.endpoint.app.constants.EmployeeDataConstants;
import com.employeedata.endpoint.app.dtos.EmployeeDataInputDto;
import com.employeedata.endpoint.app.dtos.EmployeeDataOutputDto;
import com.employeedata.endpoint.app.dtos.ResponseDto;
import com.employeedata.endpoint.app.kafkautil.KafkaPublisherTemplate;
import com.employeedata.endpoint.app.protobuf.EmployeeDataProto.EmployeeDataMessage;
import com.employeedata.endpoint.app.protobuf.EmployeeDataProto.UpdatedEmployeeDataMessage;
import com.employeedata.endpoint.app.protobuf.EmployeeDataProto.eFileType;

/**
 * @author Vidyashri
 *
 */
@Component
public class EmployeeDataStorageEndpointServiceImpl implements EmployeeDataStorageEndpointService {

  private static final Logger logger = LoggerFactory.getLogger(EmployeeDataStorageEndpointServiceImpl.class);

  private String employeeDataStorageServiceIp = System.getenv().get("EMPLOYEE_DATA_STORAGE_SERVICE_IP");
  private String employeeDataStorageServicePort = System.getenv().get("EMPLOYEE_DATA_STORAGE_SERVICE_PORT");
  private String employeeDataStorageServiceApi = "/employeservice/getemployeedata/";

  private Map<String, String> employeeMap = new HashMap<>();

  @Autowired
  KafkaPublisherTemplate kafkaPublisherTemplate;

  @Override
  public ResponseDto saveNewEmployeeData(EmployeeDataInputDto employeeDataInputDto) {
    logger.trace("Entered inside : saveNewEmployeeData");
    ResponseDto responseDTO;
    responseDTO = validateDataInputParameters(employeeDataInputDto);
    if (responseDTO.getStatusCode() != HttpStatus.ACCEPTED.value()) {
      return responseDTO;
    }
    if (employeeMap.containsKey(employeeDataInputDto.getName())) {
      responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
      responseDTO.setMessage("Employee data already exists for " + employeeDataInputDto.getName()
          + ". Use update method to update the employee data");
      return responseDTO;
    }

    employeeMap.put(employeeDataInputDto.getName(), employeeDataInputDto.getFileType());
    eFileType fileType = eFileType.CSV;
    if (employeeDataInputDto.getFileType().equalsIgnoreCase(EmployeeDataConstants.FILE_TYPE_XML)) {
      fileType = eFileType.XML;
    }

    EmployeeDataMessage employeeDataMessage = EmployeeDataMessage.newBuilder().clear()
        .setName(employeeDataInputDto.getName()).setDob(employeeDataInputDto.getDob())
        .setSalary(employeeDataInputDto.getSalary()).setAge(employeeDataInputDto.getAge()).setFileType(fileType)
        .build();

    String message = Base64.getEncoder().encodeToString(employeeDataMessage.toByteArray());
    kafkaPublisherTemplate.sendMessage(message, EmployeeDataConstants.KAFKA_SAVE_DATA_CONSUMER_TOPIC);
    responseDTO.setStatusCode(HttpStatus.CREATED.value());
    responseDTO.setMessage("Successfully saved the employee data for user : " + employeeDataInputDto.getName());
    logger.trace("Exit inside : saveNewEmployeeData");
    return responseDTO;
  }

  @Override
  public ResponseDto updateEmployeeData(EmployeeDataInputDto employeeDataInputDto) {
    logger.trace("Entered inside : updateEmployeeData");
    ResponseDto responseDTO;
    responseDTO = validateDataInputParameters(employeeDataInputDto);
    if (responseDTO.getStatusCode() != HttpStatus.ACCEPTED.value()) {
      return responseDTO;
    }
    if (!employeeMap.containsKey(employeeDataInputDto.getName())) {
      responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
      responseDTO.setMessage("Employee data doesn't exists for " + employeeDataInputDto.getName()
          + ". Use create method to update the employee data");
      return responseDTO;
    }
    if (!employeeMap.get(employeeDataInputDto.getName()).equalsIgnoreCase(employeeDataInputDto.getFileType())) {
      responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
      responseDTO.setMessage("Cannot update the file type of employee. Please use "
          + employeeMap.get(employeeDataInputDto.getName()) + " filetype");
      return responseDTO;
    }
    eFileType fileType = eFileType.CSV;
    if (employeeDataInputDto.getFileType().equalsIgnoreCase(EmployeeDataConstants.FILE_TYPE_XML)) {
      fileType = eFileType.XML;
    }

    UpdatedEmployeeDataMessage updatedEmployeeDataMessage = UpdatedEmployeeDataMessage.newBuilder().clear()
        .setUpdateName(employeeDataInputDto.getName()).setName(employeeDataInputDto.getName())
        .setDob(employeeDataInputDto.getDob()).setSalary(employeeDataInputDto.getSalary())
        .setAge(employeeDataInputDto.getAge()).setFileType(fileType).build();

    String message = Base64.getEncoder().encodeToString(updatedEmployeeDataMessage.toByteArray());
    kafkaPublisherTemplate.sendMessage(message, EmployeeDataConstants.KAFKA_UPDATE_DATA_CONSUMER_TOPIC);
    responseDTO.setStatusCode(HttpStatus.CREATED.value());
    responseDTO.setMessage("Successfully updated the employee data for user : " + employeeDataInputDto.getName());
    logger.trace("Exit from : updateEmployeeData");
    return responseDTO;
  }

  @Override
  public ResponseEntity<Object> getEmployeeData(String empName) {
    logger.trace("Entered inside : getEmployeeData");
    ResponseDto responseDTO = new ResponseDto();
    ResponseEntity responseEntity = null;
    try {
      if (empName.isEmpty() || empName.equalsIgnoreCase("")) {
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseDTO.setMessage("Invalid Name : " + empName);
        responseEntity = new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.BAD_REQUEST);
        return responseEntity;
      }

      String url = "http://" + employeeDataStorageServiceIp + ":" + employeeDataStorageServicePort
          + employeeDataStorageServiceApi + empName + "/" + employeeMap.get(empName);

      HttpClient client = HttpClientBuilder.create().build();
      HttpGet get = new HttpGet(url);

      get.setHeader(EmployeeDataConstants.CONTENT_TYPE, EmployeeDataConstants.HTTP_HEADER_CONTENT);

      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
        responseDTO.setStatusCode(response.getStatusLine().getStatusCode());
        responseDTO.setMessage("Failed to get the Employee Data for " + empName);
        responseEntity = new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.BAD_REQUEST);
        return responseEntity;
      }
      HttpEntity httpEntity = response.getEntity();
      String content = EntityUtils.toString(httpEntity);
      JSONObject contentJson = new JSONObject(content);
      int statusCode = contentJson.has("statusCode") ? contentJson.getInt("statusCode")
          : HttpStatus.BAD_REQUEST.value();
      if (statusCode != HttpStatus.OK.value()) {
        responseDTO.setStatusCode(response.getStatusLine().getStatusCode());
        responseDTO.setMessage("Employee Data not available for " + empName);
        responseEntity = new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.BAD_REQUEST);
        return responseEntity;
      }
      String contentPayload = contentJson.has("message") ? contentJson.getString("message") : null;
      if (contentPayload == null) {
        responseDTO.setStatusCode(response.getStatusLine().getStatusCode());
        responseDTO.setMessage("Employee Data not available for " + empName);
        responseEntity = new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.BAD_REQUEST);
        return responseEntity;
      }

      byte[] payload = Base64.getDecoder().decode(contentPayload);
      EmployeeDataMessage employeeDataMessage = EmployeeDataMessage.newBuilder().mergeFrom(payload).build();
      if (employeeDataMessage == null) {
        responseDTO.setStatusCode(response.getStatusLine().getStatusCode());
        responseDTO.setMessage("Employee Data not available for " + empName);
        responseEntity = new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.BAD_REQUEST);
        return responseEntity;
      }
      EmployeeDataOutputDto employeeDataOutputDto = new EmployeeDataOutputDto(employeeDataMessage.getName(),
          employeeDataMessage.getDob(), employeeDataMessage.getSalary(), employeeDataMessage.getAge());

      responseEntity = new ResponseEntity<EmployeeDataOutputDto>(employeeDataOutputDto, HttpStatus.OK);
    } catch (IOException e) {
      logger.error("Failed to get the Employee Data to exception : {}", e.getMessage());
      responseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      responseDTO.setMessage("Failed to get the Employee Data to exception : " + e.getMessage());
    }
    logger.trace("Exit from : getEmployeeData");
    return responseEntity;
  }

  /**
   * 
   * @param employeeDataInputDto
   * @return
   */
  private ResponseDto validateDataInputParameters(EmployeeDataInputDto employeeDataInputDto) {
    logger.trace("Entered inside : validateDataInputParameters");
    ResponseDto responseDTO = new ResponseDto();
    try {
      if (employeeDataInputDto.getName() == null || employeeDataInputDto.getName().isEmpty()
          || employeeDataInputDto.getName().equalsIgnoreCase("")) {
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseDTO.setMessage("Invalid name : name sould not be empty");
        return responseDTO;
      } else if (employeeDataInputDto.getDob() == null || employeeDataInputDto.getDob().isEmpty()) {
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseDTO.setMessage("Invalid dob : dob should not be empty");
        return responseDTO;
      } else if (employeeDataInputDto.getDob() != null) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.parse(employeeDataInputDto.getDob());
      } else if (employeeDataInputDto.getSalary() == 0) {
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseDTO.setMessage("Invalid Salary : Salary should not be 0");
        return responseDTO;
      } else if (employeeDataInputDto.getAge() == 0) {
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseDTO.setMessage("Invalid Age : Age should not be 0");
        return responseDTO;
      } else if (employeeDataInputDto.getFileType() == null
          || !(employeeDataInputDto.getFileType().equalsIgnoreCase(EmployeeDataConstants.FILE_TYPE_CSV)
              || (employeeDataInputDto.getFileType().equalsIgnoreCase(EmployeeDataConstants.FILE_TYPE_XML)))) {
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseDTO.setMessage("Invalid fileType : file type cannot be empty, Use either CSV or XML");
        return responseDTO;
      }
    } catch (ParseException e) {
      logger.error("[validateDataInputParameters] ParseException : {}", e.getMessage());
      responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
      responseDTO.setMessage("Invalid Date : Date should be in yyyy-MM-dd format");
      return responseDTO;
    }
    logger.trace("Entered inside : validateDataInputParameters");
    return responseDTO;
  }
}
