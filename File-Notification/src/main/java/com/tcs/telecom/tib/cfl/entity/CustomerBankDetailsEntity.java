package com.tcs.telecom.tib.cfl.entity;
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
/* 15-09-2022      Initial     Shashank Kedar(2259420)        Mapped resultset data into entity class  

/********************************************************************************/
import org.springframework.stereotype.Component;

@Component
public class CustomerBankDetailsEntity {

	private String seqNum;
	private String payerIdentifier;
	private String custBankAccName;
	private String custBankAccNo;
	private String custBankSrtCode;
	private String partyId;
	private String status;
	private String accountId;
	private String totalAmountDue;
	private String firstLastInd;
	private String mstID;
	private String interactionID;
	private String pInteractionID;
	private String ddFileStatus;
	private String fileType;
	
	
	public CustomerBankDetailsEntity() {
		super();
	}

	public CustomerBankDetailsEntity(String seqNum, String payerIdentifier, String custBankAccName,
			String custBankAccNo, String custBankSrtCode, String partyId, String status, String accountId,
			String totalAmountDue, String firstLastInd, String mstID, String interactionID, String pInteractionID,
			String ddFileStatus, String fileType) {
		super();
		this.seqNum = seqNum;
		this.payerIdentifier = payerIdentifier;
		this.custBankAccName = custBankAccName;
		this.custBankAccNo = custBankAccNo;
		this.custBankSrtCode = custBankSrtCode;
		this.partyId = partyId;
		this.status = status;
		this.accountId = accountId;
		this.totalAmountDue = totalAmountDue;
		this.firstLastInd = firstLastInd;
		this.mstID = mstID;
		this.interactionID = interactionID;
		this.pInteractionID = pInteractionID;
		this.ddFileStatus = ddFileStatus;
		this.fileType = fileType;
	}




	public String getCustBankAccName() {
		return custBankAccName;
	}
	public void setCustBankAccName(String custBankAccName) {
		this.custBankAccName = custBankAccName;
	}
	public String getCustBankAccNo() {
		return custBankAccNo;
	}
	public void setCustBankAccNo(String custBankAccNo) {
		this.custBankAccNo = custBankAccNo;
	}
	public String getCustBankSrtCode() {
		return custBankSrtCode;
	}
	public void setCustBankSrtCode(String custBankSrtCode) {
		this.custBankSrtCode = custBankSrtCode;
	}

	public String getPayerIdentifier() {
		return payerIdentifier;
	}

	public void setPayerIdentifier(String payerIdentifier) {
		this.payerIdentifier = payerIdentifier;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getTotalAmountDue() {
		return totalAmountDue;
	}

	public void setTotalAmountDue(String totalAmountDue) {
		this.totalAmountDue = totalAmountDue;
	}

	public String getFirstLastInd() {
		return firstLastInd;
	}

	public void setFirstLastInd(String firstLastInd) {
		this.firstLastInd = firstLastInd;
	}

	public String getMstID() {
		return mstID;
	}

	public void setMstID(String mstID) {
		this.mstID = mstID;
	}

	public String getInteractionID() {
		return interactionID;
	}

	public void setInteractionID(String interactionID) {
		this.interactionID = interactionID;
	}

	public String getpInteractionID() {
		return pInteractionID;
	}

	public void setpInteractionID(String pInteractionID) {
		this.pInteractionID = pInteractionID;
	}

	public String getDdFileStatus() {
		return ddFileStatus;
	}

	public void setDdFileStatus(String ddFileStatus) {
		this.ddFileStatus = ddFileStatus;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Override
	public String toString() {
		return "CustomerBankDetailsEntity [seqNum=" + seqNum + ", payerIdentifier=" + payerIdentifier
				+ ", custBankAccName=" + custBankAccName + ", custBankAccNo=" + custBankAccNo + ", custBankSrtCode="
				+ custBankSrtCode + ", partyId=" + partyId + ", status=" + status + ", accountId=" + accountId
				+ ", totalAmountDue=" + totalAmountDue + ", firstLastInd=" + firstLastInd + ", mstID=" + mstID
				+ ", interactionID=" + interactionID + ", pInteractionID=" + pInteractionID + ", ddFileStatus="
				+ ddFileStatus + ", fileType=" + fileType + "]";
	}


}
