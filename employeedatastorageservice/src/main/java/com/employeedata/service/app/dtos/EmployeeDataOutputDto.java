package com.employeedata.service.app.dtos;

/**
 * 
 * @author Vidyashri
 *
 */
public class EmployeeDataOutputDto {

  /**
   * 
   */
  private String name;
  private String dob;
  private String salary;
  private String age;
  private String fileType;

  /**
   * 
   */
  public EmployeeDataOutputDto() {
    super();
  }

  /**
   * @param name
   * @param dob
   * @param salary
   * @param age
   * @param fileType
   */
  public EmployeeDataOutputDto(String name, String dob, String salary, String age, String fileType) {
    super();
    this.name = name;
    this.dob = dob;
    this.salary = salary;
    this.age = age;
    this.fileType = fileType;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the dob
   */
  public String getDob() {
    return dob;
  }

  /**
   * @param dob the dob to set
   */
  public void setDob(String dob) {
    this.dob = dob;
  }

  /**
   * @return the salary
   */
  public String getSalary() {
    return salary;
  }

  /**
   * @param salary the salary to set
   */
  public void setSalary(String salary) {
    this.salary = salary;
  }

  /**
   * @return the age
   */
  public String getAge() {
    return age;
  }

  /**
   * @param age the age to set
   */
  public void setAge(String age) {
    this.age = age;
  }

  /**
   * @return the fileType
   */
  public String getFileType() {
    return fileType;
  }

  /**
   * @param fileType the fileType to set
   */
  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

}
