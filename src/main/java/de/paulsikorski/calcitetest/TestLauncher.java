package de.paulsikorski.calcitetest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestLauncher {
	
	private static final Logger LOG = LogManager.getLogger(TestLauncher.class);
	
	public static void main(String... args) throws SQLException, ClassNotFoundException {
			
		LOG.info("Telling calcite where to find model.json file");
		Class.forName("org.apache.calcite.jdbc.Driver");
		//Tell Calcite where to find model.json file
		Properties info = new Properties();
		info.setProperty("model", "src/main/resources/model.json");
		
		//Create a connection to calcite
		LOG.info("Creating a connection to calcite");
		Connection connection =
		    DriverManager.getConnection("jdbc:calcite:", info);
		CalciteConnection calciteConnection =
		    connection.unwrap(CalciteConnection.class);
		
		//Create and execute statement
		LOG.info("Creating and executing join statement");
		
		//SELECT on both tables
		try(Statement statement = calciteConnection.createStatement();
			ResultSet selectStudentsResult = calciteConnection.createStatement().executeQuery("Select * from students")) {
			
			ResultSetMetaData rSMD = selectStudentsResult.getMetaData();
			int columnsNumber2 = rSMD.getColumnCount();
			   while (selectStudentsResult.next()) {
			       for (int i = 1; i <= columnsNumber2; i++) {
			           if (i > 1) System.out.print(",  ");
			           String columnValue = selectStudentsResult.getString(i);
			           System.out.print(columnValue + " " + rSMD.getColumnName(i));
			       }
			   System.out.println("");
			}
		} 
		
		try(Statement statement = calciteConnection.createStatement();
				ResultSet selectStudentsResult = statement.executeQuery("Select * from topics")) {
			
			ResultSetMetaData rSMD = selectStudentsResult.getMetaData();
			int columnsNumber2 = rSMD.getColumnCount();
			   while (selectStudentsResult.next()) {
			       for (int i = 1; i <= columnsNumber2; i++) {
			           if (i > 1) System.out.print(",  ");
			           String columnValue = selectStudentsResult.getString(i);
			           System.out.print(columnValue + " " + rSMD.getColumnName(i));
			       }
			   System.out.println("");
			}
		}
		
		
		//Query with join
		try(Statement statement = calciteConnection.createStatement();
			ResultSet queryJoinResult = statement.executeQuery("select students.matNr, students.name, topics.topic "
					+ "from students inner join topics on students.matNr=topics.matNr")) {
			
			ResultSetMetaData rSMD = queryJoinResult.getMetaData();
			while (queryJoinResult.next()) {
				for (int i = 1; i <= rSMD.getColumnCount(); i++) {
		           if (i > 1) System.out.print(",  ");
		           String columnValue = queryJoinResult.getString(i);
		           System.out.print(columnValue + " " + rSMD.getColumnName(i));
				}
		    System.out.println("");
	   }
		}
		connection.close();
	}
	

}
