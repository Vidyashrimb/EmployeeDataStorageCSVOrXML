package com.employeedata.service.app.dtos;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Vidyashri
 *
 */
@XmlRootElement
public class EmployeeDataXmlDto {

  private List<EmployeeDataDto> employeeDataDto;

  /**
   * 
   */
  public EmployeeDataXmlDto() {
    super();
  }

  /**
   * @param employeeDataDto
   */
  public EmployeeDataXmlDto(List<EmployeeDataDto> employeeDataDto) {
    super();
    this.employeeDataDto = employeeDataDto;
  }

  /**
   * @return the employeeDataDto
   */
  @XmlElement
  public List<EmployeeDataDto> getEmployeeDataDto() {
    return employeeDataDto;
  }

  /**
   * @param employeeDataDto the employeeDataDto to set
   */
  public void setEmployeeDataDto(List<EmployeeDataDto> employeeDataDto) {
    this.employeeDataDto = employeeDataDto;
  }

}
