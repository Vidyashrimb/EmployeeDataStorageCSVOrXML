package com.employeedata.service.app.dtos;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Vidyashri
 *
 */
@XmlRootElement
public class EmployeeDataDto {

  /**
   * 
   */
  private String name;
  private Date dob;
  private double salary;
  private int age;
  private String fileType;

  /**
   * 
   */
  public EmployeeDataDto() {
    super();
  }

  /**
   * @param name
   * @param dob
   * @param salary
   * @param age
   * @param fileType
   */
  public EmployeeDataDto(String name, Date dob, double salary, int age, String fileType) {
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
  @XmlElement
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
  @XmlElement
  public Date getDob() {
    return dob;
  }

  /**
   * @param dob the dob to set
   */
  public void setDob(Date dob) {
    this.dob = dob;
  }

  /**
   * @return the salary
   */
  @XmlElement
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
  @XmlElement
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
  @XmlElement
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
