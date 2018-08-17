package com.ingenico.epayment.transfer.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.repository.AccountRepository;

@Service
public class AccountService implements IAccountService{

	@Autowired
	AccountRepository accountRepository;
	
	@Override
	public void createAccount(String name, BigDecimal amount) {
		Account account = new Account(name,amount);
		accountRepository.save(account);
		
	}

	@Override
	public Optional<Account> getAccountInformation(Long id) {
		Optional<Account> account = accountRepository.findById(id);
		
		return account;
	}
	
	@Override
	public Optional<Account> getAccountInformationByName(String name) {
		Optional<Account> account = accountRepository.findByName(name);
		
		return account;
	}


}
