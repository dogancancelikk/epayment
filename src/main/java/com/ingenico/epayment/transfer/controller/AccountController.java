package com.ingenico.epayment.transfer.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ingenico.epayment.transfer.DTO.AccountDTO;
import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.service.IAccountService;

@RestController
@RequestMapping("account")
public class AccountController {

	@Autowired
	IAccountService accountService;

	@PostMapping("/create")
	public ResponseEntity<Account> createAccount(@RequestBody(required = true) Account account) {
		return accountService.createAccount(account);
	}

	@PostMapping("/create/multiple")
	public ResponseEntity<List<Account>> createMultipleAccounts(
			@RequestBody(required = true) Collection<Account> accounts) {
		return accountService.createMultipleAccounts(accounts);
	}

//	@GetMapping("/getbyname/{name}")
//	public ResponseEntity<Account> getAccountByName(@PathVariable("name") String name) {
//		return accountService.getAccountInformationByName(name);
//	}

	@GetMapping("/get/all")
	public ResponseEntity<List<Account>> getAllAccounts() throws Throwable {
		return accountService.getAllAccountsResponse();
	}

	@GetMapping("/getbyid/{id}")
	public ResponseEntity<Account> getAccountById(@PathVariable("id") Long id) {

		return accountService.getOneAccountInformationById(id);

	}

}
