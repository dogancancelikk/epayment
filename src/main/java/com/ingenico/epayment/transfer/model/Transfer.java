package com.ingenico.epayment.transfer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transfer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long senderAccountId;
	private Long receiverAccountId;
	private BigDecimal amount;
	private LocalDateTime date;
	
	public Transfer(){
		
	}
	
	public Transfer(Long receiverAccountId,Long senderAccountId, BigDecimal amount, LocalDateTime date) {
        this.receiverAccountId = receiverAccountId;
        this.senderAccountId = senderAccountId;
        this.amount = amount;
        this.date = date;
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSenderAccountId() {
		return senderAccountId;
	}
	public void setSenderAccountId(Long senderAccountId) {
		this.senderAccountId = senderAccountId;
	}
	public Long getReceiverAccountId() {
		return receiverAccountId;
	}
	public void setReceiverAccountId(Long receiverAccountId) {
		this.receiverAccountId = receiverAccountId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	

}
