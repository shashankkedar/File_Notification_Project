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
/* 22-09-2022      Initial     Shashank Kedar(2259420)        these is repository class where we added all DB methods

/********************************************************************************/
package com.tcs.telecom.tib.cfl.repository.daoImpl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tcs.telecom.tib.cfl.dao.CustomerPaymentDetailRepository;
import com.tcs.telecom.tib.cfl.entity.BankDetailsRowMapperForA;
import com.tcs.telecom.tib.cfl.entity.BankDetailsRowMapperForP;
import com.tcs.telecom.tib.cfl.entity.CustomerBankDetailsEntity;
import com.tcs.telecom.tib.cfl.util.FileCreationConstants;

@Repository
public class JDBCRepository implements CustomerPaymentDetailRepository {
	private static final Logger log = LoggerFactory.getLogger(JDBCRepository.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Environment environment;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<CustomerBankDetailsEntity> findAll(String fileNature) {
		log.info("inside findAll method  :: " );
		List<CustomerBankDetailsEntity> objLst = new ArrayList<CustomerBankDetailsEntity>();
		try {
			String sqlQry = "";
			if(fileNature.equalsIgnoreCase(FileCreationConstants.AUDDIS)) {
				 String aStatus = environment.getProperty(FileCreationConstants.AUDDIS_STATUS_LST);
			     sqlQry = environment.getProperty(FileCreationConstants.FETCH_IN_PROGRESS_TXN_FOR_AUDDIS);
			     objLst = jdbcTemplate.query(String.format(sqlQry,aStatus), new BankDetailsRowMapperForA());
			}else if(fileNature.equalsIgnoreCase(FileCreationConstants.DD)) {
				String pStatus = environment.getProperty(FileCreationConstants.DD_STATUS_LST);
				 sqlQry = environment.getProperty(FileCreationConstants.FETCH_IN_PROGRESS_TXN_FOR_DD);
				 objLst = jdbcTemplate.query(sqlQry,new Object[]{pStatus}, new BankDetailsRowMapperForP());
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.info("inside findAll method ended :: " );
		return objLst;
	}

	@Override
	public int saveSeqNumber() {
		log.info("in saveSeqNumber method  :: " );
		int successCount = 0;
		try {
			String insertQry= environment.getProperty(FileCreationConstants.INSERT_SEQ_NO);
			successCount = jdbcTemplate.update(insertQry,new Object[]{"DUMMY"});
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return successCount;
	}

	public List<Map<String, Object>> findHolidays() {
		log.info("inside findHolidays method");
		List<Map<String, Object>> hldLst = new ArrayList<>();
		
		String fetchHolidays = environment.getProperty(FileCreationConstants.GET_HOLIDAYS_LIST); 
		
		hldLst = jdbcTemplate.queryForList(fetchHolidays);
		
		log.info("findHolidays method ended ..Holiday list :: " + hldLst);
		return hldLst;
	}

	public 	List<Map<String, Object>>  findByAccountId(List<String> accountIDList,String fileNature) {
		log.info(" findByAccountId method started..");
		String sb ="'";
		for (String accountId : accountIDList) {
			sb = sb+accountId +"','";
		}
		sb = sb.replaceAll(",'$", ""); // remove last ' and comma
		log.info("Account number of in query :: " +sb);
		
		String accountDetailsQry ="";
		if(fileNature.equalsIgnoreCase(FileCreationConstants.AUDDIS)) {
			accountDetailsQry = environment.getProperty(FileCreationConstants.FETCH_ACCOUNT_ID_DETAILS_AUDDIS);
			
			// once the AUDDIS files created on HSBC server then change cpm.stauts ='IN-PROGRESS/PENDING' to 'ACTIVE'
			updateCustomerStatus(sb);
			
		}else if(fileNature.equalsIgnoreCase(FileCreationConstants.DD)){
			accountDetailsQry = environment.getProperty(FileCreationConstants.FETCH_ACCOUNT_ID_DETAILS_DD);
		}
		
		log.info("Query 11:: " + accountDetailsQry);

		List<Map<String, Object>> accountDetailsLst  = jdbcTemplate.queryForList(String.format(accountDetailsQry,sb));
		
		log.info(" findByAccountId4 method ended.."+accountDetailsLst);
		
		return accountDetailsLst;
	}
	
	public HashMap<String, Object> saveResponseInMasterTbl(HashMap<String, Object>responseMap) {
		log.info(" saveResponseInMasterTbl method started..");
		String insertQry = environment.getProperty(FileCreationConstants.INSERT_FILE_NOTIFY_MST);
		
		if(null != responseMap && !(responseMap.isEmpty())) {
		
		//String INTERACTION_ID = (responseMap.containsKey("INTERACTION_ID") && null != responseMap.get("INTERACTION_ID"))?responseMap.get("INTERACTION_ID").toString():null;
		String INTERACTION_ID= String.valueOf(UUID.randomUUID());
		String ACCOUNT_ID = (responseMap.containsKey(FileCreationConstants.ACCOUNT_ID) && null != responseMap.get(FileCreationConstants.ACCOUNT_ID))?responseMap.get(FileCreationConstants.ACCOUNT_ID).toString():null;
		String op_id = (responseMap.containsKey(FileCreationConstants.op_id) && null != responseMap.get(FileCreationConstants.op_id))?responseMap.get(FileCreationConstants.op_id).toString():null;
		String is_billable = (responseMap.containsKey(FileCreationConstants.is_billable) && null != responseMap.get(FileCreationConstants.is_billable))?responseMap.get(FileCreationConstants.is_billable).toString():null;
		String SUBSCRIBER_ID = (responseMap.containsKey(FileCreationConstants.SUBSCRIBER_ID) && null != responseMap.get(FileCreationConstants.SUBSCRIBER_ID))?responseMap.get(FileCreationConstants.SUBSCRIBER_ID).toString():null;
		String acc_cur = (responseMap.containsKey(FileCreationConstants.acc_cur) && null != responseMap.get(FileCreationConstants.acc_cur))?responseMap.get(FileCreationConstants.acc_cur).toString():null;
		String pay_by_date = (responseMap.containsKey(FileCreationConstants.pay_by_date) && null != responseMap.get(FileCreationConstants.pay_by_date))?responseMap.get(FileCreationConstants.pay_by_date).toString():null;
		String invoice_num = (responseMap.containsKey(FileCreationConstants.invoice_num) && null != responseMap.get(FileCreationConstants.invoice_num))?responseMap.get(FileCreationConstants.invoice_num).toString():null;
		String bill_due = (responseMap.containsKey(FileCreationConstants.bill_due) && null != responseMap.get(FileCreationConstants.bill_due))?responseMap.get(FileCreationConstants.bill_due).toString():null;
		String customer_id = (responseMap.containsKey(FileCreationConstants.customer_id) && null != responseMap.get(FileCreationConstants.customer_id))?responseMap.get(FileCreationConstants.customer_id).toString():null;
		String bu_id = (responseMap.containsKey(FileCreationConstants.bu_id) && null != responseMap.get(FileCreationConstants.bu_id))?responseMap.get(FileCreationConstants.bu_id).toString():null;
		String INVOICE_SEQ_NBR = (responseMap.containsKey(FileCreationConstants.INVOICE_SEQ_NBR) && null != responseMap.get(FileCreationConstants.INVOICE_SEQ_NBR))?responseMap.get(FileCreationConstants.INVOICE_SEQ_NBR).toString():null;
		String FILE_TYPE = (responseMap.containsKey(FileCreationConstants.FILE_TYPE) && null != responseMap.get(FileCreationConstants.FILE_TYPE))?responseMap.get(FileCreationConstants.FILE_TYPE).toString():null;
		String FILE_NAME = (responseMap.containsKey(FileCreationConstants.FILE_NAME) && null != responseMap.get(FileCreationConstants.FILE_NAME))?responseMap.get(FileCreationConstants.FILE_NAME).toString():null;
		
		String Response_Status = (responseMap.containsKey(FileCreationConstants.Response_Status) && null != responseMap.get(FileCreationConstants.Response_Status))?responseMap.get(FileCreationConstants.Response_Status).toString():null;
		String Response = (responseMap.containsKey(FileCreationConstants.Response) && null != responseMap.get(FileCreationConstants.Response))?responseMap.get(FileCreationConstants.Response).toString():null;
		String Response_Code = (responseMap.containsKey(FileCreationConstants.Response_Code) && null != responseMap.get(FileCreationConstants.Response_Code))?responseMap.get(FileCreationConstants.Response_Code).toString():null;
		String createdBy=FileCreationConstants.BATCH;
		String updatedBy=FileCreationConstants.HOBS;
		String updatedDateTime=(responseMap.containsKey(FileCreationConstants.UpdatedDateTime) && null != responseMap.get(FileCreationConstants.UpdatedDateTime))?responseMap.get(FileCreationConstants.UpdatedDateTime).toString():null;
		String reqDateTime=(responseMap.containsKey(FileCreationConstants.REQ_DATE_TIME) && null != responseMap.get(FileCreationConstants.REQ_DATE_TIME))?responseMap.get(FileCreationConstants.REQ_DATE_TIME).toString():null;
		
		//HSBC_STATUS depends on file created on SFTP or not  
		String HSBC_STATUS=(responseMap.containsKey(FileCreationConstants.FILE_STATUS) && null != responseMap.get(FileCreationConstants.FILE_STATUS))? responseMap.get(FileCreationConstants.FILE_STATUS).toString():"";
		
		// New changes added from here
		String pInteractionId = (responseMap.containsKey("P_INTERACTION_ID") && null != responseMap.get("P_INTERACTION_ID"))? responseMap.get("P_INTERACTION_ID").toString():null;
		
		Object[] params = new Object[] {INTERACTION_ID, ACCOUNT_ID, op_id, is_billable, SUBSCRIBER_ID, acc_cur, pay_by_date,invoice_num, bill_due, customer_id,bu_id, INVOICE_SEQ_NBR, Response,Response_Code,Response_Status,FILE_TYPE,FILE_NAME,
										createdBy,updatedBy,updatedDateTime,reqDateTime,HSBC_STATUS,pInteractionId};
		
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
        		Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.BIGINT,Types.LONGVARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR
        		,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR};
        
        int row = jdbcTemplate.update(insertQry, params, types);
        
        log.info(" saveResponseInMasterTbl method ended with row count.." +row);
        responseMap.put(FileCreationConstants.INTERACTION_ID, INTERACTION_ID);
        responseMap.put("PARENT_INTERACTION_ID", pInteractionId);
		}
 
		return responseMap;
		 
	}

	public void updateMstTbl(String request,String Response_Status,String responseCode,String response,String responseDate,String hobs_Status,String hsbc_status, String actualStatusDesc, String interactionID) throws Exception {
		log.info(" updateMstTbl method started..");
		String updateQuery = environment.getProperty(FileCreationConstants.UPDATE_MST_TBL);
		
		Object[] params = new Object[] {request, Response_Status,responseCode,response,responseDate,hobs_Status,hsbc_status,actualStatusDesc, interactionID};

		int[] types = new int[] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,Types.LONGVARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR};

		int updatedCnt = jdbcTemplate.update(updateQuery,params,types);
		
		log.info(" updateMstTbl method ended with updated row count.." +updatedCnt);
		
	}
/* unused method
	public void updateParentRecord(String dd_File_Status, String pInteractionID) {
		log.info(" updateParentRecord method started..");
		String updateQuery = "UPDATE rms_billing.FILE_NOTIFICATION_MST SET DD_FILE_STATUS =? WHERE INTERACTION_ID=?";
		
		Object[] params = new Object[] {dd_File_Status,pInteractionID};

		int[] types = new int[] {Types.VARCHAR, Types.VARCHAR};

		int updatedCnt = jdbcTemplate.update(updateQuery,params,types);
		
		log.info(" updateParentRecord method ended with updated row count.." +updatedCnt);
		
	}

*/
	
	public void updateCustomerStatus(String partyId) {
		log.info(" updateCustomerStatus method started.."+partyId);
		String updateQuery = environment.getProperty(FileCreationConstants.UPDATE_CUST_STATUS);
		
		int updatedCnt = jdbcTemplate.update(String.format(updateQuery,partyId));
		
		log.info(" updateCustomerStatus method ended with updated row count.." +updatedCnt);
		
	}

	public HashMap<String, Object> saveTxnRecord(HashMap<String, Object> singleMap) {
		log.info(" saveTxnRecord method started..");
			String insertQry = environment.getProperty(FileCreationConstants.INSERT_FILE_TXN_INFO);
			
			if(null != singleMap && !(singleMap.isEmpty())) {
			
			String INTERACTION_ID = (singleMap.containsKey("INTERACTION_ID") && null != singleMap.get("INTERACTION_ID"))?singleMap.get("INTERACTION_ID").toString():null;
			String ACCOUNT_ID = (singleMap.containsKey(FileCreationConstants.ACCOUNT_ID) && null != singleMap.get(FileCreationConstants.ACCOUNT_ID))?singleMap.get(FileCreationConstants.ACCOUNT_ID).toString():null;
			String invoice_num = (singleMap.containsKey(FileCreationConstants.invoice_num) && null != singleMap.get(FileCreationConstants.invoice_num))?singleMap.get(FileCreationConstants.invoice_num).toString():null;
			String FILE_TYPE = (singleMap.containsKey(FileCreationConstants.FILE_TYPE) && null != singleMap.get(FileCreationConstants.FILE_TYPE))?singleMap.get(FileCreationConstants.FILE_TYPE).toString():null;
			String FILE_NAME = (singleMap.containsKey(FileCreationConstants.FILE_NAME) && null != singleMap.get(FileCreationConstants.FILE_NAME))?singleMap.get(FileCreationConstants.FILE_NAME).toString():null;
			String FILE_DATA = (singleMap.containsKey(FileCreationConstants.FILE_DATA) && null != singleMap.get(FileCreationConstants.FILE_DATA))?singleMap.get(FileCreationConstants.FILE_DATA).toString():null;
			String reqDateTime=(singleMap.containsKey(FileCreationConstants.REQ_DATE_TIME) && null != singleMap.get(FileCreationConstants.REQ_DATE_TIME))?singleMap.get(FileCreationConstants.REQ_DATE_TIME).toString():null;
			
			Object[] params = new Object[] {INTERACTION_ID, ACCOUNT_ID, invoice_num,FILE_TYPE,FILE_NAME,FILE_DATA,reqDateTime};
			
		    int[] types = new int[] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.LONGVARCHAR,Types.VARCHAR};
		    
		    int row = jdbcTemplate.update(insertQry, params, types);
		    
		    log.info(" saveTxnRecord method ended with row count.." +row);
		
		  }
			return singleMap;
			
     }
	
}
