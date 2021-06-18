package com.employeedata.service.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.employeedata.service.app.constants.EmployeeDataConstants;
import com.employeedata.service.app.dtos.ResponseDto;
import com.employeedata.service.app.service.EmployeeDataStorageService;

/**
 * @author Vidyashri
 *
 */
@RestController
public class EmployeeDataStorageServiceController {

  private static final Logger logger = LoggerFactory.getLogger(EmployeeDataStorageServiceController.class);

  // Instance of the EmployeeDataStorageService
  @Autowired
  private EmployeeDataStorageService employeeDataStorageService;

  /**
   * Method to update the Employee Data
   * 
   * @param dataInputDto
   * @return
   */
  @GetMapping(value = "/getemployeedata/{empName}/{fileType}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseDto getEmployeeData(@PathVariable String empName, @PathVariable String fileType) {
    logger.trace("Enter inside :getEmployeeData");
    ResponseDto responseDto = employeeDataStorageService.getEmployeeData(empName, fileType);
    logger.info("{} Successfully fetched the employee data for : {}",
        EmployeeDataConstants.LOGGER_PREFIX_API_INVOCATION, empName);
    logger.trace("Exit from :getEmployeeData");
    return responseDto;
  }
}