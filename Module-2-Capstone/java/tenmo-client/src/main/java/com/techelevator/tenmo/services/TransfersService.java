package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.Scanner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfers;
import com.techelevator.tenmo.models.User;

/*******************************************************************************************************
 * This is where you code Application Services required by your solution
 * 
 * Remember:  theApp ==> ApplicationServices  ==>  Controller  ==>  DAO
 ********************************************************************************************************/

public class TransfersService {
	
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	private AuthenticatedUser user;

	public TransfersService(String url, AuthenticatedUser user) {
		this.user = user;
		BASE_URL = url;
	}

	
	
	private HttpEntity<Transfers> makeTransferEntity(Transfers transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(user.getToken());
		HttpEntity<Transfers> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(user.getToken());
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
}




