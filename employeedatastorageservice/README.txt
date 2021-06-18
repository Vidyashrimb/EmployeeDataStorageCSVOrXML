Use-case:
Consumes the messages published from the employee endpoint for saving and updating. Processes the data and saves or updates in CSV or XML file as per the requested file type. And exposes an API to get the employee data stored in either CSV or XML file.

Assumptions:
The store and update operations of the employee data will be forwarded to the service layer using kafka protocol with the Google’s protocol buffer format encrypted payload. (EmployeeDataProto.java generated from employeedata_message.proto in the resources)
The read operation of the employee data will be using the HTTP.
The kafka broker will be running in the localhost (IP is configiured as localhost)
The program will creates the CSV or XML file when the first employee data is stored, further the program will update the employee details to the existing file.
The file names are fixed to EmployeeData.csv and EmployeeData.xml

The Project structure:
There are total 7 packages.
com.employeedata.service.app - Main Application
com.employeedata.service.app.constants - Contains the file with constants required in the project
com.employeedata.service.app.consumer
com.employeedata.service.app.controller - Contains the controller which will have the REST API required in the project.
com.employeedata.service.app.dtos - Contains the DTO objects required in the project.
com.employeedata.service.app.kafkautil - Contains the kafka consumer config, topic creation object and the KafkaConsumer template.
com.employeedata.service.app.protobuf - Contains the protocol buffer java file generated from the employeedata_message.proto in the resources folder.
com.employeedata.service.app.service - Contains the service class and it's implementation to process the request from the controller and fetches the employee data for the specified employee name and the fileType.

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
Request URL Signature : http://<localhostip>:8083/employeservice/getemployeedata/{empName}/{fileType}
Sample URL : http://192.168.43.6:8083/employeservice/getemployeedata/{empName}/{fileType}

Sample Response payload : 
{
    "name": "XYZ",
    "dob": "2021-06-18",
    "salary": "55000.0",
    "age": "25"
}