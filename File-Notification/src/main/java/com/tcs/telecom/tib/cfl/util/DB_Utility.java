package com.tcs.telecom.tib.cfl.util;

/********************************************************************************/
/*Copyright@ 2022 TCS All rights reserved
 * 
 * ******************************************************************************/
/* Module Name   : File Creation
 * Description   : File generation for HSBC
 * Author        : Shashank Kedar(2259420)
 * ******************************************************************************/
/* Version Control Block
 * ~~~~~~~ ~~~~~~~ ~~~~~                                                        */
/* Date            Version      Author                        Description       */
/* =============================================================================*/
/* 22-09-2022      Initial     Shashank Kedar(2259420)        these is DB Utility class here we get DB connection object using external db properties 

/********************************************************************************/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class DB_Utility {
	private static final Logger log = LoggerFactory.getLogger(DB_Utility.class);
	
	private static String DB_DRIVER_CLASS;
	private static String DB_USERNAME;
	private static String DB_PASSWORD;
	private static String DB_URL;
	private static String DB_SCHEMA_NAME;

	private static Connection connection1 = null;
	private static Connection connection2 = null;
	private static Connection connection3 = null;

	public Connection getConnection1() {
		try (ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class,
				DBProperties.class);) {
			DBProperties dbProperties = context.getBean(DBProperties.class);
			log.info("DB proeprties.......");
			log.info(dbProperties.toString());

			try {
				DB_DRIVER_CLASS =dbProperties.getDriverCls();
				DB_USERNAME = dbProperties.getUserName();
				DB_PASSWORD =dbProperties.getPassword();
				DB_URL = dbProperties.getUrl();
				DB_SCHEMA_NAME= dbProperties.getSchemaName();
				
				Class.forName(DB_DRIVER_CLASS);
				Properties properties = new Properties();
				properties.put("user", DB_USERNAME);
				properties.put("password", DB_PASSWORD);

				connection1 = DriverManager.getConnection(DB_URL+DB_SCHEMA_NAME + "?" + "user=" + DB_USERNAME + "&" + "password=" + DB_PASSWORD);
				connection2 = DriverManager.getConnection(DB_URL+DB_SCHEMA_NAME, DB_USERNAME, DB_PASSWORD);
				connection3 = DriverManager.getConnection(DB_URL+DB_SCHEMA_NAME, properties);
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return connection1;
	}
}
