package com.weather.api.automate;

import static io.restassured.RestAssured.baseURI;


import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.weather.api.automate.utils.WeatherUtils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class LatLongWeatherData {
	
	protected static Properties config = new Properties();
	protected static InputStream file;
	protected static String validateUserURI;
	protected static RequestSpecification validateUserSpec;
	protected static String userName;
	protected static String password;
	protected static String key;
	protected static String coordinatesFPath;
	protected static Logger log = Logger.getLogger(LatLongWeatherData.class.getName());
	
	protected static WeatherUtils weatherUtil = new WeatherUtils();
	
	 

	@BeforeTest
	public static void setup() {
		//load the data from property file
		
		try {
			file = weatherUtil.loadProperties();
			config.load(file);
			baseURI = config.getProperty("baseURI");
			key = config.getProperty("key");
			validateUserURI = config.getProperty("validateUserURI");
			userName = config.getProperty("userName");
			password = config.getProperty("password");
			coordinatesFPath= config.getProperty("coordinatesFilePath");
			
			
			  if (validateUserURI != null) { validateUserSpec = new
			  RequestSpecBuilder().setBaseUri(validateUserURI).build(); }
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	/*AC2. “As a frequent flyer, I want to get current weather data for the city at 
	 * Latitude: -33.865143, Longitude: 151.209900”.*/
	@Test
	public void TestWeatherDataLatLong() {
		
		float temperature;
		JSONObject coordinatesJson;
		JsonPath jsonPath;
		Response weatherResp;
		log.info("As a frequent flyer, I want to get current weather data for the city at \n"
				+ "	Latitude: -33.865143, Longitude: 151.209900");
		int statusCode = weatherUtil.validateQFFUser(userName, password, validateUserSpec);

		if (statusCode == 200) {

			log.info("The current user is a valid Frequent Flyer member");

			// fetch the coordinates to get the weather data	
			Object coordinatesObj = weatherUtil.getDataFromFile(coordinatesFPath);
			coordinatesJson = new JSONObject((Map) coordinatesObj);
			//Set set = coordinatesJson.entrySet();
			
			weatherResp = weatherUtil.fetchWeatherDataLatLong((double)coordinatesJson.get("latitude"), (double)coordinatesJson.get("longitude"), key);
			jsonPath = weatherResp.jsonPath();
			temperature = jsonPath.getFloat("data[0].temp");
			log.info("Temperature for the given lat long is:"+temperature);
			
			
		
		
		
		
		
	}
	
	
	

}
}
