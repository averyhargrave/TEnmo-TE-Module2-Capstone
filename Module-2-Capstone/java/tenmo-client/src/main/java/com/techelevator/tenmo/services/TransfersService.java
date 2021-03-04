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

public class TransfersService {
	
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();

	public TransfersService(String url) {
		BASE_URL = url;
	}

	// list all transfers
	public Transfers[] transfersList(AuthenticatedUser user) throws TransfersServiceException {
		Transfers[] transfers = null;
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

	public void sendBucks(AuthenticatedUser user) {
		User[] users;
		Transfers transfer = new Transfers();
		Scanner input = new Scanner(System.in);
		users = restTemplate.exchange(BASE_URL + "listusers", HttpMethod.GET, makeAuthEntity(user), User[].class).getBody();
		for (User i : users) {
			if (i.getId() != user.getUser().getId()) {
				System.out.println(i.getId() + ": " + i.getUsername());				
			}
		}
		System.out.print("Enter account ID of the recipient or press 0 to cancel: ");
		transfer.setAccountTo(Long.parseLong(input.nextLine()));
		transfer.setAccountFrom(user.getUser().getId());
		if (transfer.getAccountTo() != 0) {
			System.out.print("Enter amount: ");
			transfer.setAmount(new BigDecimal(Double.parseDouble(input.nextLine())));
			System.out.println("Error when entering amount");
			String output = restTemplate.exchange(BASE_URL + "transfers", HttpMethod.POST, makeTransferEntity(transfer, user), String.class).getBody();
			System.out.println(output);			
		}
		input.close();
	}

	
	// gives a list of all the users
	public User[] getUsers(AuthenticatedUser user) {
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




