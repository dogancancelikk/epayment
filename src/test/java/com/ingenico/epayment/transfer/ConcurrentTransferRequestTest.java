package com.ingenico.epayment.transfer;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import com.ingenico.epayment.transfer.DTO.TransferAccountDTO;
import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConcurrentTransferRequestTest {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	TestHelper testHelper;
	
	@Autowired
	TestRestTemplate testRestTemplate;
	
	
	private TransferAccountDTO transferAccountDTO;
	
	@Before
	public void setUp() throws Exception {
		accountRepository.save(new Account("AdilBilbil", new BigDecimal("1500")));
		accountRepository.save(new Account("MTosun", new BigDecimal("1200")));
		accountRepository.save(new Account("Ayse", new BigDecimal("1300")));
		accountRepository.save(new Account("Ali", new BigDecimal("1400")));
	}
	@Test(expected = ObjectOptimisticLockingFailureException.class)
	public void testConcurrencyWriting(){
		Account account1 = accountRepository.findByName("AdilBilbil").get();
		Account account2 = accountRepository.findByName("AdilBilbil").get();
		
		account1.setBalance(new BigDecimal("1700"));
		account2.setBalance(new BigDecimal("1550"));
		
		assertEquals(0,account1.getVersion().intValue());
		assertEquals(0,account2.getVersion().intValue());
		
		accountRepository.save(account1);
		accountRepository.save(account2);
	}
	
//	@Test
//	public void concurrentTransferRequestTest(){
//		String resourceUrl = "/transfer/create";
//		Account account1 = accountRepository.findByName("AdilBilbil").get();
//		Account account2 = accountRepository.findByName("AdilBilbil").get();
//		Account receiverAccount = accountRepository.findByName("MTosun").get();
//		
//		account1.setBalance(new BigDecimal("1700"));
//		account2.setBalance(new BigDecimal("1550"));
//		
//		JSONObject postBody = testHelper.constructTransfer(account1.getId(), receiverAccount.getId(),new BigDecimal("500"));
//		
//		ResponseEntity<TransferAccountDTO> responseEntity = testRestTemplate.exchange(resourceUrl, HttpMethod.POST,
//				testHelper.getPostRequestHeaders(postBody.toString()),TransferAccountDTO.class);
//		ResponseEntity<TransferAccountDTO> responseEntity2 = testRestTemplate.exchange(resourceUrl, HttpMethod.POST,
//				testHelper.getPostRequestHeaders(postBody.toString()),TransferAccountDTO.class);
//		
//		transferAccountDTO = responseEntity.getBody();
//		TransferAccountDTO transferAccount2 = new TransferAccountDTO();
//		transferAccount2 = responseEntity2.getBody();
//		
//	}
	
//	@Test
//	public void createNewACcount() {
//		String resourceUrl = "/account/create";
//		String name = "Dogancan";
//		BigDecimal balance = new BigDecimal(150.22);
//		String balanceString = "151";
//
//		JSONObject postBody = testHelper.constructAccount(name, balance);
//		ResponseEntity<Account> responseEntity = testRestTemplate.exchange(resourceUrl, HttpMethod.POST,
//				testHelper.getPostRequestHeaders(postBody.toString()), Account.class);
//		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//		assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
//
//		createdAccount = responseEntity.getBody();
//
//		assertEquals(name, createdAccount.getName());
//		assertEquals(balance, createdAccount.getBalance());
//	}

}
