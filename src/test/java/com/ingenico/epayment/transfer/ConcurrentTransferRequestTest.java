package com.ingenico.epayment.transfer;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

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
	
	private Account account;
	
	@Before
	public void setUp() throws Exception {
		account = accountRepository.save(new Account("Accont1", new BigDecimal("1500")));
		
	}

	@Test(expected = ObjectOptimisticLockingFailureException.class)
	public void testConcurrencyWriting() {
		Account accountOfTransferRequest1 = accountRepository.findById(account.getId()).get();
		Account accountOfTransferRequest2 = accountRepository.findById(account.getId()).get();

		accountOfTransferRequest1.setBalance(new BigDecimal("1700"));
		accountOfTransferRequest2.setBalance(new BigDecimal("1550"));

		assertEquals(0, accountOfTransferRequest1.getVersion().intValue());
		assertEquals(0, accountOfTransferRequest2.getVersion().intValue());

		accountRepository.save(accountOfTransferRequest1);
		accountRepository.save(accountOfTransferRequest2);
	}

}
