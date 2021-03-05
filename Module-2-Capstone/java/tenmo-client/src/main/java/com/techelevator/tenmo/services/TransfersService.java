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
import com.techelevator.view.ConsoleService;

public class TransfersService {
	
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	private ConsoleService consoleService = new ConsoleService(System.in, System.out);

	public TransfersService(String url) {
		BASE_URL = url;
	}

	public Transfers getTransferById(AuthenticatedUser user, Long id) throws TransfersServiceException {
		Transfers transfer;
		try {
			transfer = restTemplate.exchange(BASE_URL + "/transfers/" + id, HttpMethod.GET, makeAuthEntity(user), Transfers.class).getBody();
		} catch(RestClientResponseException ex) {
			throw new TransfersServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return transfer;
	}
	
	// list all transfers
	public Transfers[] listTransfers(AuthenticatedUser user) throws TransfersServiceException {
		Transfers[] transfers;
		try {
			transfers = restTemplate.exchange(BASE_URL + "accounts/transfers/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(user), Transfers[].class).getBody();
		} catch (RestClientResponseException ex) {
			throw new TransfersServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return transfers;
	}
	
	// list all pending transfers
	public Transfers[] getPendingTransfers(AuthenticatedUser user) throws TransfersServiceException {
		Transfers[] pendTrans = null;
		try {
			pendTrans = restTemplate.exchange(BASE_URL + "accounts/transfers/pending/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(user), Transfers[].class).getBody();
		} catch (RestClientResponseException ex) {
			throw new TransfersServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return pendTrans;
	}

	// send money
	public void sendBucks(AuthenticatedUser user) {
		User[] users;
		Transfers transfer = new Transfers();
		
		users = restTemplate.exchange(BASE_URL + "listusers", HttpMethod.GET, makeAuthEntity(user), User[].class).getBody();
		for(User i : users) {
			if(i.getId() != user.getUser().getId()) {
				System.out.println(i.getId() + ": " + i.getUsername());				
			}
		}
		transfer.setAccountTo(Long.parseLong(consoleService.getUserInput("Enter account ID of the recipient or press 0 to cancel")));
		transfer.setAccountFrom(user.getUser().getId());
		if(transfer.getAccountTo() != 0) {
			transfer.setAmount(new BigDecimal(Double.parseDouble(consoleService.getUserInput("Enter amount"))));
			String output = restTemplate.exchange(BASE_URL + "transfers", HttpMethod.POST, makeTransferEntity(transfer, user), String.class).getBody();
			System.out.println(output);			
		}
	}
	
	// request money
	public void requestBucks(AuthenticatedUser user) {
		User[] users = null;
		Transfers transfer = new Transfers();
		
		users = restTemplate.exchange(BASE_URL + "listusers", HttpMethod.GET, makeAuthEntity(user), User[].class).getBody();
		for(User i : users) {
			if(i.getId() != user.getUser().getId()) {
				System.out.println(i.getId() + ": " + i.getUsername());				
			}
		}
		transfer.setAccountTo(user.getUser().getId());
		transfer.setAccountFrom(Long.parseLong(consoleService.getUserInput("Enter account ID of the sender or press 0 to cancel")));
		if(transfer.getAccountTo() != 0) {
			transfer.setAmount(new BigDecimal(Double.parseDouble(consoleService.getUserInput("Enter amount"))));			
			String output = restTemplate.exchange(BASE_URL + "request", HttpMethod.POST, makeTransferEntity(transfer, user), String.class).getBody();
			System.out.println(output);			
		}
	}
	
	// list all users
	public User[] listUsers(AuthenticatedUser user) {
		User[] userArray = null;
		try {
			userArray = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(user), User[].class).getBody();
			for (User i : userArray) {
				System.out.println(i);
			}
		} catch (RestClientResponseException ex) {
			System.out.println("Error getting users");
		}
		return userArray;
	}
	
	
	
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