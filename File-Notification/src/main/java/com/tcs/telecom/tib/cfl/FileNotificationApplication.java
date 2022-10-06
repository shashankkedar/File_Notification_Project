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
/* 09-09-2022      Initial     Shashank Kedar(2259420)       Main class for File creation

/********************************************************************************/

package com.tcs.telecom.tib.cfl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
@EnableAutoConfiguration
public class FileNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileNotificationApplication.class, args);
	}

}
