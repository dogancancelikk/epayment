package com.ingenico.epayment.transfer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ingenico.epayment.transfer.DTO.TransferAccountDTO;
import com.ingenico.epayment.transfer.DTO.TransferDTO;
import com.ingenico.epayment.transfer.model.Transfer;
import com.ingenico.epayment.transfer.service.ITransferService;

@RestController
@RequestMapping("/transfer")
public class TransferController {
	
	@Autowired
	ITransferService transferService;
	
	@PostMapping("/create")
	public ResponseEntity<TransferAccountDTO> createTransfer(@RequestBody TransferDTO transferRequest){
		return transferService.createTransfer(transferRequest);
	}
	
	@GetMapping("/getsenttransfers/{id}")
	public ResponseEntity<List<Transfer>> getSentTransfersBySenderId(@PathVariable("id") Long id){
		return transferService.getBySenderId(id);
	}
	
	@GetMapping("/getreceivedtransfers/{id}")
	public ResponseEntity<List<Transfer>> getReceivedTransfersByReceiverId(@PathVariable("id") Long id){
		return transferService.getByReceiverId(id);
	}
	
	@GetMapping("/getalltransfers")
	public ResponseEntity<List<Transfer>> getAllTransfers(){
		return transferService.getAllTransfers();	
	}
	
}
