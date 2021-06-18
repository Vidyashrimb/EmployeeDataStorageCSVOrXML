package com.employeedata.service.app.service;

import org.springframework.http.ResponseEntity;

/**
 * @author Vidyashri
 *
 */
public interface EmployeeDataStorageService {

  /**
   * 
   * @param empName
   * @param fileType
   * @return
   */
  ResponseEntity<Object> getEmployeeData(String empName, String fileType);
}