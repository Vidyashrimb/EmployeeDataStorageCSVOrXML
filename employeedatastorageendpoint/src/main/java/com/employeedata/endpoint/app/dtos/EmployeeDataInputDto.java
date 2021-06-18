package com.employeedata.endpoint.app.dtos;

/**
 * 
 * @author Vidyashri
 *
 */
public class EmployeeDataInputDto {

  /**
   * 
   */
  private String name;
  private String dob;
  private double salary;
  private int age;
  private String fileType;

  /**
   * @param name
   * @param dob
   * @param salary
   * @param age
   * @param fileType
   */
  public EmployeeDataInputDto(String name, String dob, double salary, int age, String fileType) {
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
  public double getSalary() {
    return salary;
  }

  /**
   * @param salary the salary to set
   */
  public void setSalary(double salary) {
    this.salary = salary;
  }

  /**
   * @return the age
   */
  public int getAge() {
    return age;
  }

  /**
   * @param age the age to set
   */
  public void setAge(int age) {
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
