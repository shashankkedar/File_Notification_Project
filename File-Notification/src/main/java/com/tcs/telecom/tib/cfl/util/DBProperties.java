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
/* 27-09-2022      Initial     Shashank Kedar(2259420)       External properties to be Load 

/********************************************************************************/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBProperties {
	private static final Logger log = LoggerFactory.getLogger(DBProperties.class);
	
	@Value("${spring.datasource.username}")
	private String userName;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${driver_class}")
	private String driverCls;
	
	@Value("${db.schema.name}")
	private String schemaName;

	public DBProperties() {
		super();
	}
	

	public DBProperties(String userName, String password, String url, String driverCls, String schemaName) {
		super();
		this.userName = userName;
		this.password = password;
		this.url = url;
		this.driverCls = driverCls;
		this.schemaName = schemaName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverCls() {
		return driverCls;
	}

	public void setDriverCls(String driverCls) {
		this.driverCls = driverCls;
	}


	public String getSchemaName() {
		return schemaName;
	}


	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}


	@Override
	public String toString() {
		return "DBProperties [userName=" + userName + ", password=" + password + ", url=" + url + ", driverCls="
				+ driverCls + ", schemaName=" + schemaName + "]";
	}

}