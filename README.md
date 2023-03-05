Weather API Automation 

What do I need?

A Java15 JDK, node js, Maven (3.8.1 recommended) and an IDE of your choice, I have chosen eclipse

Running the mock server

There is a simple node js server component shipped with this project, this is a simple user validation service. [https://github.com/yesveesteps/QFFUserService]

1) run the user validation mock server with the command below

- npm i
- npm start

This will start a server for user validation service at port 3000.

2) Then execute the below command from the terminal

Command to execute all the tests:

mvn test -DtestGroup=com/weather/api/automate -Dkey=b19a40587d344e8fa34944a7e35de60d

3) Command to execute a single test:

mvn -Dtest=<classname>#<method name> -Dkey=<key>test
eg:

mvn -Dtest=LatLongWeatherData#TestWeatherDataLatLong test


Report:

Test reports will be present in the Folder target/surefire-reports/emailable-report.html


