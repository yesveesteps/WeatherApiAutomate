package com.weather.api.automate;


import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.weather.api.automate.utils.WeatherUtils;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.json.simple.JSONObject;

public class CitiesWeatherDataTest {

	static Properties config = new Properties();
	static FileInputStream file;
	static String validateUserURI;
	static RequestSpecification validateUserSpec;
	static String key;
	{
		try {
			file = new FileInputStream("./src/test/resources/config.properties");
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();

		}
	}

	@BeforeTest
	public static void setup() {
		try {
			config.load(file);
			baseURI = config.getProperty("baseURI");
			key = config.getProperty("key");
			validateUserURI = config.getProperty("validateUserURI");
			if (validateUserURI != null) {
				validateUserSpec = new RequestSpecBuilder().setBaseUri(validateUserURI).build();
			}

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

		// validate the user is a frequent flyer
		Response res = given().spec(validateUserSpec).
						queryParam("username", "test1@gmail.com").
						queryParam("pwd", "secret123").
						when().
						get("/qffvalidateuser");

		if (res.getStatusCode() == 200) {

			System.out.println("The current user is a valid Frequent Flyer member");

			// fetch the multiple cities to get the weather data
			WeatherUtils weatherUtil = new WeatherUtils();
			Object citiesList = weatherUtil.getCitiesList();

			//System.out.println(citiesList);
			JSONObject citiesJson = new JSONObject((Map) citiesList);
			Set set = citiesJson.entrySet();
			Iterator itr = set.iterator();
			while (itr.hasNext()) {
				Map.Entry item = (Map.Entry) itr.next();

				// for each city code fetch the weather data

				Response weatherResp = given().
										get("?city_id=" + item.getKey() + "&key=" + key).
										then().
										statusCode(200)
										.body("data[0].city_name", equalTo(item.getValue())).
										extract().
										response();

				JsonPath jsonPath = weatherResp.jsonPath();
				Float temperature = jsonPath.getFloat("data[0].temp");

				System.out.println("Weather for the city " + item.getValue() + " is: " + temperature);

			}

		}else {
			System.out.println("The given user is not a valid Frequent Flyer member");
		}

	}
}
