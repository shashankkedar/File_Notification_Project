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
/* 27-09-2022      Initial     Shashank Kedar(2259420)       Load external properties 

/********************************************************************************/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class AppConfig {
	private static final Logger log = LoggerFactory.getLogger(AppConfig.class);
	
	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
	    PropertySourcesPlaceholderConfigurer properties = 
	      new PropertySourcesPlaceholderConfigurer();
	    properties.setLocation(new FileSystemResource("/app/server/HOBS-MicroServices/configuration/com/tcs/telecom/tib/rms/cflfilenotification/cflfilenotification-application.properties"));
	    properties.setIgnoreResourceNotFound(false);
	    return properties;
	}

}
