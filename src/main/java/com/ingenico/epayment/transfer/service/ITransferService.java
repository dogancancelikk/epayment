package com.ingenico.epayment.transfer.service;

import java.math.BigDecimal;
import java.util.List;

import com.ingenico.epayment.transfer.model.Transfer;

public interface ITransferService {
	
	public void createTransfer(Long senderID, Long receiverId, BigDecimal amount);
	
	public List<Transfer> getTransfers();
	
	public List<Transfer> getBySenderId(Long id);
	
	public List<Transfer> getByReceiverId(Long id);

}
