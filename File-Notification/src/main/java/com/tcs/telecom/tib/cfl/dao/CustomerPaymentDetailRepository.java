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
/* 15-09-2022      Initial     Shashank Kedar(2259420)        define all the DAO layer methods  

/********************************************************************************/
package com.tcs.telecom.tib.cfl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcs.telecom.tib.cfl.entity.CustomerBankDetailsEntity;

public interface CustomerPaymentDetailRepository {
	List<CustomerBankDetailsEntity> findAll(String fileNature);
	
	int saveSeqNumber();
	
	List<Map<String, Object>>findHolidays();
	
	List<Map<String, Object>> findByAccountId(List<String> accountIDList,String fileNature);
	
	HashMap<String, Object> saveResponseInMasterTbl(HashMap<String, Object>responseMap);
	
	HashMap<String, Object> saveTxnRecord(HashMap<String, Object> singleMap);
}
