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
/* 20-09-2022      Initial     Shashank Kedar(2259420)       these is the class where we prepare request for SOAP SERVICE and call the SOAP Web service

/********************************************************************************/

package com.tcs.telecom.tib.cfl.soap;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.tcs.telecom.tib.cfl.helper.CustomerDetailsHelper;
import com.tcs.telecom.tib.cfl.repository.daoImpl.JDBCRepository;
import com.tcs.telecom.tib.cfl.util.FileCreationConstants;


@Service
public class SOAP_Service {
	private static final Logger log = LoggerFactory.getLogger(SOAP_Service.class);
	
	@Autowired
	CustomerDetailsHelper helper;
	
	@Autowired
	Environment env;
	@Autowired
	JDBCRepository jdbcRepository;
	
	@SuppressWarnings("rawtypes")
	public void preparationForSOAPService(List<HashMap<String, Object>> accountDetailsLst) throws Exception{
		log.info("prepartion of SOAP call started.. SOAP service calling only when filetype is 'Payments'");
		
			String xmlFileTemplate = readXMLTemplate();
			for(Map<String, Object> singleMap : accountDetailsLst) {
			
				String finalSOAPRequesst = recreationSOAPRequest(xmlFileTemplate,singleMap);
				
				HashMap<String, Object> responseHmap = soapServiceCalling(finalSOAPRequesst);
				
				
				String Response_Status = (responseHmap.containsKey(FileCreationConstants.Response_Status) && null != responseHmap.get(FileCreationConstants.Response_Status))? responseHmap.get(FileCreationConstants.Response_Status).toString():"";
				String responseCode= (responseHmap.containsKey(FileCreationConstants.Response_Code) && null != responseHmap.get(FileCreationConstants.Response_Code))? responseHmap.get(FileCreationConstants.Response_Code).toString():"";
				String actualStatusType = (responseHmap.containsKey(FileCreationConstants.STATUS_TYPE) && null != responseHmap.get(FileCreationConstants.STATUS_TYPE))? responseHmap.get(FileCreationConstants.STATUS_TYPE).toString():"";
				String actualStatusCode = (responseHmap.containsKey(FileCreationConstants.STATUS_CODE) && null != responseHmap.get(FileCreationConstants.STATUS_CODE))? responseHmap.get(FileCreationConstants.STATUS_CODE).toString():"";
				String actualStatusDesc = (responseHmap.containsKey(FileCreationConstants.STATUS_DESC) && null != responseHmap.get(FileCreationConstants.STATUS_DESC))? responseHmap.get(FileCreationConstants.STATUS_DESC).toString():"";
				
				String response= (responseHmap.containsKey(FileCreationConstants.Response) && null != responseHmap.get(FileCreationConstants.Response))? responseHmap.get(FileCreationConstants.Response).toString():"";
				String responseDate= (responseHmap.containsKey(FileCreationConstants.UpdatedDateTime) && null != responseHmap.get(FileCreationConstants.UpdatedDateTime))? responseHmap.get(FileCreationConstants.UpdatedDateTime).toString():"";
						
				String hobs_Status =FileCreationConstants.FAILED;
				//String dd_File_Status = FileCreationConstants.FAILED;   //unused code
				String hsbc_status = (singleMap.containsKey(FileCreationConstants.FILE_STATUS) && null != singleMap.get(FileCreationConstants.FILE_STATUS))? singleMap.get(FileCreationConstants.FILE_STATUS).toString():FileCreationConstants.SUCCESS;
				String code = actualStatusCode.isEmpty()?responseCode:actualStatusCode;
				if((!Response_Status.isEmpty()) && (Response_Status.equalsIgnoreCase(FileCreationConstants.OK))) {
					hobs_Status = actualStatusType.isEmpty()?FileCreationConstants.SUCCESS:actualStatusType;
					hsbc_status = FileCreationConstants.SUBMIT;
					//dd_File_Status = FileCreationConstants.SUCCESS;   //unused code
				}
				
				
				if(singleMap.containsKey(FileCreationConstants.INTERACTION_ID)) {
					String interactionID = null != singleMap.get(FileCreationConstants.INTERACTION_ID)?singleMap.get(FileCreationConstants.INTERACTION_ID).toString():"";
					String pInteractionID = null != singleMap.get("P_INTERACTION_ID")?singleMap.get("P_INTERACTION_ID").toString():"";
					if(!interactionID.isEmpty()) {
						//update hob_Status in mst table where interaction_id = (singleMap.get("InteractionID"))
						jdbcRepository.updateMstTbl(finalSOAPRequesst,Response_Status,code,response,responseDate,hobs_Status,hsbc_status,actualStatusDesc,interactionID);
						
					/* unused code
						if(!pInteractionID.isEmpty()) {
						//below updated qry for, DD_FILE_STATUS updation in parent/AUDDIS record update where p_interaction = (singleMap.get("PARENT_INTERACTION_ID"))
						jdbcRepository.updateParentRecord(dd_File_Status,pInteractionID);
						}
					*/	
						
					}
					
				}
				
			}
   }
	
	

