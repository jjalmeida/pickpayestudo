package com.estudo.pickpaysimplificado.domain.dtos;

import java.math.BigDecimal;

import com.estudo.pickpaysimplificado.domain.user.UserType;

public record UserDTO(String firstName, String lastName, String document, BigDecimal balance, String email, String password, UserType userType) {

}
