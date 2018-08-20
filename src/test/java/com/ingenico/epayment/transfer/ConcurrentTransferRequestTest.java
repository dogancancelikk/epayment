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

	@Before
	public void setUp() throws Exception {
		accountRepository.save(new Account("Accont1", new BigDecimal("1500")));
		accountRepository.save(new Account("Account2", new BigDecimal("1200")));
		accountRepository.save(new Account("Account3", new BigDecimal("1300")));
		accountRepository.save(new Account("Account4", new BigDecimal("1400")));
	}

	@Test(expected = ObjectOptimisticLockingFailureException.class)
	public void testConcurrencyWriting() {
		Account account1 = accountRepository.findById((long)1).get();
		Account account2 = accountRepository.findById((long)1).get();

		account1.setBalance(new BigDecimal("1700"));
		account2.setBalance(new BigDecimal("1550"));

		assertEquals(0, account1.getVersion().intValue());
		assertEquals(0, account2.getVersion().intValue());

		accountRepository.save(account1);
		accountRepository.save(account2);
	}

}
