package com.ingenico.epayment.transfer.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.model.Transfer;
import com.ingenico.epayment.transfer.repository.AccountRepository;
import com.ingenico.epayment.transfer.repository.TransferRepository;

@Service
public class TransferService implements ITransferService{
	
	@Autowired
	TransferRepository transferRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Override
	public void createTransfer(Long senderID, Long receiverId, BigDecimal amount) {
		// TODO Auto-generated method stub
		LocalDateTime transactionTime = LocalDateTime.now();
		Transfer transfer = new Transfer(receiverId, senderID, amount, transactionTime);
		
		
			Optional<Account> senderAccount = accountRepository.findById(senderID);
			Optional<Account> receiverAccount = accountRepository.findById(receiverId);
			if(senderAccount.get().getBalance().compareTo(amount)==0 || senderAccount.get().getBalance().compareTo(amount)==1 ){
				try {
				transferRepository.save(transfer);
				senderAccount.get().setBalance(senderAccount.get().getBalance()-);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		
		
	}

	@Override
	public List<Transfer> getTransfers() {
		// TODO Auto-generated method stub
		List<Transfer> getTransfer = transferRepository.findAll();
		return getTransfer;
	}

	@Override
	public List<Transfer> getBySenderId(Long id) {
		
		return null;
	}

	@Override
	public List<Transfer> getByReceiverId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
