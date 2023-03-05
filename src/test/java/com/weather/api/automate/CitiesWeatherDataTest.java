package com.weather.api.automate;


import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.weather.api.automate.utils.WeatherUtils;

import static io.restassured.RestAssured.*;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import java.io.InputStream;

import org.json.simple.JSONObject;
import java.util.logging.Logger;


/**
 * This class is used to test - get current weather data for multiple
	 * cities in the world
 * */
public class CitiesWeatherDataTest {

	protected static Properties config = new Properties();
	protected static InputStream file;
	protected static String validateUserURI;
	protected static RequestSpecification validateUserSpec;
	protected static String userName;
	protected static String password;
	protected static String key;
	protected static String multiCitiesFPath;
	protected static Logger log = Logger.getLogger(CitiesWeatherDataTest.class.getName());
	
	static WeatherUtils weatherUtil = new WeatherUtils();
	
	 

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
			multiCitiesFPath= config.getProperty("multipleCitiesList");
			
			
			  if (validateUserURI != null) { validateUserSpec = new
			  RequestSpecBuilder().setBaseUri(validateUserURI).build(); }
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * AC1. “As a frequent flyer, I want to get current weather data for multiple
	 * cities in the world”.
	 */
	@Test
	public void TestWeatherForMultipleCities() {
		
		
		JsonPath jsonPath;
		Float temperature;
		Response weatherResp;
		log.info("As a frequent flyer, I want to get current weather data for multiple\n"
				+ "cities in the world");
		// validate the user is a frequent flyer
		int statusCode = weatherUtil.validateQFFUser(userName, password, validateUserSpec);

		if (statusCode == 200) {

			log.info("The current user is a valid Frequent Flyer member");

			// fetch the multiple cities to get the weather data	
			final Object citiesList = weatherUtil.getDataFromFile(multiCitiesFPath);

			//System.out.println(citiesList);
			JSONObject citiesJson = new JSONObject((Map) citiesList);
			Set set = citiesJson.entrySet();
			Iterator itr = set.iterator();
			while (itr.hasNext()) {
				Map.Entry item = (Map.Entry) itr.next();

				// for each city code fetch the weather data				
				 weatherResp = weatherUtil.fetchWeatherData((String)item.getKey(), (String)item.getValue(), key);
				 jsonPath = weatherResp.jsonPath();
				 temperature = jsonPath.getFloat("data[0].temp");

				
				log.info("Weather for the city " + item.getValue() + " is: " + temperature);

			}

		}else {
			log.info("The given user is not a valid Frequent Flyer member");
			Assert.assertEquals(statusCode, 200);
		}

	}
}
