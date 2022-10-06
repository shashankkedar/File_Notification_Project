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
/* 09-09-2022      Initial     Shashank Kedar(2259420)       Initial class where we added cron job scheduler

/********************************************************************************/

package com.tcs.telecom.tib.cfl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tcs.telecom.tib.cfl.entity.CustomerBankDetailsEntity;
import com.tcs.telecom.tib.cfl.helper.CustomerDetailsHelper;
import com.tcs.telecom.tib.cfl.soap.SOAP_Service;
import com.tcs.telecom.tib.cfl.util.CommanUtility;
import com.tcs.telecom.tib.cfl.util.FileCreationConstants;

@Component
@EnableAsync
public class SchedularService {
	private static final Logger log = LoggerFactory.getLogger(SchedularService.class);
	
	@Autowired
	AUDDIS_File_Creation auddisDataCreation;
	
	@Autowired
	DD_File_Creation ddDataCreation;
	
	@Autowired
	CustomerDetailsHelper helper;
	
	@Autowired
	CommanUtility utility;
	
	@Autowired
	SOAP_Service soapService;
	
	   @Scheduled(fixedRate = 60000)
	  // @Scheduled(cron = "0 0 21 * * MON-FRI",zone = "BST")
	   //@Scheduled(cron = "0 0/15 7-19 * * MON-FRI",zone = "BST")
	   @Async
	   public void auddis_cronJobSch() {
		  
		  List<CustomerBankDetailsEntity> custPaymentDetailLst= helper.getInProgressCustomer(FileCreationConstants.AUDDIS);
		  log.info("For AUDDIS, In Progress Data List :: " + custPaymentDetailLst.size());
		  if(custPaymentDetailLst.size()>0) 
		  {
				try {
				  helper.insertDataInSeqTable(); // This call for to generate auto incremented sequence number
				  
				  List<Map<String, Object>>hldLst = helper.getHolidaysListfromDB();
				  String bacsProcessDate = utility.processingDateCalculation(hldLst);
				  
			      String sampleFileData = auddisDataCreation.fileDataCreation(custPaymentDetailLst,bacsProcessDate);   // actual data replacement logic here
			      String validationStr = utility.fileValidation(sampleFileData);     // validate file each line length
			      
			      log.info("AUDDIS file Validation :: " + validationStr );
					  if(!(validationStr.equalsIgnoreCase(FileCreationConstants.NOT_VALID))) 
					     {
					    	  Map<String, String> fileMap = utility.fileCreationTask(sampleFileData,FileCreationConstants.AUDDIS);   // if length wise file ok then file created at specified location
					    	  
					    	  //once file created on our own server then Move these files to HSBC Server
					    	  utility.transferFiles(fileMap);
					    	  log.info(" AUDDIS LEVEL :: Data fetching for SOAP started ::::::::::::::: " );
					    	  
					    	  List<Map<String, Object>> accountDetailsLst = helper.fetchAccountIdDetails(custPaymentDetailLst,FileCreationConstants.AUDDIS);
					    	  log.info("Account Id details ::"+accountDetailsLst);
					    	 
								List<HashMap<String, Object>> lstMap = utility.insertionInMasterTable(accountDetailsLst, fileMap);
								log.info("AUDDIS FILE Process Ended here::::::::::::::: " );	
						    	  
					      }else {
					    	  log.info("AUDDIS File failed in Length validation ...");
					      }
					  
				} catch (Exception e) {
					 e.printStackTrace();
				     log.error("Error occurred in AUDDIS process :" , e);
				}
			}
	   }

	   
	  // @Scheduled(fixedRate = 90000)
	     @Scheduled(cron = "0 0 22 * * MON-FRI",zone = "BST")
	  // @Scheduled(cron = "0 0/30 7-19 * * MON-FRI",zone = "BST")
	   @Async
	   public void dd_cronJobSch() {
		  
		  List<CustomerBankDetailsEntity> custPaymentDetailLst= helper.getInProgressCustomer(FileCreationConstants.DD);
		  log.info("For DD, In Progress Data List :: " + custPaymentDetailLst);
		  
		  if(custPaymentDetailLst.size()>0) 
		   {
		     try {
					  helper.insertDataInSeqTable(); // This call for to generate auto incremented sequence number
					  
					  List<Map<String, Object>>hldLst = helper.getHolidaysListfromDB();
					  String bacsProcessDate = utility.processingDateCalculation(hldLst);
					  
				      String sampleFileData = ddDataCreation.fileDataCreation(custPaymentDetailLst,bacsProcessDate);  // actual data replacement logic here
				      String validationStr = utility.fileValidation(sampleFileData);    // validate file each line length
				     
				      log.info("DD FILE Validation :: " + validationStr );
					      if(!(validationStr.equalsIgnoreCase(FileCreationConstants.NOT_VALID))) 
					      {
					    	  Map<String, String> fileMap = utility.fileCreationTask(sampleFileData,FileCreationConstants.DD);       // if length wise file ok then file created at specified location
					    	  
					    	  //once file created on our own server then Move these files to HSBC Server
					    	  utility.transferFiles(fileMap);
					    	  
					    	  log.info(" DD LEVEL ::Data fetching for SOAP started ::::::::::::::: " );
					    	  
					    	  List<Map<String, Object>> accountDetailsLst = helper.fetchAccountIdDetails(custPaymentDetailLst,FileCreationConstants.DD);
					    	  log.info("Account ID details ::" + accountDetailsLst);
					    	  
					    	  List<HashMap<String, Object>> lstMap = utility.insertionInMasterTable(accountDetailsLst, fileMap);
					    	  log.info("For SOAP Preparation map  ::" + lstMap);
					    	  soapService.preparationForSOAPService(lstMap);
					    	  
					    	  log.info("DD FILE Process Ended here::::::::::::::: " );	    	  
					      }else {
					    	  log.info("DD File failed in Length validation ...");
					      }
				}catch (Exception e) {
				     e.printStackTrace();
				     log.error("Error occurred in DD process :" , e);
			     }    
			      
		   }
	}
	
}