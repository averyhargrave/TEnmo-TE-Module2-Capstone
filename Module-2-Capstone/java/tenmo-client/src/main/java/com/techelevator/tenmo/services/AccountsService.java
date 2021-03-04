package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;

public class AccountsService {

	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	private AuthenticatedUser currentUser;
	
	public AccountsService(String url, AuthenticatedUser currentUser) {
		this.currentUser = currentUser;
		BASE_URL = url;
	}
	
	public BigDecimal getBalance() {
		BigDecimal bal = new BigDecimal(0);
		bal = restTemplate.exchange(BASE_URL + "balance/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
		System.out.println("Your current account balance is: $" + bal);
		return bal;
	}
	
	 private HttpEntity makeAuthEntity() {
	    HttpHeaders headers = new HttpHeaders();		
	    headers.setBearerAuth(currentUser.getToken());
	    HttpEntity entity = new HttpEntity<>(headers);
	    return entity;
	}
	  
}

