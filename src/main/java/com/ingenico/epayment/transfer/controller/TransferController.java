package com.ingenico.epayment.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ingenico.epayment.transfer.DTO.TransferAccountDTO;
import com.ingenico.epayment.transfer.DTO.TransferDTO;
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
	
	
	
	
}
