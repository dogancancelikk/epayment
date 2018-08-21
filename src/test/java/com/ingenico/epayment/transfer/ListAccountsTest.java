package com.ingenico.epayment.transfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListAccountsTest {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TestHelper testHelper;
    
    private Account account;
    
    @Before
    public void addSingleAccount(){
    	Account singleAccount = new Account("DogancanCelik",new BigDecimal(123));
    	account = accountRepository.saveAndFlush(singleAccount);
    }
    
    @After
    public void afterAllTests(){
    	accountRepository.delete(account);
    }
    
    @Test
    public void listAccounts(){
    	ResponseEntity<Account[]> responseEntity = restTemplate.exchange("/account/get/all", HttpMethod.GET,
    			testHelper.getRequestHeaders(),Account[].class);
    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    	assertEquals(MediaType.APPLICATION_JSON_UTF8,responseEntity.getHeaders().getContentType());
    }
    
    @Test
    public void getSingleAccountByID(){
    	String resourceUrl = "/account/getbyid/"+account.getId();
    	ResponseEntity<Account> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.GET,testHelper.getRequestHeaders(),
    			Account.class);
    	
    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    	assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
    	
    	Account parsedAccount = responseEntity.getBody();
    	assertEquals(account.getId(), parsedAccount.getId());
    	assertEquals(account.getName(), parsedAccount.getName());
    	assertTrue(account.getBalance().compareTo(parsedAccount.getBalance())==0);
    	
    }
    
//    @Test
//    public void getSingleAccountByName(){
//    	String resourceUrl = "/account/getbyname/"+account.getName();
//    	ResponseEntity<Account> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.GET,
//    			testHelper.getRequestHeaders(),Account.class);
//    	
//    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//    	assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
//    	
//    	Account parsedAccount = responseEntity.getBody();
//    	assertEquals(account.getId(), parsedAccount.getId());
//    	assertEquals(account.getName(), parsedAccount.getName());
//    	assertTrue(account.getBalance().compareTo(parsedAccount.getBalance())==0);
//    	
//    }
    
    @Test
    public void accountNotFound() {
        String resourceUrl = "/account/getbyid/5555";
        ResponseEntity<Account> responseEntity = restTemplate.exchange(resourceUrl, 
        		HttpMethod.GET, testHelper.getRequestHeaders(), Account.class);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
    }
    
}
