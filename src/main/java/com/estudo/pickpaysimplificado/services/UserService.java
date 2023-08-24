package com.estudo.pickpaysimplificado.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estudo.pickpaysimplificado.domain.user.User;
import com.estudo.pickpaysimplificado.domain.user.UserType;
import com.estudo.pickpaysimplificado.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public void validateTransaction(User sender, BigDecimal amount) throws Exception {
		if(sender.getUserType() == UserType.MERCHANT) {
			throw new Exception("Usuario do tipo lojista não esta autorixaod a realizar transação");
		}
		
		if(sender.getBalance().compareTo(amount) < 0) {
			throw new Exception("Saldo insuficiente");
		}
	}
	
	public User findUserById(Long id) throws Exception {
		return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuario não encontrado"));
	}
	
	public void saveUser(User user) {
		this.repository.save(user);
	}
}
