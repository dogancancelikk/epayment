package com.ingenico.epayment.transfer;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
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

import com.ingenico.epayment.transfer.DTO.AccountDTO;
import com.ingenico.epayment.transfer.model.Account;
import com.ingenico.epayment.transfer.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateAccountTest {

	@Autowired
	TestHelper testHelper;

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	AccountRepository accountRepository;

	private Account createdAccount;

	private List<AccountDTO> accountList;

	@After
	public void cleanup() {
		if (null != createdAccount) {
			accountRepository.delete(createdAccount);
		}
	}

	@Test
	public void createNewACcount() {
		String resourceUrl = "/account/create";
		String name = "Dogancan";
		BigDecimal balance = new BigDecimal(150.22);
		String balanceString = "151";

		JSONObject postBody = testHelper.constructAccount(name, balance);
		ResponseEntity<Account> responseEntity = testRestTemplate.exchange(resourceUrl, HttpMethod.POST,
				testHelper.getPostRequestHeaders(postBody.toString()), Account.class);
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());

		createdAccount = responseEntity.getBody();

		assertEquals(name, createdAccount.getName());
		assertEquals(balance, createdAccount.getBalance());
	}

	@Test
	public void createNewAccountWithoutName() {
		String resourceUrl = "/account/create";
		BigDecimal balance = new BigDecimal(132);
		JSONObject postObject = testHelper.constructAccount(null, balance);
		ResponseEntity<Account> responseEntity = testRestTemplate.exchange(resourceUrl, HttpMethod.POST,
				testHelper.getPostRequestHeaders(postObject.toString()), Account.class);

		assertEquals(422, responseEntity.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
	}

	@Test
	public void createNewAccountWithBlankName() {
		String resourceUrl = "/contact/create";
		String name = "";
		BigDecimal balance = new BigDecimal(152);

		JSONObject postBody = testHelper.constructAccount(name, balance);

		ResponseEntity<Account> responseEntity = testRestTemplate.exchange(resourceUrl, HttpMethod.POST,
				testHelper.getPostRequestHeaders(postBody.toString()), Account.class);

		assertEquals(422, responseEntity.getStatusCodeValue());
		assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
	}

	

}
