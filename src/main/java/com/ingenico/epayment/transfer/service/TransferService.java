package com.ingenico.epayment.transfer.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ingenico.epayment.transfer.DTO.TransferAccountDTO;
import com.ingenico.epayment.transfer.DTO.TransferDTO;
import com.ingenico.epayment.transfer.exception.AccountNotFoundException;
import com.ingenico.epayment.transfer.exception.BalanceNotSufficientException;
import com.ingenico.epayment.transfer.exception.UnknownTransferException;
import com.ingenico.epayment.transfer.exception.UnknownUpdateAccountBalanceException;
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

	@Override
	public ResponseEntity<TransferAccountDTO> createTransfer(TransferDTO transferRequest) {
		if (validateTransferRequest(transferRequest)) {
			Optional<Account> senderAccount = accountRepository.findById(transferRequest.getSenderID());
			Optional<Account> receiverAccount = accountRepository.findById(transferRequest.getReceiverID());
			LocalDateTime transactionTime = LocalDateTime.now();
			Transfer transfer = new Transfer(transferRequest.getReceiverID(), transferRequest.getSenderID(),
					transferRequest.getAmount(), transactionTime);
			try {
				transferRepository.saveAndFlush(transfer);
				BigDecimal senderAccountNewBalance = senderAccount.get().getBalance()
						.subtract(transferRequest.getAmount());
				BigDecimal receiverAccountNewBalance = receiverAccount.get().getBalance()
						.add(transferRequest.getAmount());
				senderAccount.get().setBalance(senderAccountNewBalance);
				receiverAccount.get().setBalance(receiverAccountNewBalance);
				try {
					accountRepository.saveAndFlush(senderAccount.get());
					accountRepository.saveAndFlush(receiverAccount.get());
					TransferAccountDTO transferAccountDTO = new TransferAccountDTO(senderAccount.get().getName(),
							receiverAccount.get().getName(), transferRequest.getAmount());
					return new ResponseEntity<TransferAccountDTO>(transferAccountDTO, HttpStatus.OK);
				} catch (Exception e) {
					transferRepository.delete(transfer);
					throw new UnknownUpdateAccountBalanceException();
				}
			} catch (Exception e) {
				System.out.println("Transfer has not completed.");
				throw new UnknownTransferException();
			}
		} else {
			throw new UnknownTransferException();
		}
	}

	private boolean validateTransferRequest(TransferDTO transferDTO) {
		Optional<Account> senderAccount = accountRepository.findById(transferDTO.getSenderID());
		Optional<Account> receiverAccount = accountRepository.findById(transferDTO.getReceiverID());
		if (senderAccount.isPresent() && receiverAccount.isPresent()) {
			if (senderAccount.get().getBalance().compareTo(transferDTO.getAmount()) == 0
					|| senderAccount.get().getBalance().compareTo(transferDTO.getAmount()) == 1) {
				return true;
			} else {
				throw new BalanceNotSufficientException();
			}
		} else {
			throw new AccountNotFoundException();
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
		Optional<Transfer> transfers = transferRepository.findById(id);
		transfers.ifPresent(transfer -> System.out.println(transfer.getAmount()));

		return null;
	}

	@Override
	public List<Transfer> getByReceiverId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
