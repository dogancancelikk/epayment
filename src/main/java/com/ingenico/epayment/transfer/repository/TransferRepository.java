package com.ingenico.epayment.transfer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ingenico.epayment.transfer.model.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
	
	Optional<Transfer> findById(Long id);
	
	List<Transfer> findBySenderAccountId(Long id);
	
	List<Transfer> findByReceiverAccountId(Long id);
	
}
