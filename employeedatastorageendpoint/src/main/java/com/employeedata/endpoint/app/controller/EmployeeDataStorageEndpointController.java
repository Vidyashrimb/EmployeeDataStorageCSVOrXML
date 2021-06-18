package com.employeedata.endpoint.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.employeedata.endpoint.app.constants.EmployeeDataConstants;
import com.employeedata.endpoint.app.dtos.EmployeeDataInputDto;
import com.employeedata.endpoint.app.dtos.ResponseDto;
import com.employeedata.endpoint.app.service.EmployeeDataStorageEndpointService;

/**
 * @author Vidyashri
 *
 */
@RestController
public class EmployeeDataStorageEndpointController {

  private static final Logger logger = LoggerFactory.getLogger(EmployeeDataStorageEndpointController.class);

  // Instance of the FilesStorageEndpointService
  @Autowired
  private EmployeeDataStorageEndpointService employeeDataStorageEndpointService;

  // Instance of the ResponseDTO
  private ResponseDto responseDTO = new ResponseDto();

  /**
   * Method to save the new Employee Data
   * 
   * @param employeeDataInputDto
   * @return
   */
  @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> saveNewEmployeeData(@RequestBody EmployeeDataInputDto employeeDataInputDto) {
    logger.trace("Enter inside :saveNewEmployeeData");
    ResponseEntity<String> responseEntity = null;
    responseDTO = employeeDataStorageEndpointService.saveNewEmployeeData(employeeDataInputDto);
    if (responseDTO.getStatusCode() == HttpStatus.OK.value()) {
      responseEntity = ResponseEntity.status(responseDTO.getStatusCode()).body(responseDTO.getMessage());
    } else {
      responseEntity = ResponseEntity.status(responseDTO.getStatusCode()).body(responseDTO.toString());
    }
    logger.info("{} Successfully Saved the employee data for : {} ",
        EmployeeDataConstants.LOGGER_PREFIX_API_INVOCATION, employeeDataInputDto.getName());
    logger.trace("Exit from :saveNewEmployeeData");
    return responseEntity;
  }

  /**
   * Method to update the Employee Data
   * 
   * @param employeeDataInputDto
   * @return
   */
  @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> updateEmployeeData(@RequestBody EmployeeDataInputDto employeeDataInputDto) {
    logger.trace("Enter inside :updateEmployeeData");
    ResponseEntity<String> responseEntity = null;
    responseDTO = employeeDataStorageEndpointService.updateEmployeeData(employeeDataInputDto);
    if (responseDTO.getStatusCode() == HttpStatus.OK.value()) {
      responseEntity = ResponseEntity.status(responseDTO.getStatusCode()).body(responseDTO.getMessage());
    } else {
      responseEntity = ResponseEntity.status(responseDTO.getStatusCode()).body(responseDTO.toString());
    }
    logger.info("{} Successfully updated the employee data for : {} ",
        EmployeeDataConstants.LOGGER_PREFIX_API_INVOCATION, employeeDataInputDto.getName());
    logger.trace("Exit from :updateEmployeeData");
    return responseEntity;
  }

  /**
   * Method to update the Employee Data
   * 
   * @param dataInputDto
   * @return
   */
  @GetMapping(value = "/{empName}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getEmployeeData(@PathVariable String empName) {
    logger.trace("Enter inside :updateEmployeeData");
    ResponseEntity<String> responseEntity = null;
    responseDTO = employeeDataStorageEndpointService.getEmployeeData(empName);
    if (responseDTO.getStatusCode() == HttpStatus.OK.value()) {
      responseEntity = ResponseEntity.status(responseDTO.getStatusCode()).body(responseDTO.getMessage());
    } else {
      responseEntity = ResponseEntity.status(responseDTO.getStatusCode()).body(responseDTO.toString());
    }
    logger.info("{} Successfully fetched the employee data for : {} ",
        EmployeeDataConstants.LOGGER_PREFIX_API_INVOCATION, empName);
    logger.trace("Exit from :updateEmployeeData");
    return responseEntity;
  }
}