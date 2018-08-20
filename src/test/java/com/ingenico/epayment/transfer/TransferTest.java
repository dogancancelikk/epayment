package com.ingenico.epayment.transfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ingenico.epayment.transfer.DTO.TransferAccountDTO;
import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferTest {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	TestHelper testHelper;
	
	@Autowired
	TestRestTemplate testRestTemplate;
	
	private TransferAccountDTO transferAccountDTO;
	
	@Test
	public void createTransfer(){
		Account account1 = addAccount("Dogancan", new BigDecimal(10000));
		Account account2 = addAccount("Celik", new BigDecimal(12000));
		
		String responseUrl = "/transfer/create";
		JSONObject postBody = testHelper.constructTransfer(account1.getId(), account2.getId(), new BigDecimal(1250));
		ResponseEntity<TransferAccountDTO> responseEntity = testRestTemplate.exchange(responseUrl, HttpMethod.POST,
				testHelper.getPostRequestHeaders(postBody.toString()),TransferAccountDTO.class);
		transferAccountDTO = responseEntity.getBody();
		
		assertEquals(transferAccountDTO.getSenderName(), account1.getName());
		assertEquals(transferAccountDTO.getReceiverName(), account2.getName());
		
		assertTrue(transferAccountDTO.getAmount().compareTo(new BigDecimal("1250"))==0);
		
		
		account1 = accountRepository.findById(account1.getId()).get();
		account2 = accountRepository.findById(account2.getId()).get();
		
		assertTrue(account1.getBalance().compareTo(new BigDecimal("8750"))==0);
		assertTrue(account2.getBalance().compareTo(new BigDecimal("13250"))==0);
		
	}
	
	
	
	private Account addAccount(String name,BigDecimal balance){
		Account account = new Account(name,balance);
		Account createdAccount = accountRepository.save(account);
		return createdAccount;
	}
	
	//İlk olarak iki account oluşturup bunların arasında transferi gerçekleştir.
	
	
	
	//Bir tanesinde olan balancedan daha fazla amount transfer etmesini iste.
	
	//Olmayan accounta transfer etmesini iste.
	
	//Bunları yaparken transfercontroller içerisinde yer alan createTransfer servisini kullan. RestTemplate ile.
	
}
