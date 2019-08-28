package com.qa.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class jsonReader {

@Test
public void testing() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
	System.out.println(jsonTestClassRunmode("LoginPageTest"));
}

	@DataProvider
	public static Object[][] jsonTestDataSet(Method m) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		JsonParser jsonParser= new JsonParser();
		System.out.println("declaring class of method"+m.getDeclaringClass().getSimpleName().toString());
		JsonObject jsonObj = jsonParser.parse(new FileReader(System.getProperty("user.dir")+"/src/test/data/"+m.getDeclaringClass().getSimpleName().toString()+".json")).getAsJsonObject();
		JsonArray array= (JsonArray) jsonObj.get("DataSet");
		int i=0,j=0;
		Hashtable<String,String> table=null;
		for(JsonElement jsonElement:array) {
			if(jsonElement.toString().contains((m.getName().toString()))){
				i++;
			}
		}
		Object[][] matrix= new Object[i][1];
		for(JsonElement jsonElement :array)
		{
			if(jsonElement.toString().contains(m.getName().toString()))
			{
				table=new Hashtable<String,String>();
				for(Entry<String, JsonElement> entry:jsonElement.getAsJsonObject().entrySet())
				{
					String key = entry.getKey().toString();
					String value= entry.getValue().toString().replace("\"", "").toString();		
					table.put(key, value);			
				}
				matrix[j][0]=table;
				j++;
			}
		}
		return matrix;
	}
	
	public static boolean jsonTestClassRunmode(String className) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		JsonParser jsonParser= new JsonParser();
		JsonObject jsonObj = jsonParser.parse(new FileReader(System.getProperty("user.dir")+"/src/test/data/ClassRunner.json")).getAsJsonObject();
		JsonArray array= (JsonArray) jsonObj.get("TestClassRunmode");
		for(JsonElement jsonElement :array)
		{
			if(jsonElement.toString().contains(className)) {
				for(Entry<String,JsonElement>entry:jsonElement.getAsJsonObject().entrySet()) {
					String value=entry.getValue().toString().replace("\"", "").toString();
					if(value.equalsIgnoreCase("Y")) {
						return true;
						
					}
				}
			}
		}
		
		return false;
		
	}
	
	public static HashMap<String,String> jsonGetTestClasses() throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		JsonParser jsonParser= new JsonParser();
		JsonObject jsonObj = jsonParser.parse(new FileReader(System.getProperty("user.dir")+"/src/test/data/ClassRunner.json")).getAsJsonObject();
		JsonArray array= (JsonArray) jsonObj.get("TestClassRunmode");
		HashMap<String,String> getTestSet=new HashMap<String,String>();;
		for(JsonElement jsonElement :array)
		{
			for(Entry<String,JsonElement>entry:jsonElement.getAsJsonObject().entrySet()) {
				
				if(entry.getKey().equalsIgnoreCase("TestClass")) {
					getTestSet.put(entry.getValue().toString().replace("\"", "").toString(), entry.getValue().toString().replace("\"", "").toString());
				}
			}
		} System.out.println(getTestSet);
		return getTestSet;
	}
}
