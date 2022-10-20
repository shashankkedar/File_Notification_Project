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
/* 09-08-2022      Initial     Shashank Kedar(2259420)       Constant Values added here

/********************************************************************************/

package com.tcs.telecom.tib.cfl.util;

public final class FileCreationConstants {

	public static final String AUDDIS_FILE_PATH = "AUDDIS_FILE_PATH";
	public static final String AUDDIS_FILE_NAME="AUDDIS_FILE_NAME";
	public static final String DD_FILE_PATH = "DD_FILE_PATH";
	public static final String DD_FILE_NAME="DD_FILE_NAME";
	public static final String SOAP_XML_TEMPLATE_PATH ="/XML_Template/soap_xml_template.txt";
	public static final String AUDDIS_STATUS_LST="AUDDIS_STATUS_LST";
	public static final String DD_STATUS_LST="DD_STATUS_LST";
	public static final String JULIAN_DATE_FORMAT="JULIAN_DATE_FORMAT";
	public static final String AUDDIS_TEMPLATE_FILE_NAME="TEMPLATE_CFL_AUDDIS.txt";
	public static final String DD_TEMPLATE_FILE_NAME="TEMPLATE_CFL_DD.txt";
	public static final String INPUT_DATE="yyyy-MM-dd";
	public static final String FILE_NAME_DATE="dd-MM-yyyy_HHmmss";
	public static final String SERVICE_USER_NUMBER= "SERVICE_USER_NUMBER";
	public static final String PROCESSING_DAY_VALUE= "PROCESSING_DAY_VALUE";
	public static final String NOT_VALID="NOT_VALID";
	public static final String VALID="VALID";
	public static final String DD="DD";
	public static final String AUDDIS="AUDDIS";
	public static final String REMOVAL_PART = "(?m)^[ \\t]*\\r?\\n";
	public static final String SINGLE_DAY_VALUE = "00100";
	public static final String MULTI_DAY_VALUE = "00106";
	public static final String SingleDay = "SingleDay";
	public static final String MultiDay = "MultiDay";
	public static final String DAILY="DAILY";
	public static final String MULTI="MULTI";
	public static final String TRXN_CD_FLAG="TRXN_CD_FLAG";
	public static final String NEW_FILE="0N";
	public static final String CANCEL_FILE="0C";
	public static final String ORIGINATING_SORT_CODE="ORIGINATING_SORT_CODE";
	public static final String ORIGINATING_ACC_NUM="ORIGINATING_ACC_NUM";
	public static final String FETCH_IN_PROGRESS_TXN_FOR_AUDDIS="FETCH_IN_PROGRESS_TXN_FOR_AUDDIS";
	public static final String FETCH_IN_PROGRESS_TXN_FOR_DD="FETCH_IN_PROGRESS_TXN_FOR_DD";
	public static final String INSERT_SEQ_NO="INSERT_SEQ_NO";
	public static final String GET_HOLIDAYS_LIST="GET_HOLIDAYS_LIST";
	public static final String FETCH_ACCOUNT_ID_DETAILS_AUDDIS="FETCH_ACCOUNT_ID_DETAILS_AUDDIS";
	public static final String FETCH_ACCOUNT_ID_DETAILS_DD="FETCH_ACCOUNT_ID_DETAILS_DD";
	public static final String INSERT_FILE_NOTIFY_MST="INSERT_FILE_NOTIFY_MST";
	public static final String INSERT_FILE_TXN_INFO="INSERT_FILE_TXN_INFO"; 
	public static final String UPDATE_MST_TBL="UPDATE_MST_TBL"; 
	public static final String UPDATE_CUST_STATUS="UPDATE_CUST_STATUS";
	public static final String RHM_OPR_ID="RHM_OPR_ID";
	
	//HSBC SERVER DETAILS
	public static final String HSBC_HOST = "HSBC_HOST";
	public static final String HSBC_PORT = "HSBC_PORT";
	public static final String HSBC_USER_NAME = "HSBC_USER_NAME";
	public static final String HSBC_PWD = "HSBC_PWD";
	public static final String HSBC_CHANNEL = "HSBC_CHANNEL";
	
