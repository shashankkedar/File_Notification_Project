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
/* 09-08-2022      Initial     Shashank Kedar(2259420)       Write business logic for File creation

/********************************************************************************/
package com.tcs.telecom.tib.cfl.service;


import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.tcs.telecom.tib.cfl.entity.CustomerBankDetailsEntity;
import com.tcs.telecom.tib.cfl.repository.daoImpl.DbOperImpl;
import com.tcs.telecom.tib.cfl.util.CommanUtility;
import com.tcs.telecom.tib.cfl.util.FileCreationConstants;

@Component
public class DD_File_Creation {
	private static final Logger log = LoggerFactory.getLogger(DD_File_Creation.class);
	@Autowired
	Environment environment;
	@Autowired
	CommanUtility util;
	@Autowired
	DbOperImpl daoOprImpl; 
	public String fileDataCreation(List<CustomerBankDetailsEntity> custPaymentDetailLst,String bacsProcessDate) 
	{
		log.info("fileDataCreation() started for DD's");
		String line = null;
			InputStream sampleFile = getClass().getClassLoader().getResourceAsStream(FileCreationConstants.DD_TEMPLATE_FILE_NAME);
			if (null == sampleFile) 
			  {
				log.error("DD file tempalte not found..");
			  } 
			else 
			    {
				    //Julian date conversion
				  String inputFormat = FileCreationConstants.INPUT_DATE;
				  LocalDate ld = LocalDate.now();
				  String creationDate = util.toJulian(ld.toString(),inputFormat,environment.getProperty(FileCreationConstants.JULIAN_DATE_FORMAT));
				  String expiryDate = util.toJulian(ld.plusYears(1).toString(),inputFormat,environment.getProperty(FileCreationConstants.JULIAN_DATE_FORMAT));
				  
				  Integer seqCount =  daoOprImpl.getSequenceNumber();
				  String  seqNumber = seqCount !=null?String.valueOf(seqCount):"1";
				  
				  String fileLine =  util.fileTemplateReCreation(sampleFile,custPaymentDetailLst);
				  line = ddFileCreationLogic(fileLine,creationDate,expiryDate,custPaymentDetailLst,seqNumber,bacsProcessDate);
				  
				  line = line.contains(FileCreationConstants.STD_RECORD)?line.replace(FileCreationConstants.STD_RECORD, ""):line;
				  line = line.replaceAll(FileCreationConstants.REMOVAL_PART, "");

			}
		log.info("fileDataCreation() ended for DD's");
		return line;
	}
		
