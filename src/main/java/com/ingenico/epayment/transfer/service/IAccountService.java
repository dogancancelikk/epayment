package com.ingenico.epayment.transfer.service;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.ingenico.epayment.transfer.DTO.AccountDTO;
import com.ingenico.epayment.transfer.model.Account;

public interface IAccountService {
	
	public ResponseEntity<Account> createAccount(Account account);
	
	ResponseEntity<Account> getOneAccountInformationById(Long id);

	ResponseEntity<Account> getAccountInformationByName(String name);
	
	ResponseEntity<List<Account>> getAllAccountsResponse();
	
	ResponseEntity<List<Account>> createMultipleAccounts(Collection<Account> accounts);
}
