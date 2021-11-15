package de.paulsikorski.calcitetest;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CalciteQueryExecutor {

	private static final Logger LOG = LogManager.getLogger(CalciteQueryExecutor.class);
	private final CalciteConnection calciteConnection;

	public CalciteQueryExecutor(CalciteConnection calciteConnection) {
		this.calciteConnection = calciteConnection;
	}

	public boolean executeStatement(String sqlQuery) {
		try(ResultSet selectStudentsResult = calciteConnection.createStatement().executeQuery(sqlQuery)) {
				
			final ResultSetMetaData rSMD = selectStudentsResult.getMetaData();
			int columnsNumber2 = rSMD.getColumnCount();
			while (selectStudentsResult.next()) {
				for (int i = 1; i <= columnsNumber2; i++) {
					if (i > 1) System.out.print(",  ");
				    	String columnValue = selectStudentsResult.getString(i);
				        System.out.print(columnValue + " " + rSMD.getColumnName(i));
				    }
				System.out.println("");
			}
			return true;
		} catch (SQLException e) {
			LOG.error(String.format("Error while executing statement '%s'", sqlQuery), e);
			return false;
		} 
		
	}

}