	private String ddFileCreationLogic(String line,String creationDate,String expiryDate,List<CustomerBankDetailsEntity> custPaymentDetailLst,String seqNumber,String bacsProcessDate) {
		
		log.info("DD's file replacement logic started");
		BigInteger totalAmount = new BigInteger("00000000000");
		LocalDate ld = LocalDate.now();
		String inputFormat = FileCreationConstants.INPUT_DATE;
		int nextJDate = Integer.parseInt(util.toJulian(ld.plusDays(39).toString(), inputFormat,environment.getProperty(FileCreationConstants.JULIAN_DATE_FORMAT)));
		int prevJDate = Integer.parseInt(util.toJulian(ld.minusDays(1).toString(), inputFormat,environment.getProperty(FileCreationConstants.JULIAN_DATE_FORMAT)));
		
		String serviceUserNumber = (null != environment.getProperty(FileCreationConstants.SERVICE_USER_NUMBER) || !(environment.getProperty(FileCreationConstants.SERVICE_USER_NUMBER).isEmpty())) ? environment.getProperty(FileCreationConstants.SERVICE_USER_NUMBER) :"SAGE  " ;
		String processingDay = (null == environment.getProperty(FileCreationConstants.PROCESSING_DAY_VALUE) || (environment.getProperty(FileCreationConstants.PROCESSING_DAY_VALUE).isEmpty()))?"SingleDay":environment.getProperty(FileCreationConstants.PROCESSING_DAY_VALUE);
		
		String procDayValue = "";
				if(processingDay.equalsIgnoreCase(FileCreationConstants.SingleDay)) {
					procDayValue = FileCreationConstants.SINGLE_DAY_VALUE;
				}else if(processingDay.equalsIgnoreCase(FileCreationConstants.MultiDay)) {
					procDayValue = FileCreationConstants.MULTI_DAY_VALUE;
				}
				
		
		if(!(line.isEmpty())) 
		{
			//START- VOL line replacement logic
				line = line.contains(FileCreationConstants.VOL_1)?line.replace(FileCreationConstants.VOL_1, "VOL"):line;
			    line = line.contains(FileCreationConstants.VOL_2)?line.replace(FileCreationConstants.VOL_2, "1"):line;
			    line = line.contains(FileCreationConstants.VOL_3_SN)?line.replace(FileCreationConstants.VOL_3_SN, String.format(FileCreationConstants.PADDING_6_DIGIT, seqNumber).replace(' ', '0')):line;   // Fetching from 6 digit Seq nummber from DB
				line = line.contains(FileCreationConstants.VOL_LAST)?line.replace(FileCreationConstants.VOL_LAST, "1"):line;
				line = line.contains(FileCreationConstants.SUN)?line.replace(FileCreationConstants.SUN, serviceUserNumber):line;   //static value
				
			//END- VOL line replacement logic
			
			//START- HDR1 line replacement logic
				line = line.contains(FileCreationConstants.HDR1_1)?line.replace(FileCreationConstants.HDR1_1, "HDR"):line;
				line = line.contains(FileCreationConstants.HDR1_2)?line.replace(FileCreationConstants.HDR1_2, "1"):line;
				line = line.contains(FileCreationConstants.HDR1_3)?line.replace(FileCreationConstants.HDR1_3, "A"):line;
				line = line.contains(FileCreationConstants.HDR1_4_SUN)?line.replace(FileCreationConstants.HDR1_4_SUN, serviceUserNumber):line;   //static value
				line = line.contains(FileCreationConstants.HDR1_5_S)?line.replace(FileCreationConstants.HDR1_5_S, "S"):line;    //static value
				line = line.contains(FileCreationConstants.HDR1_6_1)?line.replace(FileCreationConstants.HDR1_6_1, "1"):line;     //static value
				line = line.contains(FileCreationConstants.HDR1_7_SUN)?line.replace(FileCreationConstants.HDR1_7_SUN, serviceUserNumber):line;   //static value
				line = line.contains(FileCreationConstants.HDR1_8_SN)?line.replace(FileCreationConstants.HDR1_8_SN, String.format(FileCreationConstants.PADDING_6_DIGIT, seqNumber).replace(' ', '0')):line;   // Fetching from 6 digit Seq nummber from DB
				line = line.contains(FileCreationConstants.HDR1_9_CD)?line.replace(FileCreationConstants.HDR1_9_CD, " "+creationDate):line;    //creation date in julian format
				line = line.contains(FileCreationConstants.HDR1_10_ED)?line.replace(FileCreationConstants.HDR1_10_ED, " "+expiryDate):line;    // expiry date in julian format
				line = line.contains(FileCreationConstants.HDR1_11)?line.replace(FileCreationConstants.HDR1_11, "000000"):line;    //static value
			//END- HDR1 line replacement logic
				
			//START- HDR2 line replacement logic
				line = line.contains(FileCreationConstants.HDR2_1)?line.replace(FileCreationConstants.HDR2_1, "HDR"):line;
				line = line.contains(FileCreationConstants.HDR2_2)?line.replace(FileCreationConstants.HDR2_2, "2"):line;
				line = line.contains(FileCreationConstants.HDR2_3)?line.replace(FileCreationConstants.HDR2_3, "F"):line;
				line = line.contains(FileCreationConstants.HDR2_4)?line.replace(FileCreationConstants.HDR2_4, "02000"):line;
				line = line.contains(FileCreationConstants.HDR2_5)?line.replace(FileCreationConstants.HDR2_5, procDayValue):line;  // Single Day Or Multiple day, static value
				line = line.contains(FileCreationConstants.HDR2_6)?line.replace(FileCreationConstants.HDR2_6, "00"):line;
			//END- HDR2 line replacement logic
				
			//START- UHL1 line replacement logic
				line = line.contains(FileCreationConstants.UHL1_1)?line.replace(FileCreationConstants.UHL1_1, "UHL"):line;
				line = line.contains(FileCreationConstants.UHL1_2)?line.replace(FileCreationConstants.UHL1_2, "1"):line;
				line = line.contains(FileCreationConstants.UHL1_3_PD)?line.replace(FileCreationConstants.UHL1_3_PD, bacsProcessDate):line;
				line = line.contains(FileCreationConstants.UHL1_4_REC_PRTY_NO)?line.replace(FileCreationConstants.UHL1_4_REC_PRTY_NO, "AAAAAAAAAA"):line;
				line = line.contains(FileCreationConstants.UHL1_5_CCY_CD)?line.replace(FileCreationConstants.UHL1_5_CCY_CD, "00"):line;
				
				String wrkCodeId =" ";
				String wrkCodeDesc ="     ";
				if(procDayValue.equalsIgnoreCase(FileCreationConstants.SINGLE_DAY_VALUE)) {
					wrkCodeId="1";
					wrkCodeDesc =FileCreationConstants.DAILY;
				}else if(procDayValue.equalsIgnoreCase(FileCreationConstants.MULTI_DAY_VALUE)){
					wrkCodeId="4";
					wrkCodeDesc =FileCreationConstants.MULTI;
				}
				
				line = line.contains(FileCreationConstants.UHL1_6_WRKCD_ID)?line.replace(FileCreationConstants.UHL1_6_WRKCD_ID, wrkCodeId):line;   // 1 or 4
				line = line.contains(FileCreationConstants.UHL1_7_WRKCD_DESC)?line.replace(FileCreationConstants.UHL1_7_WRKCD_DESC, wrkCodeDesc):line; // Daily or MULTI
				String fileNumber = String.format(FileCreationConstants.PADDING_6_DIGIT, seqNumber).replace(' ', '0').substring(3, 6);
				line = line.contains(FileCreationConstants.UHL1_8_FILE_NO)?line.replace(FileCreationConstants.UHL1_8_FILE_NO, fileNumber):line;  // Fetching from 6 digit Seq nummber from DB

				line = line.contains(FileCreationConstants.UHL1_9_AUD_ID)?line.replace(FileCreationConstants.UHL1_9_AUD_ID, "AUD0010"):line;
			//END- UHL1 line replacement logic
				
			//START- EOF1 line replacement logic	
				line = line.contains(FileCreationConstants.EOF1_1)?line.replace(FileCreationConstants.EOF1_1, "EOF"):line;
				line = line.contains(FileCreationConstants.EOF1_2)?line.replace(FileCreationConstants.EOF1_2, "1"):line;
				line = line.contains(FileCreationConstants.HDR1_3)?line.replace(FileCreationConstants.HDR1_3, "A"):line;
				line = line.contains(FileCreationConstants.HDR1_4_SUN)?line.replace(FileCreationConstants.HDR1_4_SUN, serviceUserNumber):line;
				line = line.contains(FileCreationConstants.HDR1_5_S)?line.replace(FileCreationConstants.HDR1_5_S, "S"):line;
				line = line.contains(FileCreationConstants.HDR1_6_1)?line.replace(FileCreationConstants.HDR1_6_1, "1"):line;
				line = line.contains(FileCreationConstants.HDR1_7_SUN)?line.replace(FileCreationConstants.HDR1_7_SUN, serviceUserNumber):line;
				line = line.contains(FileCreationConstants.HDR1_8_SN)?line.replace(FileCreationConstants.HDR1_8_SN, String.format(FileCreationConstants.PADDING_6_DIGIT, seqNumber).replace(' ', '0')):line;   // Fetching from 6 digit Seq nummber from DB
				line = line.contains(FileCreationConstants.HDR1_9_CD)?line.replace(FileCreationConstants.HDR1_9_CD, " "+creationDate):line;
				line = line.contains(FileCreationConstants.HDR1_10_ED)?line.replace(FileCreationConstants.HDR1_10_ED, " "+expiryDate):line;
				line = line.contains(FileCreationConstants.HDR1_11)?line.replace(FileCreationConstants.HDR1_11, "000000"):line;
			//END- EOF1 line replacement logic
				
			//START- EOF2 line replacement logic	
				line = line.contains(FileCreationConstants.EOF2_1)?line.replace(FileCreationConstants.EOF2_1, "EOF"):line;
				line = line.contains(FileCreationConstants.EOF2_2)?line.replace(FileCreationConstants.EOF2_2, "2"):line;
				line = line.contains(FileCreationConstants.HDR2_3)?line.replace(FileCreationConstants.HDR2_3, "F"):line;
				line = line.contains(FileCreationConstants.HDR2_4)?line.replace(FileCreationConstants.HDR2_4, "02000"):line;
				line = line.contains(FileCreationConstants.HDR2_5)?line.replace(FileCreationConstants.HDR2_5, "00100"):line;
				line = line.contains(FileCreationConstants.HDR2_6)?line.replace(FileCreationConstants.HDR2_6, "00"):line;
			//END- EOF2 line replacement logic
				
			//START- EOF2 line replacement logic	
				line = line.contains(FileCreationConstants.UTL1_1)?line.replace(FileCreationConstants.UTL1_1, "UTL"):line;
				line = line.contains(FileCreationConstants.UTL1_2)?line.replace(FileCreationConstants.UTL1_2, "1"):line;
			//END- EOF2 line replacement logic
				
				
			//START-STD Record replacement logic
				if((line.contains(FileCreationConstants.STD_REC_1) || line.contains(FileCreationConstants.STD_REC_2))&&custPaymentDetailLst.size()>0) 
				{
						for(int i =0;i<custPaymentDetailLst.size();i++) {
							
							    String constStr = "#0"+i+"-";
							    String CustBankSrtCD ="";
							    String CustBankAccNo = "";
							    String CustBankAccName = "";
							    String accountId = "";
							    BigInteger amountDue = new BigInteger("00000000000");
							    
							    if(null != custPaymentDetailLst.get(i).getCustBankSrtCode() && !(custPaymentDetailLst.get(i).getCustBankSrtCode().isEmpty())) {
							    	CustBankSrtCD = custPaymentDetailLst.get(i).getCustBankSrtCode().length()>6?custPaymentDetailLst.get(i).getCustBankSrtCode().substring(0, 6):custPaymentDetailLst.get(i).getCustBankSrtCode();
							    }else if(custPaymentDetailLst.get(i).getCustBankSrtCode().isEmpty()) {
							    	CustBankSrtCD ="1";
							    }
							    if(null != custPaymentDetailLst.get(i).getCustBankAccNo() && !(custPaymentDetailLst.get(i).getCustBankAccNo().isEmpty())) {
							    	CustBankAccNo= custPaymentDetailLst.get(i).getCustBankAccNo().length()>8?custPaymentDetailLst.get(i).getCustBankAccNo().substring(0, 8):custPaymentDetailLst.get(i).getCustBankAccNo();
							    }else if(custPaymentDetailLst.get(i).getCustBankAccNo().isEmpty()) {
							    	CustBankAccNo="2";
							    }
							    if(null != custPaymentDetailLst.get(i).getCustBankAccName() && !(custPaymentDetailLst.get(i).getCustBankAccName().isEmpty())) {
							    	CustBankAccName= custPaymentDetailLst.get(i).getCustBankAccName().length()>18?custPaymentDetailLst.get(i).getCustBankAccName().substring(0, 18):custPaymentDetailLst.get(i).getCustBankAccName();
							    }else if(custPaymentDetailLst.get(i).getCustBankAccName().isEmpty()) {
							    	//CustBankAccName="                  ";
							    	CustBankAccName=environment.getProperty("CUST_BANK_ACCOUNT_NAME");
							    }
							    if(null != custPaymentDetailLst.get(i).getAccountId() && !(custPaymentDetailLst.get(i).getAccountId().isEmpty())) {
							    	accountId= custPaymentDetailLst.get(i).getAccountId().length()>18?custPaymentDetailLst.get(i).getAccountId().substring(0, 18):custPaymentDetailLst.get(i).getAccountId();
							    }else if(custPaymentDetailLst.get(i).getAccountId().isEmpty()) {
							    	//accountId="                  ";
							    	accountId=environment.getProperty("ACCOUNT_ID");
							    }
							    if(null != custPaymentDetailLst.get(i).getTotalAmountDue() && !(custPaymentDetailLst.get(i).getTotalAmountDue().isEmpty())) {
							    	String totalAmntDue= custPaymentDetailLst.get(i).getTotalAmountDue().length()>11?custPaymentDetailLst.get(i).getTotalAmountDue().substring(0, 11):custPaymentDetailLst.get(i).getTotalAmountDue();
							    	BigDecimal bd = new BigDecimal(totalAmntDue);
									amountDue = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(100)).toBigInteger();
							    }else if(custPaymentDetailLst.get(i).getTotalAmountDue().isEmpty()) {
							    	amountDue= new BigInteger("00000000000");
							    }
							    
							     totalAmount = totalAmount.add(amountDue);
							     
							    // transaction code deciding flag for now this value come from property file, next phase it will come from DB
							    String transactionCode = "";
							    if(!(custPaymentDetailLst.get(i).getFirstLastInd().isEmpty()) || null != custPaymentDetailLst.get(i).getFirstLastInd()) 
								  {
								    if(custPaymentDetailLst.get(i).getFirstLastInd().equalsIgnoreCase("F")) {
								    	transactionCode="01";
								    }else if(custPaymentDetailLst.get(i).getFirstLastInd().equalsIgnoreCase("I")) {
								    	transactionCode="17";
								    }else if(custPaymentDetailLst.get(i).getFirstLastInd().equalsIgnoreCase("L")) {
								    	transactionCode="19";
								    }
								  }
							    String origSortCD = environment.getProperty(FileCreationConstants.ORIGINATING_SORT_CODE);
							    String origAccNum = environment.getProperty(FileCreationConstants.ORIGINATING_ACC_NUM);
							    
							    log.info("############### Payments files ################");
							    log.info("CustBankSrtCD :: "+CustBankSrtCD);
							    log.info("CustBankAccNo :: "+CustBankAccNo);
							    log.info("CustBankAccName :: "+CustBankAccName);
							    log.info("accountId :: "+accountId);
							    log.info("amountDue :: "+amountDue);
							    log.info("transactionCode :: "+transactionCode);
							    log.info("origSortCD :: "+origSortCD);
							    log.info("origAccNum :: "+origAccNum);
							    
							    line = line.contains(constStr+FileCreationConstants.STD_REC_1)?line.replace(constStr+FileCreationConstants.STD_REC_1, String.format(FileCreationConstants.PADDING_6_DIGIT, CustBankSrtCD).replace(' ', '1')):line;
								line = line.contains(constStr+FileCreationConstants.STD_REC_2)?line.replace(constStr+FileCreationConstants.STD_REC_2, String.format(FileCreationConstants.PADDING_8_DIGIT, CustBankAccNo).replace(' ', '2')):line;
								line = line.contains(constStr+FileCreationConstants.TransactionCode)?line.replace(constStr+FileCreationConstants.TransactionCode, String.format(FileCreationConstants.PADDING_2_DIGIT, transactionCode).replace(' ', ' ')):line;
								line = line.contains(constStr+FileCreationConstants.OrigSrtCode)?line.replace(constStr+FileCreationConstants.OrigSrtCode, String.format(FileCreationConstants.PADDING_6_DIGIT, origSortCD).replace(' ', '3')):line;
								line = line.contains(constStr+FileCreationConstants.OrigAccNum)?line.replace(constStr+FileCreationConstants.OrigAccNum, String.format(FileCreationConstants.PADDING_8_DIGIT, origAccNum).replace(' ', '4')):line;
								line = line.contains(constStr+FileCreationConstants.FreeFormat)?line.replace(constStr+FileCreationConstants.FreeFormat, String.format(FileCreationConstants.PADDING_4_DIGIT, "    ").replace(' ', ' ')):line;
								line = line.contains(constStr+FileCreationConstants.Amount)?line.replace(constStr+FileCreationConstants.Amount, String.format(FileCreationConstants.PADDING_11_DIGIT, amountDue).replace(' ', '0')):line;
								line = line.contains(constStr+FileCreationConstants.userName)?line.replace(constStr+FileCreationConstants.userName, String.format(FileCreationConstants.PADDING_18_DIGIT, accountId).replace(' ', ' ')):line;
								line = line.contains(constStr+FileCreationConstants.userRefName)?line.replace(constStr+FileCreationConstants.userRefName, String.format(FileCreationConstants.PADDING_18_DIGIT, "XYZ XYZ XYZ 123456").replace(' ', ' ')):line;
								line = line.contains(constStr+FileCreationConstants.DestAccountName)?line.replace(constStr+FileCreationConstants.DestAccountName, String.format(FileCreationConstants.PADDING_18_DIGIT, CustBankAccName).replace(' ', ' ')):line;
								
								int currentJDate = Integer.parseInt(creationDate);
								if(currentJDate > prevJDate && currentJDate < nextJDate) {
									line = line.contains(constStr+FileCreationConstants.ProcessDayData)?line.replace(constStr+FileCreationConstants.ProcessDayData, String.format(FileCreationConstants.PADDING_6_DIGIT, creationDate).replace(' ', ' ')):line;
								}else {
									line = line.contains(constStr+FileCreationConstants.ProcessDayData)?line.replace(constStr+FileCreationConstants.ProcessDayData, String.format(FileCreationConstants.PADDING_6_DIGIT, "      ").replace(' ', ' ')):line;
								}
						}
				}
				
			//END-STD Record replacement logic
				
		   //START-CONTRA-Record replacement logic
			    String origSortCD = environment.getProperty(FileCreationConstants.ORIGINATING_SORT_CODE);
			    String origAccNum = environment.getProperty(FileCreationConstants.ORIGINATING_ACC_NUM);
			    String transactionCode = "99";
			    
			    
				line = line.contains(FileCreationConstants.CONTRA_REC_1)?line.replace(FileCreationConstants.CONTRA_REC_1, String.format(FileCreationConstants.PADDING_6_DIGIT, origSortCD).replace(' ', '1')):line;
				line = line.contains(FileCreationConstants.CONTRA_REC_2)?line.replace(FileCreationConstants.CONTRA_REC_2, String.format(FileCreationConstants.PADDING_8_DIGIT, origAccNum).replace(' ', '2')):line;
				line = line.contains(FileCreationConstants.CONTRA_TransactionCode)?line.replace(FileCreationConstants.CONTRA_TransactionCode, String.format(FileCreationConstants.PADDING_2_DIGIT, transactionCode).replace(' ', ' ')):line;
				line = line.contains(FileCreationConstants.CONTRA_OrigSrtCode)?line.replace(FileCreationConstants.CONTRA_OrigSrtCode, String.format(FileCreationConstants.PADDING_6_DIGIT, origSortCD).replace(' ', '3')):line;
				line = line.contains(FileCreationConstants.CONTRA_OrigAccNum)?line.replace(FileCreationConstants.CONTRA_OrigAccNum, String.format(FileCreationConstants.PADDING_8_DIGIT, origAccNum).replace(' ', '4')):line;
				line = line.contains(FileCreationConstants.CONTRA_FreeFormat)?line.replace(FileCreationConstants.CONTRA_FreeFormat, String.format(FileCreationConstants.PADDING_4_DIGIT, "    ").replace(' ', ' ')):line;
				line = line.contains(FileCreationConstants.CONTRA_Amount)?line.replace(FileCreationConstants.CONTRA_Amount, String.format(FileCreationConstants.PADDING_11_DIGIT, totalAmount).replace(' ', '0')):line;
				line = line.contains(FileCreationConstants.CONTRA_Narrative_User)?line.replace(FileCreationConstants.CONTRA_Narrative_User, String.format(FileCreationConstants.PADDING_18_DIGIT, "AAABBBCCCDDDEEEFFF").replace(' ', ' ')):line;
				line = line.contains(FileCreationConstants.CONTRA_IDENTIFY)?line.replace("{CONTRA_IDENTIFY}", String.format(FileCreationConstants.PADDING_12_DIGIT, "CONTRA            ").replace(' ', ' ')):line;
				line = line.contains(FileCreationConstants.CONTRA_User_Account_Name)?line.replace(FileCreationConstants.CONTRA_User_Account_Name, String.format(FileCreationConstants.PADDING_18_DIGIT, "ZZZYYYXXXWWWUUUVVV").replace(' ', ' ')):line;

				int currentJDate = Integer.parseInt(creationDate);
				if(currentJDate > prevJDate && currentJDate < nextJDate) {
					line = line.contains(FileCreationConstants.CONRA_PD)?line.replace(FileCreationConstants.CONRA_PD, String.format(FileCreationConstants.PADDING_6_DIGIT, creationDate).replace(' ', ' ')):line;
				}else {
					line = line.contains(FileCreationConstants.CONRA_PD)?line.replace(FileCreationConstants.CONRA_PD, String.format(FileCreationConstants.PADDING_6_DIGIT, "      ").replace(' ', ' ')):line;
				}
		  //END-CONTRA-Record replacement logic	
			
		}
		log.info("DD's file replacement logic ended");
		return line;
	}
	

}
