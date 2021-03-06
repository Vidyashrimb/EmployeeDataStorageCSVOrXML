package com.employeedata.endpoint.app.service;

import com.employeedata.endpoint.app.dtos.ResponseDto;

import org.springframework.http.ResponseEntity;

import com.employeedata.endpoint.app.dtos.EmployeeDataInputDto;

/**
 * @author Vidyashri
 *
 */
public interface EmployeeDataStorageEndpointService {

  /**
   * 
   * @param employeeDataInputDto
   * @return
   */
  ResponseDto saveNewEmployeeData(EmployeeDataInputDto employeeDataInputDto);

  /**
   * 
   * @param employeeDataInputDto
   * @return
   */
  ResponseDto updateEmployeeData(EmployeeDataInputDto employeeDataInputDto);

  /**
   * 
   * @param empName
   * @return
   */
  ResponseEntity<Object> getEmployeeData(String empName);
}