	private String readXMLTemplate() throws Exception {
		log.info("Inside readXMLTemplate started..");
		String line = null;
		StringBuffer sb = new StringBuffer(); 
		try {
			InputStream sampleFile = getClass().getClassLoader().getResourceAsStream(FileCreationConstants.SOAP_XML_TEMPLATE_PATH);

			InputStreamReader streamReader = new InputStreamReader(sampleFile, StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(streamReader);

			while ((line = br.readLine()) != null) {
						sb.append(line);
			}
			streamReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("error occurred while SOAP template creation :" , e);
			throw new Exception(e);
		}
		log.info("Inside readXMLTemplate ended..");
		return sb.toString();
	}
	
	private String recreationSOAPRequest(String xmlFileTemplate, Map<String, Object> singleMap) throws Exception {
		log.info("Inside recreationSOAPRequest started..");
		String newXMLRequest=xmlFileTemplate;
		
		if(!(xmlFileTemplate.isEmpty())) 
		{
			 //pay_by_date conversion for SOAP specific format
			    String payByDate = (singleMap.containsKey(FileCreationConstants.XML_PAY_BY_DATE_VALUE) && null != singleMap.get(FileCreationConstants.XML_PAY_BY_DATE_VALUE))? singleMap.get(FileCreationConstants.XML_PAY_BY_DATE_VALUE).toString():null;
			    log.info("payByDate :: " +payByDate);
			    String modifyPayByDate ="";
			    if(null != payByDate) 
				    {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
					SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
					Date d = sdf.parse(payByDate);
					modifyPayByDate = output.format(d);
					log.info("modifyPayByDate :: " +modifyPayByDate);
				    }else {
				    	 SimpleDateFormat f = new SimpleDateFormat(FileCreationConstants.PAY_BY_DATE_FORMAT);
				    	 modifyPayByDate =f.format(new Date());
						 log.info("modifyPayByDate => " + modifyPayByDate);
				    }
				
			 String interactionIdValue = (singleMap.containsKey(FileCreationConstants.INTERACTION_ID) && null != singleMap.get(FileCreationConstants.INTERACTION_ID))? singleMap.get(FileCreationConstants.INTERACTION_ID).toString():"";
			 
			 String accountId = (singleMap.containsKey(FileCreationConstants.ACCOUNT_ID) && null != singleMap.get(FileCreationConstants.ACCOUNT_ID))? singleMap.get(FileCreationConstants.ACCOUNT_ID).toString():"";
			 String customerId = (singleMap.containsKey(FileCreationConstants.XML_CUSTOMER_ID_VALUE) && null != singleMap.get(FileCreationConstants.XML_CUSTOMER_ID_VALUE))? singleMap.get(FileCreationConstants.XML_CUSTOMER_ID_VALUE).toString():"";
			 String subscriberId = (singleMap.containsKey(FileCreationConstants.SUBSCRIBER_ID) && null != singleMap.get(FileCreationConstants.SUBSCRIBER_ID))? singleMap.get(FileCreationConstants.SUBSCRIBER_ID).toString():"";
			 String accountCCY = (singleMap.containsKey(FileCreationConstants.XML_ACC_CUR_VALUE) && null != singleMap.get(FileCreationConstants.XML_ACC_CUR_VALUE))? singleMap.get(FileCreationConstants.XML_ACC_CUR_VALUE).toString():"";
			 String billDue = (singleMap.containsKey(FileCreationConstants.XML_BILL_DUE_VALUE) && null != singleMap.get(FileCreationConstants.XML_BILL_DUE_VALUE))? singleMap.get(FileCreationConstants.XML_BILL_DUE_VALUE).toString():"";
			 String invoiceNumber = (singleMap.containsKey(FileCreationConstants.XML_INVOICE_NUM_VALUE) && null != singleMap.get(FileCreationConstants.XML_INVOICE_NUM_VALUE))? singleMap.get(FileCreationConstants.XML_INVOICE_NUM_VALUE).toString():"";
			 String opID = (singleMap.containsKey(FileCreationConstants.op_id) && null != singleMap.get(FileCreationConstants.op_id))? singleMap.get(FileCreationConstants.op_id).toString():"";
			 
			
			 
			 newXMLRequest = newXMLRequest.contains(FileCreationConstants.XML_INTERACTION_ID)?newXMLRequest.replace(FileCreationConstants.XML_INTERACTION_ID,interactionIdValue ):newXMLRequest;
			 newXMLRequest = newXMLRequest.contains(FileCreationConstants.XML_PAY_BY_DATE)?newXMLRequest.replace(FileCreationConstants.XML_PAY_BY_DATE, modifyPayByDate):newXMLRequest;
			 newXMLRequest = newXMLRequest.contains(FileCreationConstants.XML_ACCOUNT_ID)?newXMLRequest.replace(FileCreationConstants.XML_ACCOUNT_ID, accountId):newXMLRequest;
			 newXMLRequest = newXMLRequest.contains(FileCreationConstants.XML_CUSTOMER_ID)?newXMLRequest.replace(FileCreationConstants.XML_CUSTOMER_ID, customerId):newXMLRequest;
			 newXMLRequest = newXMLRequest.contains(FileCreationConstants.XML_SUBSCRIBER_ID)?newXMLRequest.replace(FileCreationConstants.XML_SUBSCRIBER_ID, subscriberId):newXMLRequest;
			 newXMLRequest = newXMLRequest.contains(FileCreationConstants.XML_PARTY_TYPE)?newXMLRequest.replace(FileCreationConstants.XML_PARTY_TYPE, FileCreationConstants.XML_PARTY_TYPE_VALUE):newXMLRequest;
			 newXMLRequest = newXMLRequest.contains(FileCreationConstants.XML_ACCOUNT_CURRENCY)?newXMLRequest.replace(FileCreationConstants.XML_ACCOUNT_CURRENCY, accountCCY):newXMLRequest;
			 newXMLRequest = newXMLRequest.contains(FileCreationConstants.XML_ACCOUNT_AMOUNT)?newXMLRequest.replace(FileCreationConstants.XML_ACCOUNT_AMOUNT, billDue):newXMLRequest;
			 newXMLRequest = newXMLRequest.contains(FileCreationConstants.XML_INVOICE_NUMBER)?newXMLRequest.replace(FileCreationConstants.XML_INVOICE_NUMBER, invoiceNumber):newXMLRequest;
			 newXMLRequest = newXMLRequest.contains(FileCreationConstants.XML_OP_ID)?newXMLRequest.replace(FileCreationConstants.XML_OP_ID, opID):newXMLRequest;
			 
		}
		log.info("Inside recreationSOAPRequest ended..");
		return newXMLRequest;
	}
	
	private HashMap<String, Object> soapServiceCalling(String finalSOAPRequesst) throws Exception {
		log.info("Inside soapServiceCalling started..");
		HashMap<String, Object> responseHmap = new HashMap<String, Object>();
		String responseStatus = null;
		int responseCode = 0;
		String responseDate ="";
		StringBuffer response = new StringBuffer();
		
		String statusCode ="";
		String statusType ="";
		String statusDesc ="";
		
		try {
			String url = env.getProperty(FileCreationConstants.SOAP_URL);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod(FileCreationConstants.SOAP_METHOD_TYPE);
			con.setRequestProperty(FileCreationConstants.CONTENT_TYPE, FileCreationConstants.CONTENT_TYPE_VALUE);
			con.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(finalSOAPRequesst);
			wr.flush();
			wr.close();

			responseCode = con.getResponseCode();
			System.out.println("Response code :: "+ responseCode);
			responseStatus = con.getResponseMessage();
			BufferedReader in ;
			if(!(responseCode==FileCreationConstants.SOAP_SUCCESS_RESP_CODE)) {
				InputStream error = ((HttpURLConnection) con).getErrorStream();
				 in = new BufferedReader(new InputStreamReader(error));
			}else {
			
			 in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			}
			
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			Date date=new Date(con.getDate());
	        SimpleDateFormat df2 = new SimpleDateFormat(FileCreationConstants.RESPONSE_DATE_TIME_FORMAT);
	        responseDate = df2.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error come in soap service calling method :",e);
			throw new Exception(e);
		}
		
		if(responseCode==200) {
			 statusCode = response.substring(response.indexOf("<ns3:statusCode>")+16, response.indexOf("</ns3:statusCode>"));
			 statusType = response.substring(response.indexOf("<ns3:statusType>")+16, response.indexOf("</ns3:statusType>"));
			 statusDesc = response.substring(response.indexOf("<ns3:statusDescription>")+23, response.indexOf("</ns3:statusDescription>"));
		}
		
		responseHmap.put(FileCreationConstants.Response_Status, responseStatus);
		responseHmap.put(FileCreationConstants.Response_Code, responseCode);
		responseHmap.put(FileCreationConstants.Response, response);
		responseHmap.put(FileCreationConstants.UpdatedDateTime, responseDate);
		responseHmap.put(FileCreationConstants.STATUS_CODE, statusCode);
		responseHmap.put(FileCreationConstants.STATUS_TYPE, statusType);
		responseHmap.put(FileCreationConstants.STATUS_DESC, statusDesc);
		
		log.info("Inside soapServiceCalling ended ..");
		return responseHmap;
	}
}
