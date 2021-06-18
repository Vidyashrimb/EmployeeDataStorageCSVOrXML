Use-case:
Accept the employee data (name, dob, salary, age and fileType) in the json format and forward it to the storage service to store it in either the CSV or XML File based on the fileType mentioned in the request payload. And exposes 3 API's to store, update and read the employee data.

Assumptions:
DOB should be in the yyyy-MM-dd format.
File type can be either CSV or XML (csv and xml also allowed)
File type cannot be updated for a particular employee.
The store and update operations of the employee data will be forwarded to the service layer using kafka protocol with the Google’s protocol buffer format encrypted payload. (EmployeeDataProto.java generated from employeedata_message.proto in the resources)
The read operation of the employee data will be using the HTTP.
The kafka broker will be running in the localhost (IP is configiured as localhost)

The Project structure:
There are total 7 packages.
com.employeedata.endpoint.app - Main Application
com.employeedata.endpoint.app.constants - Contains the file with constants required in the project
com.employeedata.endpoint.app.controller - Contains the controller which will have the REST API required in the project.
com.employeedata.endpoint.app.dtos - Contains the DTO objects required in the project.
com.employeedata.endpoint.app.kafkautil - Contains the kafka producer config, topic creation object and the KafkaProducer template.
com.employeedata.endpoint.app.protobuf - Contains the protocol buffer java file generated from the employeedata_message.proto in the resources folder.
com.employeedata.endpoint.app.service - Contains the service class and it's implementation to process the requests from the controller and forward the messages to the service layer using kafka.

Kafka topics and groups used for store and update data messages:
kafka.savedata.consumer.topic
kafka.savedata.consumer.group
kafka.updatedata.consumer.topic
kafka.updatedata.consumer.group

Project Setup and steps to run
Once the project is down-loaded from the Git, we can see that the the main folder has 2 directories.
1. employeedatastorageendpoint
2. employeedatastorageservice
Import both modules in Java Eclipse or STS.
Then mvn clean and mvn install.
Start the application.

API Supported :

Store employee data :
Request URL Signature : http://<localhostip>:8083/employeedata/
Sample URL : http://192.168.43.6:8082/employeedata/
Request method : POST

Sample Request payload : 
{
    "name": "XYZ",
    "dob": "2021-06-18",
    "salary": 55000.0,
    "age": 25
    "fileType" : "CSV"
}

Sample Response payload : 
{
    "statusCode": 201,
    "message": "Successfully saved the employee data for user : XYZ"
}

Update employee data :
Request URL Signature : http://<localhostip>:8083/employeedata/
Sample URL : http://192.168.43.6:8082/employeedata/
Request method : PUT

Sample Request payload : 
{
    "name": "XYZ",
    "dob": "2021-06-18",
    "salary": 55000.0,
    "age": 25
    "fileType" : "CSV"
}

Sample Response payload : 
{
    "statusCode": 201,
    "message": "Successfully updated the employee data for user : XYZ"
}

Get employee data :
Request URL Signature : http://<localhostip>:8083/employeedata/{empName}
Sample URL : http://192.168.43.6:8082/employeedata/XYZ
Request method : PUT
Sample Response payload : NA

Sample Response payload : 
{
    "statusCode": 201,
    "message": "Successfully updated the employee data for user : XYZ"
}