	//MST TABLE INSERTION CONSTANTS START
	public static final String ACCOUNT_ID="ACCOUNT_ID";
	public static final String op_id="OP_ID";
	public static final String is_billable="IS_BILLABLE";
	public static final String SUBSCRIBER_ID="SUBSCRIBER_ID";
	public static final String acc_cur="ACC_CUR";
	public static final String pay_by_date="PAY_BY_DATE";
	public static final String invoice_num="INVOICE_NUM";
	public static final String bill_due="BILL_DUE";
	public static final String customer_id="CUSTOMER_ID";
	public static final String bu_id="BU_ID";
	public static final String INVOICE_SEQ_NBR="INVOICE_SEQ_NBR";
	public static final String FILE_TYPE="FILE_TYPE";
	public static final String FILE_NAME="FILE_NAME";
	public static final String FILE_DATA="FILE_DATA";
	public static final String Response_Status="Response_Status";
	public static final String Response="Response";
	public static final String Response_Code="Response_Code";
	public static final String STATUS_CODE="STATUS_CODE";
	public static final String STATUS_TYPE="STATUS_TYPE";
	public static final String STATUS_DESC="STATUS_DESC";
	
	public static final String BATCH="BATCH";
	public static final String HOBS="HOBS";
	public static final String UpdatedDateTime="UpdatedDateTime";
	public static final String REQ_DATE_TIME="REQ_DATE_TIME";
	public static final String FILE_STATUS="FILE_STATUS";
	public static final String HOBS_STATUS="HOBS_STATUS";
	public static final String FAILED="FAILED";
	public static final String SUCCESS="SUCCESS";
	public static final String SUBMIT="SUBMITTED";
	public static final String OK="OK";
	public static final String INTERACTION_ID="INTERACTION_ID";
	//MST TABLE INSERTION CONSTANTS END
	
	//SOAP XML REPLACEMENT CONSTANT
	public static final String XML_INTERACTION_ID="#INTERACTION_ID#";
	public static final String XML_PAY_BY_DATE="#PAY_BY_DATE#";
	public static final String XML_ACCOUNT_ID="#ACCOUNT_ID#";
	public static final String XML_CUSTOMER_ID="#CUSTOMER_ID#";
	public static final String XML_SUBSCRIBER_ID="#SUBSCRIBER_ID#";
	public static final String XML_PARTY_TYPE="#PARTY_TYPE#";
	public static final String XML_ACCOUNT_CURRENCY="#ACCOUNT_CURRENCY#";
	public static final String XML_ACCOUNT_AMOUNT="#ACCOUNT_AMOUNT#";
	public static final String XML_INVOICE_NUMBER="#INVOICE_NUMBER#";
	public static final String XML_PARTY_TYPE_VALUE="Individual";
	public static final String XML_PAY_BY_DATE_VALUE="PAY_BY_DATE";
	public static final String XML_CUSTOMER_ID_VALUE="CUSTOMER_ID";
	public static final String XML_ACC_CUR_VALUE="ACC_CUR";
	public static final String XML_BILL_DUE_VALUE="BILL_DUE";
	public static final String XML_INVOICE_NUM_VALUE="INVOICE_NUM";
	public static final String XML_OP_ID="#OP_ID#";
	public static final String PAY_BY_DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss";
			
			
	//SOAP SERVICE 3RD PARTY URL		
			
	public static final String SOAP_URL = "SOAP_URL";
	public static final String SOAP_METHOD_TYPE ="POST";
	public static final String CONTENT_TYPE="Content-Type";
	public static final String CONTENT_TYPE_VALUE="application/xml";
	public static final int SOAP_SUCCESS_RESP_CODE=200;
	public static final String RESPONSE_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	// File Replacement Constant START
	public static final String VOL_1 ="{VOL_1}";
	public static final String VOL_2 ="{VOL_2}";
	public static final String VOL_3_SN = "{VOL_3_SN}";
	public static final String VOL_LAST ="{VOL_LAST}";
	
