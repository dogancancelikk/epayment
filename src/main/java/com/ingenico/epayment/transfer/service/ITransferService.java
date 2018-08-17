package com.ingenico.epayment.transfer.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ingenico.epayment.transfer.DTO.TransferAccountDTO;
import com.ingenico.epayment.transfer.DTO.TransferDTO;
import com.ingenico.epayment.transfer.model.Transfer;

public interface ITransferService {
	
	ResponseEntity<TransferAccountDTO> createTransfer(TransferDTO transferDTO);
	
	public List<Transfer> getTransfers();
	
	public List<Transfer> getBySenderId(Long id);
	
	public List<Transfer> getByReceiverId(Long id);

}
