package com.ingenico.epayment.transfer.controller;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ingenico.epayment.transfer.model.Transfer;
import com.ingenico.epayment.transfer.service.ITransferService;

@RestController
public class TransferController {
	
	@Autowired
	ITransferService transferService;
	
	@RequestMapping(value = "/transfer/create/{id},{receiverId},{amount}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String createTransfer(@PathVariable("id")Long id, @PathVariable("receiverId")Long receiverId,@PathVariable("amount")BigDecimal amount){
		transferService.createTransfer(id, receiverId, amount);
		List<Transfer> transfers = transferService.getTransfers();
		for (Transfer transfer : transfers) {
			System.out.println(transfer.getAmount());
			System.out.println(transfer.getSenderAccountId());
		}
		System.out.println("Sender Id: " +id+" Receiver ID"+receiverId);
		return null;
	}
	
	
	
	
}