	public static final String HDR1_1 = "{HDR1_1}";
	public static final String HDR1_2 = "{HDR1_2}";
	public static final String HDR1_3 = "{HDR1_3}";
	public static final String HDR1_4_SUN = "{HDR1_4_SUN}";
	public static final String HDR1_5_S = "{HDR1_5_S}";
	public static final String HDR1_6_1 = "{HDR1_6_1}";
	public static final String HDR1_7_SUN = "{HDR1_7_SUN}";	
	public static final String HDR1_8_SN = "{HDR1_8_SN}";
	public static final String HDR1_9_CD = "{HDR1_9_CD}";
	public static final String HDR1_10_ED = "{HDR1_10_ED}";
	public static final String HDR1_11= "{HDR1_11}";
	
	public static final String HDR2_1 = "{HDR2_1}";
	public static final String HDR2_2 = "{HDR2_2}";
	public static final String HDR2_3 = "{HDR2_3}";
	public static final String HDR2_4 = "{HDR2_4}";
	public static final String HDR2_5 = "{HDR2_5}";
	public static final String HDR2_6 = "{HDR2_6}";
	
	public static final String UHL1_1 = "{UHL1_1}";
	public static final String UHL1_2 = "{UHL1_2}";
	public static final String UHL1_3_PD = "{UHL1_3_PD}";
	public static final String UHL1_4_REC_PRTY_NO = "{UHL1_4_REC_PRTY_NO}"; 
	public static final String UHL1_5_CCY_CD = "{UHL1_5_CCY_CD}";
	public static final String UHL1_6_WRKCD_ID = "{UHL1_6_WRKCD_ID}";
	public static final String UHL1_7_WRKCD_DESC = "{UHL1_7_WRKCD_DESC}";
	public static final String UHL1_8_FILE_NO = "{UHL1_8_FILE_NO}";
	public static final String UHL1_9_AUD_ID = "{UHL1_9_AUD_ID}";
	
	public static final String EOF1_1 = "{EOF1_1}";
	public static final String EOF1_2 = "{EOF1_2}";
	public static final String EOF2_1 = "{EOF2_1}";
	public static final String EOF2_2 = "{EOF2_2}";

	public static final String UTL1_1 = "{UTL1_1}";
	public static final String UTL1_2 = "{UTL1_2}";
	
	public static final String STD_REC_1 = "111111";
	public static final String STD_REC_2 = "22222222";
	public static final String PADDING_2_DIGIT = "%2s";
	public static final String PADDING_4_DIGIT = "%4s";
	public static final String PADDING_6_DIGIT = "%6s";
	public static final String PADDING_8_DIGIT = "%8s";
	public static final String PADDING_11_DIGIT = "%11s";
	public static final String PADDING_12_DIGIT = "%12s";
	public static final String PADDING_18_DIGIT = "%18s";
	public static final String SUN = "{SUN}";
	public static final String STD_RECORD ="{Standard_Records}";
	
	public static final String TransactionCode="{TxnCode}";
	public static final String OrigSrtCode="{OrigSrtCode}";
	public static final String OrigAccNum="{OrigAccNum}";
	public static final String FreeFormat="{FreeFormat}";
	public static final String Amount="{Amount}";
	public static final String userName="{userName}";
	public static final String userRefName="{userRefName}";
	public static final String DestAccountName="{DestAccountName}";
	public static final String ProcessDayData="{ProcessDayData}";
	
	public static final String CONTRA_REC_1="{CONTRA_REC_1}";
	public static final String CONTRA_REC_2="{CONTRA_REC_2}";
	public static final String CONTRA_TransactionCode="{CONTRA_TransactionCode}";
	public static final String CONTRA_OrigSrtCode="{CONTRA_OrigSrtCode}";
	public static final String CONTRA_OrigAccNum="{CONTRA_OrigAccNum}";
	public static final String CONTRA_FreeFormat="{CONTRA_FreeFormat}";
	public static final String CONTRA_Amount="{CONTRA_Amount}";
	public static final String CONTRA_Narrative_User="{CONTRA_Narrative_User}";
	public static final String CONTRA_IDENTIFY="{CONTRA_IDENTIFY}";
	public static final String CONTRA_User_Account_Name="{CONTRA_User_Account_Name}";
	public static final String CONRA_PD="{CONRA_PD}";
	
	
	// Replacement Constant END

}
