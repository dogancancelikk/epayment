package com.ingenico.epayment.transfer.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingenico.epayment.transfer.model.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
	
	Optional<Transfer> findById(Long id);
	
}
