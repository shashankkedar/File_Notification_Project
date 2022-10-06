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
/* 09-08-2022      Initial     Shashank Kedar(2259420)       Utility class for File Generation 

/********************************************************************************/

package com.tcs.telecom.tib.cfl.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.tcs.telecom.tib.cfl.entity.CustomerBankDetailsEntity;
import com.tcs.telecom.tib.cfl.helper.CustomerDetailsHelper;

@Component
public class CommanUtility {
	private static final Logger log = LoggerFactory.getLogger(CommanUtility.class);
	@Autowired
	Environment environment;
	
	@Autowired
	CustomerDetailsHelper helper;
	
	private String inputDateFormat;
    private String outputDateFormat;

    public CommanUtility() {
    	
    }
    private CommanUtility (String inputDateFormat, String outputDateFormat) {
        this.inputDateFormat = inputDateFormat;
        this.outputDateFormat = outputDateFormat;
    }
	
	 public String toJulian(String inputDate,String inputFormat,String suffixFormat) 
	 {
		 String suffix="";
		 try {
			 	environment.getProperty(FileCreationConstants.JULIAN_DATE_FORMAT);
			   suffix = new CommanUtility(inputFormat, suffixFormat).convert(inputDate);
			} catch (ParseException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
	         return suffix;
     }
	 
	 private String convert(String inputDate) throws ParseException {
	        SimpleDateFormat idf = new SimpleDateFormat(inputDateFormat);
	        SimpleDateFormat odf = new SimpleDateFormat(outputDateFormat);
	        Date date = idf.parse(inputDate);
	        String outputDate = odf.format(date);
	        return outputDate;
	    }
	 
	 
	public String fileTemplateReCreation(InputStream sampleFile, List<CustomerBankDetailsEntity> custPaymentDetailLst) {
		log.info("Start- fileTemplateReCreation ::");
		String line = null;
		StringBuffer sb = new StringBuffer();
		InputStreamReader streamReader = new InputStreamReader(sampleFile, StandardCharsets.UTF_8);
		BufferedReader br = new BufferedReader(streamReader);
		try {
			while ((line = br.readLine()) != null) {
				for (int i = 0; i < custPaymentDetailLst.size(); i++) {
					if (line.contains(FileCreationConstants.STD_RECORD)) {

						String newStdLine = multiplerStdRecordCreation(i); // Std record multiple line creation logic
																			// here
						sb.append(newStdLine); // appends line to string buffer
						sb.append("\n"); // line feed
					}
				}
				sb.append(line); // appends line to string buffer
				sb.append("\n"); // line feed
			}
			streamReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error while file Template Recreation for std records " + e);
		}
		log.info("End- fileTemplateReCreation ::");
		return sb.toString();
	}
	 
	 public  String multiplerStdRecordCreation(int i) {
			String DestSrtCode=FileCreationConstants.STD_REC_1;
			String DestAccNum=FileCreationConstants.STD_REC_2;
			String DestTypeAccount="0";
			String TransactionCode=FileCreationConstants.TransactionCode;
			String OrigSrtCode=FileCreationConstants.OrigSrtCode;
			String OrigAccNum=FileCreationConstants.OrigAccNum;
			String FreeFormat=FileCreationConstants.FreeFormat;
			String Amount=FileCreationConstants.Amount;
			String userName=FileCreationConstants.userName;
			String userRefName=FileCreationConstants.userRefName;
			String DestAccountName=FileCreationConstants.DestAccountName;
			String ProcessDayData=FileCreationConstants.ProcessDayData;
			String constStr= "#0"+i+"-";
			String recordLine = constStr+DestSrtCode+constStr+DestAccNum+DestTypeAccount+constStr+TransactionCode+constStr+OrigSrtCode+
					constStr+OrigAccNum +constStr+FreeFormat+constStr+Amount+constStr+userName+constStr+
					            userRefName+constStr+DestAccountName+constStr+ProcessDayData;		
			return recordLine;
		}
	 
	//File line length wise validate
		public String fileValidation(String data) throws Exception {
			log.info("##### Before validated file is ##### ");
			log.info(data);
			String resultflag=FileCreationConstants.VALID;
			Scanner scanner = new Scanner(data);
			while (scanner.hasNextLine()) {
			  String line = scanner.nextLine();
			  if(line.contains("VOL1")||line.contains("HDR1")||line.contains("HDR2")||line.contains("UHL1")||line.contains("EOF1")||line.contains("EOF2")||line.contains("UTL1")) {
				  if(line.length()!=80) {
					  resultflag = FileCreationConstants.NOT_VALID;
				  }
				  
			  }else {
				  if(line.length()!=106) {
					  resultflag=FileCreationConstants.NOT_VALID;
				  }
			  }
			}
			scanner.close();
			log.info("##### validated file  ##### ");
			return resultflag;
		}
		
		public Map<String, String> fileCreationTask(String data,String identifier) throws Exception {
			log.info("File creation task started..");
			Map<String, String> fileMap = new HashMap<>();
			String fileName = "";
			String fileType = "";
			String fStatus ="";
			String requestDateTime="";
			try {
				 String inputFormat = FileCreationConstants.FILE_NAME_DATE;
			     SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);
				 Date now = new Date();
				 String strDate = sdf.format(now);
				 String relativePath = "";
				 
				  if(identifier.equalsIgnoreCase(FileCreationConstants.AUDDIS)) {
					   fileType = FileCreationConstants.AUDDIS;
					   fileName = environment.getProperty(FileCreationConstants.AUDDIS_FILE_NAME)+strDate+".txt";
					   relativePath =  environment.getProperty(FileCreationConstants.AUDDIS_FILE_PATH)+fileName;
				  }else if(identifier.equalsIgnoreCase(FileCreationConstants.DD)) {
					   fileType = FileCreationConstants.DD;
					   fileName = environment.getProperty(FileCreationConstants.DD_FILE_NAME)+strDate+".txt";
					   relativePath =  environment.getProperty(FileCreationConstants.DD_FILE_PATH)+fileName;
					   
					      Date date=new Date();
						  SimpleDateFormat df2 = new SimpleDateFormat(FileCreationConstants.RESPONSE_DATE_TIME_FORMAT);
						  requestDateTime = df2.format(date);
				  }

				 File myObj = new File(relativePath);
			      if (myObj.createNewFile()) {
			    	  log.info("File created: " + myObj.getName());
				      fStatus=FileCreationConstants.SUCCESS;
			      } else {
			    	  log.info("File already exists.");
			    	  fStatus=FileCreationConstants.FAILED;
			      }
			      
			      FileWriter myWriter = new FileWriter(relativePath);
			      myWriter.write(data);
			      myWriter.close();
			      
			} catch (IOException e) {
				e.printStackTrace();
				log.error("Error while file creation :: " + e);
				fStatus=FileCreationConstants.FAILED;
			} 
			log.info("File creation task ended..");
			fileMap.put(FileCreationConstants.FILE_STATUS, fStatus);
			fileMap.put(FileCreationConstants.FILE_NAME, fileName);
			fileMap.put(FileCreationConstants.FILE_TYPE, fileType);
			fileMap.put(FileCreationConstants.REQ_DATE_TIME, requestDateTime);
			
			return fileMap;
		}
		
