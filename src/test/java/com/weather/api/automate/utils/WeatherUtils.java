package com.weather.api.automate.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WeatherUtils {
	
	public Object getCitiesList() {
		JSONParser jsonParser = new JSONParser();
		Object citiesList = null;
		try (FileReader reader = new FileReader("./src/test/resources/cities.json"))
        {
            //Read JSON file
             citiesList = jsonParser.parse(reader);
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return citiesList;
		
	}
}
