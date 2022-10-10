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
/* 15-09-2022      Initial     Shashank Kedar(2259420)        query result map into Bank details mapping class  

/********************************************************************************/
package com.tcs.telecom.tib.cfl.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.tcs.telecom.tib.cfl.entity.CustomerBankDetailsEntity;
@Component
public class BankDetailsRowMapperForP implements RowMapper<CustomerBankDetailsEntity> {

	@Override
	public CustomerBankDetailsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		CustomerBankDetailsEntity md  = new CustomerBankDetailsEntity();
		md.setCustBankAccName(rs.getString("CUST_BANK_ACCOUNT_NAME"));
		md.setCustBankAccNo(rs.getString("CUST_BANK_ACCOUNT_NO"));
		md.setCustBankSrtCode(rs.getString("CUST_BANK_SORT_CODE"));
		md.setPartyId(rs.getString("PARTY_ID"));
		md.setStatus(rs.getString("STATUS"));
		md.setPayerIdentifier(rs.getString("PAYER_IDENTIFIER"));
		md.setSeqNum(rs.getString("CBD_SEQ_NUM"));
		md.setAccountId(rs.getString("ACCOUNT_ID"));
		md.setTotalAmountDue(rs.getString("TOT_AMT_DUE"));
		md.setFirstLastInd(rs.getString("FIRST_LAST_INV_IND"));
		
		md.setMstID(rs.getString("MST_ID"));
		md.setpInteractionID(rs.getString("INTERACTION_ID"));
		md.setFileType(rs.getString("FILE_TYPE"));
		md.setDdFileStatus(rs.getString("DD_FILE_STATUS"));
		
		return md;
	}

}
