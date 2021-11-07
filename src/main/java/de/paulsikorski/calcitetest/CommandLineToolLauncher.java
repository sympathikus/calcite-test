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

public class CommandLineToolLauncher {

	private static final Logger LOG = LogManager.getLogger(CommandLineToolLauncher.class);

	public static void main(String... args) throws SQLException, ClassNotFoundException {


		//Tell Calcite where to find model.json file
		Class.forName("org.apache.calcite.jdbc.Driver");
		Properties info = new Properties();
		info.setProperty("model", "src/test/resources/model.json");

		//Create a connection to calcite
		try(Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
				CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);){

			final CalciteQueryExecutor executor = new CalciteQueryExecutor(calciteConnection);
			LOG.info("Connected to Sql-On-File");
			System.out.println("Welcome to Sql-On-File. Please insert your queries or type quit, q or exit to exit.");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			while(true) {
				String input = br.readLine();
				if(!(input == null || input.equals(""))) {
					if(input.equals("quit") || input.equals("q") || input.equals("exit")) {
						break;
					}
					final boolean wasSuccessful = executor.executeStatement(input);
					if(!wasSuccessful) {
						LOG.error("Execution of statement {} failed", input);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
