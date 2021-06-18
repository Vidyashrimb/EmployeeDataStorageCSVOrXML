package com.employeedata.service.app.dtos;

/**
 * 
 * @author Vidyashri
 *
 */
public class EmployeeDataReturnDto {

  /**
   * 
   */
  private String name;
  private String dob;
  private String salary;
  private String age;

  /**
   * 
   */
  public EmployeeDataReturnDto() {
    super();
  }

  /**
   * @param name
   * @param dob
   * @param salary
   * @param age
   */
  public EmployeeDataReturnDto(String name, String dob, String salary, String age) {
    super();
    this.name = name;
    this.dob = dob;
    this.salary = salary;
    this.age = age;
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

}