	public String processingDateCalculation(List<Map<String, Object>> hldLst) throws Exception {
		log.info("Inside processingDateCalculation method ");
		String processingDate = "365";
		try {
			List<String> holidayLst = new ArrayList<>();

			LocalDate ld = LocalDate.now();
			String tomorrowDate = toJulian(ld.plusDays(1).toString(), FileCreationConstants.INPUT_DATE,
					environment.getProperty(FileCreationConstants.JULIAN_DATE_FORMAT));
			log.info("tommorrow date :: " + tomorrowDate);

			for (int i = 0; i < hldLst.size(); i++) {
				Map<String, Object> extractLst = new HashMap<>();

				extractLst = hldLst.get(i);
				String configureDate = (!extractLst.isEmpty() && extractLst.containsKey(FileCreationConstants.RHM_OPR_ID))
						? (null != extractLst.get(FileCreationConstants.RHM_OPR_ID)) ? extractLst.get(FileCreationConstants.RHM_OPR_ID).toString()
								: String.valueOf(extractLst.get(FileCreationConstants.RHM_OPR_ID))
						: "";
				holidayLst.add(configureDate);

			}

			boolean holidayCheckFlag = holidayLst.contains(tomorrowDate);
			log.info("is tommrow holiday ? " + holidayCheckFlag);

			processingDate = tomorrowDate;
			int i = 2;
			while (true) {

				if (holidayCheckFlag) {
					processingDate = toJulian(ld.plusDays(i).toString(), FileCreationConstants.INPUT_DATE,
							environment.getProperty(FileCreationConstants.JULIAN_DATE_FORMAT));
					holidayCheckFlag = holidayLst.contains(processingDate) ? true : false;
				} else {
					break;
				}
				i++;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			log.error(e.toString());
			throw new Exception(e);
		}
		log.info("Inside processingDateCalculation method ended:: BAC PROC Date :: "+ processingDate);
		return processingDate;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<HashMap<String, Object>> insertionInMasterTable(List<Map<String, Object>> accountDetailsLst,Map<String, String> fileMap) throws Exception {
		log.info("insertion in master table started ::");
		List<HashMap<String, Object>> lstMap = new ArrayList<>();
		try {
		for (Map<String, Object> singleMap : accountDetailsLst) {

			HashMap<String, Object> hashMap = (singleMap instanceof HashMap) ? (HashMap) singleMap: new HashMap<String, Object>(singleMap);
			hashMap.putAll(fileMap);
			HashMap<String, Object> returnMap  = helper.insertInMasterTbl(hashMap);
			lstMap.add(returnMap);
		}
		
		log.info("insertion in master table ended ::" + lstMap);
		}catch (Exception e) {
				e.printStackTrace();
				log.error("error occurred while inserting a row in MST table " + e);
				throw new Exception(e);
		}
		return lstMap;
	}

	public void transferFiles(Map<String, String> fileMap) throws IOException {

		String fileType = fileMap.get(FileCreationConstants.FILE_TYPE);
		String fileName = fileMap.get(FileCreationConstants.FILE_NAME);
		String srcFilePath = "";
		String destFilePath = "";
		if (fileType.equalsIgnoreCase(FileCreationConstants.AUDDIS)) {
			srcFilePath = environment.getProperty(FileCreationConstants.AUDDIS_FILE_PATH) + fileName;
			destFilePath = environment.getProperty("HSBC_AUDDIS_FILE_PATH");
		} else {
			srcFilePath = environment.getProperty(FileCreationConstants.DD_FILE_PATH) + fileName;
			destFilePath = environment.getProperty("HSBC_DD_FILE_PATH");
		}

		String SFTPHOST = environment.getProperty(FileCreationConstants.HSBC_HOST);
		int SFTPPORT = Integer.parseInt(environment.getProperty(FileCreationConstants.HSBC_PORT));
		String SFTPUSER = environment.getProperty(FileCreationConstants.HSBC_USER_NAME);
		String SFTPPASS = environment.getProperty(FileCreationConstants.HSBC_PWD);

		String SFTPWORKINGDIR = destFilePath;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		log.info("preparing the host information for sftp.");

		try {

			JSch jsch = new JSch();

			session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);

			session.setPassword(SFTPPASS);

			java.util.Properties config = new java.util.Properties();

			config.put("StrictHostKeyChecking", "no");

			session.setConfig(config);

			session.connect();

			log.info("Host connected.");

			channel = session.openChannel(environment.getProperty(FileCreationConstants.HSBC_CHANNEL));

			channel.connect();

			log.info("sftp channel opened and connected.");

			channelSftp = (ChannelSftp) channel;

			channelSftp.cd(SFTPWORKINGDIR);

			File f = new File(srcFilePath);

			channelSftp.put(new FileInputStream(f), f.getName());

			log.info("File transfered successfully to host.");

			// TO DO
			// Once file created on HSBC server then move that files to backup folder our
			// own server & remove from current location
			//moveFile("B",fileType,srcFilePath);

		} catch (Exception ex) {
			ex.printStackTrace();
			log.info("Exception found while tranfer the response." + ex);
			log.error(ex.getMessage());

			// TO DO
			// error comes then move that files to failed folder our own server & remove
			// from current location
			//moveFile("D",fileType,srcFilePath);

		} finally {

			channelSftp.exit();
			log.info("sftp Channel exited.");
			channel.disconnect();
			session.disconnect();
			log.info("Host Session disconnected.");

		}

	}
	private void moveFile(String decidingFlag,String fileType, String srcFilePATH) throws IOException {
		
		Path srcPath= Paths.get(srcFilePATH);
		Path desPath = null;
		if(fileType.equalsIgnoreCase("AUDDIS")) {
			if(decidingFlag.equalsIgnoreCase("B")) {
				desPath = Paths.get("/app/server/AUDDIS_FILES/backup/");
			}else if(decidingFlag.equalsIgnoreCase("F")) {
				desPath = Paths.get("/app/server/AUDDIS_FILES/backup/failed/");
			}
		}else {
			if(decidingFlag.equalsIgnoreCase("B")) {
				desPath = Paths.get("/app/server/DD_FILES/backup/");
			}else if(decidingFlag.equalsIgnoreCase("F")) {
				desPath = Paths.get("/app/server/DD_FILES/failed/");
			}
		}
		log.info("Source path :: " + srcPath);
		log.info("Destination path :: " + desPath);
		Path temp = Files.move(srcPath,desPath);
		 
		        if(temp != null)
		        {
		            log.info("File renamed and moved successfully");
		        }
		        else
		        {
		        	log.info("Failed to move the file");
		        }
		
	}
}
