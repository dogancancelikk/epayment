package com.ingenico.epayment.transfer.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ingenico.epayment.transfer.DTO.TransferAccountDTO;
import com.ingenico.epayment.transfer.DTO.TransferDTO;
import com.ingenico.epayment.transfer.model.Transfer;

public interface ITransferService {
	
	ResponseEntity<TransferAccountDTO> createTransfer(TransferDTO transferDTO);
	
	public ResponseEntity<List<Transfer>> getAllTransfers();
	
	public ResponseEntity<List<Transfer>> getBySenderId(Long id);
	
	public ResponseEntity<List<Transfer>> getByReceiverId(Long id);

}
