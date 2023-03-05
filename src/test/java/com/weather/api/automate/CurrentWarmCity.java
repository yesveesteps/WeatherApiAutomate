package com.weather.api.automate;

import static io.restassured.RestAssured.baseURI;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.weather.api.automate.utils.WeatherUtils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CurrentWarmCity {
	
	protected static Properties config = new Properties();
	protected static InputStream file;
	protected static String validateUserURI;
	protected static RequestSpecification validateUserSpec;
	protected static String userName;
	protected static String password;
	protected static String key;
	protected static String captialCitiesFPath;
	protected static Logger log = Logger.getLogger(CurrentWarmCity.class.getName());
	protected final int HTTP_SUCCESS = 200;
	
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
			captialCitiesFPath= config.getProperty("captialCitiesOfAustralia");
			
			
			  if (validateUserURI != null) { validateUserSpec = new
			  RequestSpecBuilder().setBaseUri(validateUserURI).build(); }
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*AC3. “As a frequent flyer, 
	 * I want to programmatically find the current warmest capital city in Australia”*/
	@Test
	public void FindWarmestCapitalCityInAustrlia() {
		

		JsonPath jsonPath;
		Float temperature;
		Float warmestTemperature=(float) 0.0;
		Response weatherResp;
		String warmestCity = null;
		log.info("As a frequent flyer, I want to programmatically find the"
				+ "current warmest capital city in Australia");
		// validate the user is a frequent flyer
		int statusCode = weatherUtil.validateQFFUser(userName, password, validateUserSpec);

		if (statusCode == HTTP_SUCCESS) {

			log.info("The current user is a valid Frequent Flyer member");

			// fetch the multiple cities to get the weather data	
			Object captialCitiesList = weatherUtil.getDataFromFile(captialCitiesFPath);

			JSONObject citiesJson = new JSONObject((Map) captialCitiesList);
			Set set = citiesJson.entrySet();
			Iterator itr = set.iterator();
			
			// for each city code fetch the weather data	
			while (itr.hasNext()) {
				 Map.Entry item = (Map.Entry) itr.next();	
				 weatherResp = weatherUtil.fetchWeatherData((String)item.getKey(), (String)item.getValue(), key);
				 //get the temperature from the response
				 jsonPath = weatherResp.jsonPath();
				 temperature = jsonPath.getFloat("data[0].temp");
				 log.info("Weather for the city " + item.getValue() + " is: " + temperature);
				 if(temperature >= warmestTemperature) {
					 warmestTemperature =temperature;
					 warmestCity=(String)item.getValue();
				 }
			}
			log.info("Warmest city is " + warmestCity + " is: " + warmestTemperature);

		}else {
			log.info("The given user is not a valid Frequent Flyer member");
			Assert.assertEquals(statusCode, 200);
		}
		
		
		
	}

}
