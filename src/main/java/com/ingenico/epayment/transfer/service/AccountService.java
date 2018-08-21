package com.ingenico.epayment.transfer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ingenico.epayment.transfer.exception.AccountMissingInformationException;
import com.ingenico.epayment.transfer.exception.AccountNotFoundException;
import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.repository.AccountRepository;

@Service
public class AccountService implements IAccountService {

	@Autowired
	AccountRepository accountRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	/**
	 * This method is intended for creating account.
	 * it checks account information that is used for creating account. If request misses name variable, it throws AccountMissingInformation Exception.
	 */
	@Override
	public ResponseEntity<Account> createAccount(Account account) {
		logger.info("Account who has name "+account.getName()+" is creating...");

		if (account.getName() != null && account.getName().length()>0) {
			Account newAccount = accountRepository.save(account);
			logger.info("Account is created succesfully.Account name is "+account.getName()+" and has "+account.getBalance()+"£ balance");
			return new ResponseEntity<Account>(newAccount, HttpStatus.CREATED);
		} else {
			logger.error("Missing name information on request.");
			throw new AccountMissingInformationException();
		}

	}
	
	/**
	 * This method creates multiple account. If account missing name information, it will not saved in database
	 */
	@Override
	public ResponseEntity<List<Account>> createMultipleAccounts(Collection<Account> accounts) {
		logger.debug("Accounts are creating...");
		List<Account> accountList = new ArrayList<>();
		for(Account account :accounts){
			if (account.getName() != null && account.getName().length()>0) {
				Account newAccount = accountRepository.save(account);
				logger.debug("Account is created succesfully. Account name is "+newAccount.getName()+" and it has "+newAccount.getBalance()+"£ balance");
				accountList.add(newAccount);
			}else{
				logger.error("Missing name information on request.");
			}
		}
		return new ResponseEntity<List<Account>>(accountList, HttpStatus.CREATED);
	}
	
	/**
	 * This method fetches all accounts from database 
	 */
	@Override
	public ResponseEntity<List<Account>> getAllAccountsResponse() {
		logger.debug("Fetching all accounts from database.");
		List<Account> allAccounts = accountRepository.findAll();
		if(!allAccounts.isEmpty()){
			return new ResponseEntity<List<Account>>(allAccounts, HttpStatus.OK);	
		}else{
			logger.debug("There is no account.");
			return new ResponseEntity<List<Account>>(allAccounts, HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	@Override
	public ResponseEntity<Account> getOneAccountInformationById(Long id) {
		Optional<Account> account = findAccountIfExists(id);

		return new ResponseEntity<Account>(account.get(), HttpStatus.OK);
	}
	
	
//	@Override
//	public ResponseEntity<Account> getAccountInformationByName(String name) {
//		Optional<Account> account = findAccountByNameIfExists(name);
//
//		return new ResponseEntity<Account>(account.get(), HttpStatus.OK);
//	}

	/**
	 * This method gets information of account by using account id. If account doesn't exist, it throws AccountNotFoundException. 
	 */
	private Optional<Account> findAccountIfExists(Long id) {
		logger.info("The account with account id "+id+" is fetched from the database.");
		Optional<Account> existingAccount = accountRepository.findById(id);

		if (existingAccount.isPresent()) {
			return existingAccount;
		} else {
			logger.error("There is no account with account id"+id);
			throw new AccountNotFoundException();
		}
	}
	/**
	 * This method gets information of account by using account name. If account doesn't exist, it throws AccountNotFoundException.
	 */
//	private Optional<Account> findAccountByNameIfExists(String name) {
//		Optional<Account> existingAccount = accountRepository.findByName(name);
//		logger.info("The account with account name "+name+" is fetched from the database.");
//		if (existingAccount.isPresent()) {
//			return existingAccount;
//		} else {
//			logger.warn("There is no account with account name "+name);
//			throw new AccountNotFoundException();
//		}
//	}

}
