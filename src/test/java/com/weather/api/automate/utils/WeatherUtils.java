package com.weather.api.automate.utils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/*This class contains utils method for weather api project*/
public class WeatherUtils {
	
	/*This method will load the data from the json file path  
	 * and return as a file Object*/
	public Object getDataFromFile(final String filePath) {
		JSONParser jsonParser = new JSONParser();
		
		Object fileObj = null;
		try (InputStream reader = Files.newInputStream(Paths.get(filePath)))
        {
			 final String result = IOUtils.toString(reader, StandardCharsets.UTF_8);
            //Read JSON file
			fileObj = jsonParser.parse(result);
			
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileObj;
		
	}
	
	/*	This method will hit the QFF user validation service and 
	  return the status code for the given user name and password */
		
		public int validateQFFUser(final String username, final String pwd, final RequestSpecification validateUserSpec) {
			
			int statuscode=404;
			// validate the user is a frequent flyer
					Response res = given().spec(validateUserSpec).
									queryParam("username", username).
									queryParam("pwd", pwd).
									when().
									get("/qffvalidateuser");
					if(res!=null) {
						statuscode = res.getStatusCode();
					}
					else {
						statuscode = 404;
					}
			
			return statuscode;
		}
		
		/*Hit the Weather api endpoint for the given city code and return the response*/
		public Response fetchWeatherData(String cityCode, String cityName, String key) {
			
			final Response res = given().
								get("?city_id=" + cityCode + "&key=" + key).
								then().
								statusCode(200)
								.body("data[0].city_name", equalTo(cityName)).
								extract().
								response();
			
			return res;
		}
		/* Hit the Weather api endpoint for the given latitude and longitude for eg:
		 * http://api.weatherbit.io/v2.0/current?lat=-33.86&lon=151.20&key=123
		 * */
		public Response fetchWeatherDataLatLong(final double latitue, final double longitude, final String key) {
			
			final Response res = given().
					queryParam("lat", latitue).
					queryParam("lon", longitude).
					queryParam("key", key).
					when().
					get("/current").
					then().
					statusCode(200).
					extract().
					response();

			return res;
			
		}
		
		//load the config properties file and return
		
		public InputStream loadProperties()  {
			
			InputStream reader = null;
			
				try {
					  reader = Files.newInputStream(Paths.get("./src/test/resources/config.properties"));
							 
				} catch (FileNotFoundException fe) {
					fe.printStackTrace();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return reader;
			}
		
		
}
