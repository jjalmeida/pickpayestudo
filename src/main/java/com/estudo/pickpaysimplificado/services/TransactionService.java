package com.estudo.pickpaysimplificado.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estudo.pickpaysimplificado.domain.dtos.TransactionDTO;
import com.estudo.pickpaysimplificado.domain.transaction.Transaction;
import com.estudo.pickpaysimplificado.domain.user.User;
import com.estudo.pickpaysimplificado.repositories.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionRepository repository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public void createTransaction(TransactionDTO transaction) throws Exception {
		User sender = this.userService.findUserById(transaction.senderId());
		User receiver = this.userService.findUserById(transaction.receiverId());
		
		userService.validateTransaction(sender, transaction.value());
		
		boolean isAuthorized = this.authorizeTransaction(sender, transaction.value()); 
		
		if(!isAuthorized) {
			throw new Exception("Transação não autorizada");
		}
		
		Transaction newTransaction = new Transaction();
		newTransaction.setAmount(transaction.value());
		newTransaction.setSender(sender);
		newTransaction.setReceiver(receiver);
		newTransaction.setTimestamp(LocalDateTime.now());
		
		sender.setBalance(sender.getBalance().subtract(transaction.value()));
		receiver.setBalance(receiver.getBalance().add(transaction.value()));
		
		this.repository.save(newTransaction);
		this.userService.saveUser(sender);
		this.userService.saveUser(receiver);

	}
	
	public boolean authorizeTransaction(User sender, BigDecimal value) {
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> authorizarionResponse = restTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);
		
		String message = (String) authorizarionResponse.getBody().get("message");
		
		return authorizarionResponse.getStatusCode() == HttpStatus.OK && "Autorizado".equalsIgnoreCase(message);
	}
}
