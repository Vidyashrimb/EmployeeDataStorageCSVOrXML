package com.employeedata.service.app.service;

import com.employeedata.service.app.dtos.ResponseDto;

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
  ResponseDto getEmployeeData(String empName, String fileType);
}