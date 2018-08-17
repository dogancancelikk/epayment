package com.ingenico.epayment.transfer.DTO;

import java.math.BigDecimal;

public class TransferAccountDTO {
	private String senderName;
	private String receiverName;
	private BigDecimal amount;
	
	public TransferAccountDTO(){
		
	}
	public TransferAccountDTO(String senderName,String receiverName,BigDecimal amount){
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.amount = amount;
	}
	
	
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	

}
