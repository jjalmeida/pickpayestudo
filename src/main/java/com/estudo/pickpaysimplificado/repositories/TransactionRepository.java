package com.estudo.pickpaysimplificado.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estudo.pickpaysimplificado.domain.transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
