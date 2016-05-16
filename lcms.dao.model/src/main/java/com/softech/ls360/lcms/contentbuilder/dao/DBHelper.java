package com.softech.ls360.lcms.contentbuilder.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;

public class DBHelper {

	private static Logger logger = Logger.getLogger(DBHelper.class);
	private final static String dbConnectionString = LCMSProperties.getLCMSProperty("lcms.database.connectionstring");
	private final static String dbUsername = LCMSProperties.getLCMSProperty("lcms.database.username");
	private final static String dbPassword = LCMSProperties.getLCMSProperty("lcms.database.password");
	private final static String dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	public static Connection getDBConnection() {
		 
		Connection dbConnection = null; 
		
		try {
			Class.forName(dbDriver); 
		} 
		catch (ClassNotFoundException e) { 
			logger.error(e.getMessage()); 
		} 
		try {
			logger.info("Getting DB Connection");
			dbConnection = DriverManager.getConnection(dbConnectionString, dbUsername, dbPassword);
			return dbConnection;
 
		} catch (SQLException e) { 
			logger.error(e.getMessage());
		}
 
		return dbConnection;
 
	}
}
