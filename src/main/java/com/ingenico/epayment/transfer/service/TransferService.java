package com.ingenico.epayment.transfer.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.ingenico.epayment.transfer.DTO.TransferAccountDTO;
import com.ingenico.epayment.transfer.DTO.TransferDTO;
import com.ingenico.epayment.transfer.exception.AccountNotFoundException;
import com.ingenico.epayment.transfer.exception.InsufficientBalanceException;
import com.ingenico.epayment.transfer.exception.UnknownTransferException;
import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.model.Transfer;
import com.ingenico.epayment.transfer.repository.AccountRepository;
import com.ingenico.epayment.transfer.repository.TransferRepository;

@Service
public class TransferService implements ITransferService {

	@Autowired
	TransferRepository transferRepository;

	@Autowired
	AccountRepository accountRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(TransferService.class);
	
	/**
	 * CreateTransfer method is intended for transferring money from one account to another.
	 * Initially the transfer request is checked whether it is valid or not by validateTransferRequest method. 
	 * If account doesn't exist or doesn't have sufficient balance,it throws AccountNotFoundException or InsufficientBalanceException.
	 * If the request is valid, the method will start transfer operations.
	 * 
	 * The method was developed with consideration of concurrent requests.For this purpose, Optimistic Locking was used.
	 * While using it, each transaction that reads data holds the value of the version property.Before the transaction wants to make an update, it checks the version property again.
	 * If there is problem because of concurrent request , the method throws ObjectOptimisticLockingFailureException.
	 * 
	 */
	@Override
	public ResponseEntity<TransferAccountDTO> createTransfer(TransferDTO transferRequest) {
		logger.debug("Transfer is starting...");
		if (validateTransferRequest(transferRequest)) {
			Optional<Account> senderAccount = accountRepository.findById(transferRequest.getSenderID());
			Optional<Account> receiverAccount = accountRepository.findById(transferRequest.getReceiverID());
			LocalDateTime transactionTime = LocalDateTime.now();
			Transfer transfer = new Transfer(transferRequest.getReceiverID(), transferRequest.getSenderID(),
					transferRequest.getAmount(), transactionTime);
			try {
				
				BigDecimal senderAccountNewBalance = senderAccount.get().getBalance()
						.subtract(transferRequest.getAmount());
				BigDecimal receiverAccountNewBalance = receiverAccount.get().getBalance()
						.add(transferRequest.getAmount());
				senderAccount.get().setBalance(senderAccountNewBalance);
				receiverAccount.get().setBalance(receiverAccountNewBalance);
					try {
						accountRepository.save(senderAccount.get());
						logger.debug(senderAccount.get().getName()+" account is updated. New balance is " + senderAccount.get().getBalance());
					} catch (ObjectOptimisticLockingFailureException optimisticLockingFailureException) {
						logger.error("Inconsistent data transfer request. The transfer request is cancelled. "+optimisticLockingFailureException.getMessage());
						throw optimisticLockingFailureException;
					}
					
					try {
						accountRepository.save(receiverAccount.get());
						logger.debug(receiverAccount.get().getName()+" account is updated. New balance is " + receiverAccount.get().getBalance());
					} catch (ObjectOptimisticLockingFailureException optimisticLockingFailureException) {
						logger.error("Inconsistent data transfer request. The transfer request is cancelled." + optimisticLockingFailureException.getMessage());
						throw optimisticLockingFailureException;
					}
					transferRepository.save(transfer);
					TransferAccountDTO transferAccountDTO = new TransferAccountDTO(senderAccount.get().getName(),
							receiverAccount.get().getName(), transferRequest.getAmount());
					return new ResponseEntity<TransferAccountDTO>(transferAccountDTO, HttpStatus.CREATED);
				} catch (UnknownTransferException exception) {
					logger.error("The transfer operation cannot be done.");
					throw exception;
				}
		} 
		return new ResponseEntity<TransferAccountDTO>(new TransferAccountDTO(),HttpStatus.OK);
	}
	
	/**
	 * This method fetchs all transfers from database
	 */
	@Override
	public ResponseEntity<List<Transfer>> getAllTransfers() {
		logger.debug("All transfer informations are being fetched.");
		List<Transfer> transfers = transferRepository.findAll();
		if (!transfers.isEmpty()) {
			return new ResponseEntity<List<Transfer>>(transfers, HttpStatus.OK);
		} else {
			logger.debug("There is no transfer information");
			return new ResponseEntity<List<Transfer>>(transfers, HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * This method provides information of sent transfers for one user
	 */
	@Override
	public ResponseEntity<List<Transfer>> getBySenderId(Long id) {
		logger.debug("Transfer information is being fetched according to sender id.");
		Account account = accountRepository.findById(id).get();
		if (account != null) {
			List<Transfer> transfers = transferRepository.findBySenderAccountId(id);
			
			return new ResponseEntity<List<Transfer>>(transfers, HttpStatus.OK);
		} else {
			logger.error("There is no account.");
			throw new AccountNotFoundException();
		}
	}
	/**
	 * This method provides information of received transfers for one user
	 */
	@Override
	public ResponseEntity<List<Transfer>> getByReceiverId(Long id) {
		logger.debug("Transfer information is being fetched according to receiver id.");
		Account account = accountRepository.findById(id).get();
		if (account != null) {
			List<Transfer> transfers = transferRepository.findByReceiverAccountId(id);
			
			return new ResponseEntity<List<Transfer>>(transfers, HttpStatus.OK);
		} else {
			logger.error("There is no account.");
			throw new AccountNotFoundException();
		}
	}
	/**
	 * ValidateTransferRequest Method takes information of transfer and checks information of transfer.
	 * @param transferDTO
	 * @return
	 */
	private boolean validateTransferRequest(TransferDTO transferDTO) {
		Optional<Account> senderAccount = accountRepository.findById(transferDTO.getSenderID());
		Optional<Account> receiverAccount = accountRepository.findById(transferDTO.getReceiverID());
		if (senderAccount.isPresent() && receiverAccount.isPresent()) {
			if (senderAccount.get().getBalance().compareTo(transferDTO.getAmount()) == 0
					|| senderAccount.get().getBalance().compareTo(transferDTO.getAmount()) == 1) {
				return true;
			} else {
				logger.error("There is no balance.");
				throw new InsufficientBalanceException();
			}
		} else {
			logger.error("There is no account.");
			throw new AccountNotFoundException();
		}
	}

}
