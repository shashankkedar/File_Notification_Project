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
/* 09-09-2022      Initial     Shashank Kedar(2259420)       these is helper class from here we call Repository class methods 

/********************************************************************************/

package com.tcs.telecom.tib.cfl.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.tcs.telecom.tib.cfl.entity.CustomerBankDetailsEntity;
import com.tcs.telecom.tib.cfl.repository.daoImpl.JDBCRepository;
import com.tcs.telecom.tib.cfl.util.FileCreationConstants;

@Service
public class CustomerDetailsHelper {
	private static final Logger log = LoggerFactory.getLogger(CustomerDetailsHelper.class);
	@Autowired
	Environment env;
	@Autowired
	JDBCRepository jdbcRepository;

	public List<CustomerBankDetailsEntity> getInProgressCustomer(String fileNature) {
		List<CustomerBankDetailsEntity> custDetailLst = new ArrayList<CustomerBankDetailsEntity>();
		try {

			custDetailLst = jdbcRepository.findAll(env.getProperty(FileCreationConstants.STATUS_LST),fileNature);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return custDetailLst;
	}
	
	public int insertDataInSeqTable() throws Exception {
		int insertCount=0;
		try {

			insertCount = jdbcRepository.saveSeqNumber();
			log.info("Sequence number record inserted count  :: "+ insertCount);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(e);
		}
		return insertCount;
	}
	
	public List<Map<String, Object>> getHolidaysListfromDB() throws Exception{
		List<Map<String, Object>> hldLst  = new ArrayList<>();
		try {
			hldLst = jdbcRepository.findHolidays();
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(e);
		}
		
		return hldLst;
		
	}
	
	// getting details using ACCOUNT_ID for SOAP service call
	public List<Map<String, Object>> fetchAccountIdDetails(List<CustomerBankDetailsEntity> custPaymentDetailLst,String fileNature) throws Exception {
		List<Map<String, Object>> accountDetailsLst  = null;
		List<String> accountIDList = new ArrayList<String>();
		try {
			for (CustomerBankDetailsEntity entityObj : custPaymentDetailLst) {
				accountIDList.add(entityObj.getAccountId());
			}
			accountDetailsLst = jdbcRepository.findByAccountId(accountIDList,fileNature);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(e);
		}
		return accountDetailsLst;
	}
	
	public HashMap<String, Object> insertInMasterTbl(HashMap<String, Object>responseMap) {
		log.info("Inside insertInMasterTbl started.." + responseMap);
		responseMap  = jdbcRepository.saveResponseInMasterTbl(responseMap);
		log.info("Inside insertInMasterTbl ended..");
		
		return responseMap;
	}

}
