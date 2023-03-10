<img width="185" alt="image" src="https://user-images.githubusercontent.com/22021276/222979563-dfebbe9b-d342-4e0b-8b2d-a67543891023.png">

<h1> Weather API Automation Suite </h1>

This project has automated API tests created using Rest Assured and TestNG framework for a REST API that provides weather data.

<h2> What do I need? </h2>

A Java15 JDK, node js, Maven (3.8.1 recommended) and an IDE of your choice, I have chosen eclipse

<h2> Running the mock server </h2>

There is a simple node js server component shipped with this project, called user validation service. [https://github.com/yesveesteps/QFFUserService] . Checkout the code then peform the steps below.

1) run the user validation mock server with the command below

- npm i
- npm start

This will start a http server at port 3000.

<h2> Running the Test suite </h2>

2) Then execute the below command from the terminal

Command to execute all the tests:

mvn clean test

(or)

mvn test -DtestGroup=com/weather/api/automate -Dkey=b19a40587d344e8fa34944a7e35de60d

3) Command to execute a single test:

mvn -Dtest=<classname>#<method name> -Dkey=<key>test

example:

mvn -Dtest=LatLongWeatherData#TestWeatherDataLatLong test

<h2> Project Structure </h2>
  
<img width="557" alt="image" src="https://user-images.githubusercontent.com/22021276/222982299-b4e15f57-dac7-4154-89b7-a66aa852608c.png">


<h2> Report: </h2>

Test reports will be present in the Folder target/surefire-reports/emailable-report.html
  <img width="854" alt="image" src="https://user-images.githubusercontent.com/22021276/222979205-9a77c3ea-1554-4e37-bd2e-c01401b85e60.png">

  <h2> API reference: </h2>

The metadata for this API used in this project can be found here: 

https://www.weatherbit.io/api/meta

The interactive swagger documentation for the API used in this project can be found here :

https://www.weatherbit.io/api/swaggerui/weather-api-v2

