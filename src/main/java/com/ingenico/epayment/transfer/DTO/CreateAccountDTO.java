package com.ingenico.epayment.transfer.DTO;

import java.math.BigDecimal;

public class CreateAccountDTO {

	private String name;
	private BigDecimal balance;
	
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}
