package com.ingenico.epayment.transfer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingenico.epayment.transfer.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	
	public Optional<Account> findById(Long id);

	public Optional<Account> findByName(String name);
	
	
}
