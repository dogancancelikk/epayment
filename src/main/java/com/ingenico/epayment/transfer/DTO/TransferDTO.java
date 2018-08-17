package com.ingenico.epayment.transfer.DTO;

import java.math.BigDecimal;

public class TransferDTO {
	
	private Long senderID;
	private Long receiverID;
	private BigDecimal amount;
	
	
	public Long getSenderID() {
		return senderID;
	}
	public void setSenderID(Long senderID) {
		this.senderID = senderID;
	}
	public Long getReceiverID() {
		return receiverID;
	}
	public void setReceiverID(Long receiverID) {
		this.receiverID = receiverID;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	
}
