package com.qa.utils;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;

import org.testng.SkipException;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.qa.base.TestBase;


public class readAndWriteData {


    public void classRunmodeCheck() {
    	System.out.println(TestBase.className);
		String classReadConfiguration=TestBase.config.getProperty("dataReadConfiguration");
		switch(classReadConfiguration) {

		case "json":
			System.out.println("runmode check from json...");
			try {
				if(!jsonReader.jsonTestClassRunmode(TestBase.className)) {
					throw new SkipException("Runmode of the test data is set to NO for class: "+TestBase.className);
				}
			} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
				e.printStackTrace();
			}
			break;
			
			
		case "excel":
			System.out.println("runmode check from excel...");
			if(!FiloExcelReader.readClassRunnerMode(TestBase.className).equalsIgnoreCase("Y")) {
				throw new SkipException("Runmode of the test data is set to NO for class: "+TestBase.className);
			}
			break;
			
		default:
			System.out.println("data read configuration setting is not properly resolved between json and excel");
			break;
		}

    }
    
    
    
    
    
    
    
	public void runmodeCheck( Hashtable<String,String> data){
		System.out.println(TestBase.className);
		String dataReadConfiguration=TestBase.config.getProperty("dataReadConfiguration");

		switch(dataReadConfiguration) {

		case "json":
			System.out.println("runmode check from json...");
			try {
				if(!jsonReader.jsonTestClassRunmode(TestBase.className)) {
					throw new SkipException("Runmode of the test data is set to NO for class: "+TestBase.className);
				}else if(!data.get("runmode").equalsIgnoreCase("Y")) {
					throw new SkipException("Runmode of the test data is set to NO for class: "+TestBase.className+" --- Test Case: "+TestBase.TEST);
				}
			} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
				e.printStackTrace();
			}
			break;
			
			
		case "excel":
			System.out.println("runmode check from excel...");
			if(!FiloExcelReader.readClassRunnerMode(TestBase.className).equalsIgnoreCase("Y")) {
				throw new SkipException("Runmode of the test data is set to NO for class: "+TestBase.className);
			}else if(!data.get("runmode").equalsIgnoreCase("Y")) {
				throw new SkipException("Runmode of the test data is set to NO for class: "+TestBase.className+" --- Test Case: "+TestBase.TEST);
			}
			break;
			
		default:
			System.out.println("data read configuration setting is not properly resolved between json and excel");
			break;
		}





	}



	public static Object[][] dataSet(Method m) throws FilloException {
		String testName=m.getName().trim();
		String className=m.getDeclaringClass().getSimpleName().toString().trim();
		Hashtable<String,String> table = null;
		Fillo fillo=new Fillo();
		Connection connection=fillo.getConnection(System.getProperty("user.dir")+"/Test_Suite_Data.xlsx");
		String query="Select * from "+className+" where Test_Case='"+testName+"'";
		Recordset rs=connection.executeQuery(query); 
		List<String> list=rs.getFieldNames();
		Object[][] data=new Object[rs.getCount()][1];
		int i=0;
		while(rs.next()) {
			table=new Hashtable<String,String>();
			for(int j=0;j<list.size();j++) {
				table.put(list.get(j), rs.getField(list.get(j)));

			}
			data[i][0]=table;
			i++;

		}

		rs.close();
		connection.close();

		return data;

	}



	/*************************APP SPECIFIC EXCEL FUNCTION******************************************/

}
