package com.ingenico.epayment.transfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ingenico.epayment.transfer.DTO.TransferAccountDTO;
import com.ingenico.epayment.transfer.DTO.TransferDTO;
import com.ingenico.epayment.transfer.exception.AccountNotFoundException;
import com.ingenico.epayment.transfer.exception.InsufficientBalanceException;
import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.model.Transfer;
import com.ingenico.epayment.transfer.repository.AccountRepository;
import com.ingenico.epayment.transfer.service.ITransferService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferTest {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TestHelper testHelper;

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	ITransferService transferService;

	private TransferAccountDTO transferAccountDTO;

	private TransferDTO transferDTO;

	private Account senderAccount;

	private Account receiverAccount;

	private Account account1;

	private Account account2;

	@Before
	public void setUp() {
		senderAccount = addAccount("Robben", new BigDecimal("1900"));
		receiverAccount = addAccount("Van Persie", new BigDecimal("3000"));
		account1 = addAccount("Kevin", new BigDecimal(10000));
		account2 = addAccount("Jansen", new BigDecimal(12000));
		transferDTO = new TransferDTO(senderAccount.getId(), receiverAccount.getId(), new BigDecimal("1500"));
		transferService.createTransfer(transferDTO);
	}
	

	@Test
	public void createTransfer() {
		String responseUrl = "/transfer/create";
		JSONObject postBody = testHelper.constructTransfer(account1.getId(), account2.getId(), new BigDecimal(1250));
		ResponseEntity<TransferAccountDTO> responseEntity = testRestTemplate.exchange(responseUrl, HttpMethod.POST,
				testHelper.getPostRequestHeaders(postBody.toString()), TransferAccountDTO.class);
		transferAccountDTO = responseEntity.getBody();

		assertEquals(201, responseEntity.getStatusCodeValue());
		assertEquals(transferAccountDTO.getSenderName(), account1.getName());
		assertEquals(transferAccountDTO.getReceiverName(), account2.getName());

		assertTrue(transferAccountDTO.getAmount().compareTo(new BigDecimal("1250")) == 0);

		account1 = accountRepository.findById(account1.getId()).get();
		account2 = accountRepository.findById(account2.getId()).get();

		assertTrue(account1.getBalance().compareTo(new BigDecimal("8750")) == 0);
		assertTrue(account2.getBalance().compareTo(new BigDecimal("13250")) == 0);

	}

	@Test
	public void getSenttransfers() {
		String resourceUrl = "/transfer/getsenttransfers/" + senderAccount.getId();

		ResponseEntity<Transfer[]> sentTransfers = testRestTemplate.exchange(resourceUrl, HttpMethod.GET,
				testHelper.getRequestHeaders(), Transfer[].class);
		Transfer[] transferListBySenderID = sentTransfers.getBody();
		assertEquals(200, sentTransfers.getStatusCodeValue());
		assertEquals(MediaType.APPLICATION_JSON_UTF8, sentTransfers.getHeaders().getContentType());
		for (Transfer transfer : transferListBySenderID) {
			assertEquals(senderAccount.getId(), transfer.getSenderAccountId());
		}
	}
	@Test
	public void getReceivedTransfers() {
		String resourceUrl = "/transfer/getreceivedtransfers/" + receiverAccount.getId();

		ResponseEntity<Transfer[]> receiverTransfers = testRestTemplate.exchange(resourceUrl, HttpMethod.GET,
				testHelper.getRequestHeaders(), Transfer[].class);
		Transfer[] transferListBySenderID = receiverTransfers.getBody();
		assertEquals(200, receiverTransfers.getStatusCodeValue());
		assertEquals(MediaType.APPLICATION_JSON_UTF8, receiverTransfers.getHeaders().getContentType());
		for (Transfer transfer : transferListBySenderID) {
			assertEquals(receiverAccount.getId(), transfer.getReceiverAccountId());
		}
	}

	@Test
	public void insufficientBalanceTransferTest() {
		String resourceUrl = "/transfer/create";
		JSONObject jsonPostBody = testHelper.constructTransfer(account1.getId(), account2.getId(),
				new BigDecimal("12000"));
		ResponseEntity<Transfer> responseEntity = testRestTemplate.exchange(resourceUrl, HttpMethod.POST,
				testHelper.getPostRequestHeaders(jsonPostBody.toString()), Transfer.class);
		
		assertEquals(412, responseEntity.getStatusCodeValue());
	}

	@Test
	public void createNewTransferWithNonExistAccountID() {
		String resourceUrl = "/transfer/create";
		JSONObject jsonPostBody = testHelper.constructTransfer(account1.getId(), (long) 19, new BigDecimal("12300"));
		ResponseEntity<Transfer> responseEntity = testRestTemplate.exchange(resourceUrl, HttpMethod.POST,
				testHelper.getPostRequestHeaders(jsonPostBody.toString()), Transfer.class);
		
		assertEquals(412, responseEntity.getStatusCodeValue());

	}
	
	@Test
	public void getAllTransfers(){
		String resourceUrl = "/transfer/getalltransfers";
		ResponseEntity<Transfer[]> responseEntity = testRestTemplate.exchange(resourceUrl, HttpMethod.GET,
				testHelper.getRequestHeaders(),Transfer[].class);
		assertEquals(200, responseEntity.getStatusCodeValue());
	}

	private Account addAccount(String name, BigDecimal balance) {
		Account account = new Account(name, balance);
		Account createdAccount = accountRepository.save(account);
		return createdAccount;
	}


}
