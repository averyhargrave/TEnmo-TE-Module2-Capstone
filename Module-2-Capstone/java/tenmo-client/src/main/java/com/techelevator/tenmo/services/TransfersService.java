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
	//private AuthenticatedUser user;

	public TransfersService(String url) {
		//this.user = user;
		BASE_URL = url;
	}

	
	// list all transfers
	public Transfers[] transfersList(AuthenticatedUser user) throws TransfersServiceException {
		Transfers [] transfers = null;
		try {
			transfers = restTemplate.exchange(BASE_URL + "accounts/transfers/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(user), Transfers[].class).getBody();
	} catch (RestClientResponseException ex) {
			throw new TransfersServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
	}
	        return transfers;
	}
	
	// send a transfer
	public void sendTransfers (Transfers transfer, AuthenticatedUser user) throws TransfersServiceException {
		try {
	  restTemplate.postForObject(BASE_URL + "transfers/", makeTransferEntity(transfer, user), Transfers[].class);
	} catch (RestClientResponseException ex) {
			throw new TransfersServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
	}
	        
	}
	
	
	// gives a list of all the users
	
	
	
	
	
	
	
	
	
	
	
	
	
	private HttpEntity<Transfers> makeTransferEntity(Transfers transfer, AuthenticatedUser user) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(user.getToken());
		HttpEntity<Transfers> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

	private HttpEntity makeAuthEntity(AuthenticatedUser user) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(user.getToken());
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
}




