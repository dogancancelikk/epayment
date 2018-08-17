package com.ingenico.epayment.transfer.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ingenico.epayment.transfer.DTO.CreateAccountDTO;
import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.service.IAccountService;

@RestController
public class AccountController {
	
	@Autowired
	IAccountService accountService;
	
	@RequestMapping(value = "/account/get", method = RequestMethod.POST)
	public Optional<Account> getAccountById(@RequestBody(required = true) Long id){
		
		Optional<Account> account = accountService.getAccountInformation(id);
		
		return account;
	}
	
//		@RequestMapping(value = "/account/create", method = RequestMethod.POST,
//				produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
		@PostMapping("/account/create")
	    public String createAccount(@RequestBody(required = true) CreateAccountDTO account){
			System.out.println("Blabla");
			accountService.createAccount(account.getName(), account.getBalance());
			
			return null;

	    }
		
		@GetMapping("/account/get/{name}")
		public ResponseEntity<Account> getAccountByName(@PathVariable("name") String name){
			Optional<Account> account = accountService.getAccountInformationByName(name);
			
			return new ResponseEntity<>(account.get(),HttpStatus.OK);
		}
		
		
}
