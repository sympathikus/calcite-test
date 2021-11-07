package de.paulsikorski.calcitetest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SqlOnFileTest {

	@Test
	void test() throws ClassNotFoundException {
		//Tell Calcite where to find model.json file
		Class.forName("org.apache.calcite.jdbc.Driver");
		Properties info = new Properties();
		info.setProperty("model", "src/test/resources/model.json");

		//Create a connection to calcite
		try(Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
				CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);){

			final CalciteQueryExecutor executor = new CalciteQueryExecutor(calciteConnection);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			final String sql1 = new StringBuilder().append("select * from ")
					.append("\"STUDENTS.json\"")
					.toString();


			final String sql2 = new StringBuilder().append("select * from ")
					.append("\"TOPICS.json\"")
					.toString();
			final String sql3 = new StringBuilder().append("select ")
					.append("\"STUDENTS.json\".matNr, \"STUDENTS.json\".name, \"TOPICS.json\".topic from ")
					.append("\"STUDENTS.json\" inner join \"TOPICS.json\" on ")
					.append("\"STUDENTS.json\".matNr=\"TOPICS.json\".matNr")
					.toString();
			Assertions.assertTrue(executor.executeStatement(sql1));
			Assertions.assertTrue(executor.executeStatement(sql2));
			Assertions.assertTrue(executor.executeStatement(sql3));


		} catch (SQLException e) {
			Assertions.fail(e);
		}

	}
}
