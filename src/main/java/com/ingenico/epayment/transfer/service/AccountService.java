package com.ingenico.epayment.transfer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ingenico.epayment.transfer.DTO.AccountDTO;
import com.ingenico.epayment.transfer.exception.AccountMissingInformationException;
import com.ingenico.epayment.transfer.exception.AccountNotFoundException;
import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.repository.AccountRepository;

@Service
public class AccountService implements IAccountService {

	@Autowired
	AccountRepository accountRepository;

	@Override
	public ResponseEntity<Account> createAccount(Account account) {

		if (account.getName() != null && account.getName().length()>0) {
			Account newAccount = accountRepository.saveAndFlush(account);
			return new ResponseEntity<Account>(newAccount, HttpStatus.CREATED);
		} else {
			throw new AccountMissingInformationException();
		}

	}

	@Override
	public ResponseEntity<List<Account>> createMultipleAccounts(Collection<Account> accounts) {
		List<Account> accountList = new ArrayList<>();
		for(Account account :accounts){
			if (account.getName() != null && account.getName().length()>0) {
				Account newAccount = accountRepository.saveAndFlush(account);
				accountList.add(newAccount);
				
			} else {
				throw new AccountMissingInformationException();
			}
		}
		return new ResponseEntity<List<Account>>(accountList, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<AccountDTO>> getAllAccountsResponse() {
		List<Account> allAccounts = accountRepository.findAll();
		List<AccountDTO> allAccountDTO = new ArrayList<>();
		for (Account account : allAccounts) {
			AccountDTO newAccountDTO = new AccountDTO(account.getName(), account.getBalance());
			allAccountDTO.add(newAccountDTO);
		}
		return new ResponseEntity<List<AccountDTO>>(allAccountDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Account> getOneAccountInformation(Long id) {
		Optional<Account> account = findAccountIfExists(id);

		return new ResponseEntity<Account>(account.get(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Account> getAccountInformationByName(String name) {
		Optional<Account> account = findAccountByNameIfExists(name);

		return new ResponseEntity<Account>(account.get(), HttpStatus.OK);
	}

	private String accountUrlHelper(Account contact, HttpServletRequest request) {
		StringBuilder resourcePath = new StringBuilder();

		resourcePath.append(request.getRequestURL());
		resourcePath.append("/");
		resourcePath.append(contact.getId());

		return resourcePath.toString();
	}

	private Optional<Account> findAccountIfExists(Long id) {
		Optional<Account> existingAccount = accountRepository.findById(id);

		if (existingAccount.isPresent()) {
			return existingAccount;
		} else {
			throw new AccountNotFoundException();
		}
	}
	
	private Optional<Account> findAccountByNameIfExists(String name) {
		Optional<Account> existingAccount = accountRepository.findByName(name);
  
		if (existingAccount.isPresent()) {
			return existingAccount;
		} else {
			throw new AccountNotFoundException();
		}
	}

}
