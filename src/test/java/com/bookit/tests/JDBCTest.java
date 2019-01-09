package com.bookit.tests;

import org.testng.annotations.Test;

import com.bookit.utilities.DBUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCTest {
	String dbUrl = "jdbc:postgresql://localhost:5432/hr"; 
	//database connection:database :local database or external:port number:database name
	String dbUsername = "postgres";
	String dbPassword = "abc";
	
	@Test (enabled=false)
	public void PostGreSQL() throws SQLException {
		Connection connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = statement.executeQuery("select * from employees");
		
		//first it points to 0, null, so it doesn't get any result. when we want to get the row, we use next method
//		resultset.next(); // next method -> moves pointer to next row
//		resultset.next();
//		System.out.println(resultset.getString("country_id")); //use column name or column index
//		System.out.println(resultset.getString(2));//column index stars from 1
//		resultset.next();
//		System.out.println(resultset.getString(1)+"-"+resultset.getString("country_name")+"-"+resultset.getInt("region_id"));
		
		//To get all the results, use loop
//		while(resultset.next()) { //resultset.next() will turn false once there are no more results. 
//			System.out.println(resultset.getString(1)+"-"+resultset.getString("country_name")+"-"+resultset.getInt("region_id"));
//		}
		
//		resultset.next();
//		resultset.next();
//		resultset.next();
//		resultset.next();
//		
//		System.out.println(resultset.getString(1)+"-"+resultset.getString("country_name")+"-"+resultset.getInt("region_id"));
//		
//		resultset.first();
//		System.out.println(resultset.getString(1)+"-"+resultset.getString("country_name")+"-"+resultset.getInt("region_id"));
		
		//To find out how many records in the resultset
		resultset.last();
		int rowsCount = resultset.getRow();
		System.out.println(rowsCount);
		
		//how to move first row and loop everything again
		resultset.beforeFirst();//it will first point to null, so with resultset.next() you can start with row 1
		while(resultset.next()) {  
			System.out.println(resultset.getInt(1)+"-"+resultset.getString("first_name")+" "+resultset.getString("last_name"));
		}
		
		resultset.close();
		statement.close();
		connection.close();
	}
	
	@Test (enabled=false)
	public void JDBCMetaData() throws SQLException {
		Connection connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = statement.executeQuery("select * from countries");
		
		//database metadata(create object)
		DatabaseMetaData dbMetadata = connection.getMetaData();
		
		//which username are we using?
		System.out.println("User: "+dbMetadata.getUserName());
		//database product name
		System.out.println("Database Product Name:"+dbMetadata.getDatabaseProductName());
		//database product version
		System.out.println("Database Product Version"+ dbMetadata.getDatabaseProductVersion());
		
		//result metadata create object -> it helps to know how many columns, what are the column names
		ResultSetMetaData rsMetadata = resultset.getMetaData();
		//how many columns?
		System.out.println("Columns count: "+ rsMetadata.getColumnCount());
		//get column name
		System.out.println(rsMetadata.getColumnName(1));
		System.out.println(rsMetadata.getTableName(1));
		
		//print all column names using a loop
		int columnCount = rsMetadata.getColumnCount();
		for (int i=1; i<=columnCount; i++) {
			System.out.println(rsMetadata.getColumnName(i));
		}
	
		resultset.close();
		statement.close();
		connection.close();
	}
	
	@Test (enabled=false)
	public void DBUtil() throws SQLException {
	  Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
	  Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);;
	  ResultSet resultset = statement.executeQuery("SELECT first_name, last_name, salary, job_id FROM employees LIMIT 5");
	  
	  //database metadata(create object)
//	  DatabaseMetaData dbMetadata =connection.getMetaData();
	  
	  //resultset metadata create object
//	  ResultSetMetaData rsMetadata = resultset.getMetaData();
	  
	  //our main structure, it will keep the whole query results
	  List<Map<String,Object>> queryData = new ArrayList<>();
	  
	  //we will add the first row data to this map
	  Map<String,Object> row1 = new HashMap<>();
	  
	  //point to the first row
	  resultset.next();
	  
	  //key is column name, value is value of that column
	  row1.put("first_name", "Steven");
	  row1.put("last_name", "King");
	  row1.put("Salary", 24000);
	  row1.put("job_id", "AD_PRES");
	  
	  //verify map is keeping the data
	  System.out.println(row1.toString());
	  
	  //push row1 map to the list as a whole row
	  queryData.add(row1);
	  
	  //get first element in the list and the value from the map
	  System.out.println(queryData.get(0).get("first_name"));
	  
	//--------------ADDING ONE MORE ROW----------
	  Map<String,Object> row2 = new HashMap<>();
	  
	  resultset.next();
	  
	  row2.put("first_name", resultset.getObject("first_name"));
	  row2.put("last_name", resultset.getObject("last_name"));
	  row2.put("Salary", resultset.getObject("salary"));
	  row2.put("Job_id", resultset.getObject("job_id"));
	  
	  queryData.add(row2);

	  System.out.println("First Row: "+queryData.get(0).toString());
	  System.out.println("Second Row: "+queryData.get(1).toString());
	  
	  resultset.close();
	  statement.close();
	  connection.close();
	}

	@Test(enabled=false)
	public void DBUtilDynamic() throws SQLException {
	  Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
	  Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);;
	  ResultSet resultset = statement.executeQuery("SELECT * from countries");
	  
	  //database metadata(create object)
//	  DatabaseMetaData dbMetadata =connection.getMetaData();
	      
	  //resultset metadata create object
	  ResultSetMetaData rsMetadata = resultset.getMetaData();
	  
	  // --- DYNAMIC LIST FOR EVERY QUERY ---
	  
	  //Main list
	  List<Map<String,Object>> queryData = new ArrayList<>();
	  
	  //Number of columns
	  int colCount = rsMetadata.getColumnCount();
	  
	  while(resultset.next()) {
		  
		  //creating map to adding each row inside	  
		  Map<String,Object> row = new HashMap<>();
		  
		  //dynamically getting values from rows
		  for(int i=1; i<=colCount;i++) {
			  row.put(rsMetadata.getColumnName(i), resultset.getObject(i));
		  }
		  
		  
		  //adding each row map to the list
		  queryData.add(row);
		  
	  }
	  
	  //printing one row in the list
	  System.out.println(queryData.get(0));
	  
	  //printing all rows with iterator loop
	  for (int i=0; i<queryData.size(); i++) {
		  System.out.println(queryData.get(i));
	  }
	  
	  //printing all rows with for each loop
	  for (Map<String, Object> map : queryData) {
          System.out.println(map.get("country_name"));
      }
	  
	  resultset.close();
	  statement.close();
	  connection.close();
	}
	
	@Test(enabled=false)
	public void useDBUtils() {
		//create connection with given information
		DBUtils.createConnection();
		
		String query = "SELECT first_name,last_name,job_id,salary FROM employees";
		List<Map<String,Object>> queryData = DBUtils.getQueryResultMap(query);
		
		//printout first row salary value
		System.out.println(queryData.get(0).get("salary"));
		
		//close connection
		DBUtils.destroy();
		
	}
	
	@Test
	public void useDBUtils2() {
		//create connection with given information
		DBUtils.createConnection();
		
		String query = "SELECT first_name,last_name,salary,job_id FROM employees where employee_id = 107";
		Map<String,Object> onerowresult = DBUtils.getRowMap(query);
		
		//printout first row salary value
		System.out.println(onerowresult.get("job_id"));
		
		//close connection
		DBUtils.destroy();
		
	}
}
