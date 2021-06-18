package com.employeedata.endpoint.app.constants;

/**
 * @author Vidyashri
 *
 */
public class EmployeeDataConstants {

  /**
   * 
   */
  EmployeeDataConstants() {
    super();
  }

  public static final String LOG_PREFIX_EMPLOYEE_DATA_ENDPOINT_PROCESSOR = "[EmployeeDataStorageEndpoint]";
  public static final String LOGGER_PREFIX_API_INVOCATION = "[EmployeeDataStorageEndpoint][APIInvocation]";
  public static final String HTTP_HEADER_CONTENT = "application/json";
  public static final String CONTENT_TYPE = "Content-type";

  // Kafka topics for Saving the data
  public static final String KAFKA_SAVE_DATA_CONSUMER_TOPIC = "kafka.savedata.consumer.topic";
  public static final String KAFKA_SAVE_DATA_CONSUMER_GROUP = "kafka.savedata.consumer.group";

  // Kafka topics for Saving the data
  public static final String KAFKA_UPDATE_DATA_CONSUMER_TOPIC = "kafka.updatedata.consumer.topic";
  public static final String KAFKA_UPDATE_DATA_CONSUMER_GROUP = "kafka.updatedata.consumer.group";

  // Kafka topics for Saving the data
  public static final String FILE_TYPE_CSV = "CSV";
  public static final String FILE_TYPE_XML = "XML";
}
