package com.techelevator.tenmo.controller;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.model.Transfers;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransfersController {

	public TransfersController (TransfersDAO transDAO) {
		this.transfersDAO = transDAO;
	}

	private TransfersDAO transfersDAO;

	@RequestMapping(value = "/accounts/transfers/{id}", method = RequestMethod.GET)
	public List<Transfers> getAllTransfersById(@PathVariable Long id) {
		logAPICall("Called with the path: /accounts/transfers/" + id);
		List<Transfers> results = transfersDAO.getAllTransfers(id);
		return results;
	}

	@RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
	public Transfers getTransferById(@PathVariable Long id) {
		logAPICall("Called with the path: /transfers/" + id);
		Transfers transfer = transfersDAO.getTransferById(id);
		return transfer;
	}

	
	
	@RequestMapping(path = "/transfers", method = RequestMethod.POST)
	public String sendTransfer(@RequestBody Transfers transfer) {
		logAPICall("Called with the path: /transfers");
		String results = transfersDAO.sendTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
		return results;
	}

	@RequestMapping(path = "/request", method = RequestMethod.POST)
	public String requestTransfer(@RequestBody Transfers transfer) {
		logAPICall("Called with the path: /request");
		String results = transfersDAO.requestTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
		return results;
	}

	@RequestMapping(value = "/request/{id}", method = RequestMethod.GET)
	public List<Transfers> getAllTransferRequests(@PathVariable Long id) {
		logAPICall("Called with the path: /request/" + id);
		List<Transfers> results = transfersDAO.getPendingRequests(id);
		return results;
	}

	@RequestMapping(path = "/transfers/status/{id}", method = RequestMethod.PUT)
	public String updateRequest(@RequestBody Transfers transfer, @PathVariable Long id) {
		logAPICall("Called with the path: /transfers/status/" + id);
		String results = transfersDAO.updateTransferRequest(transfer, id);
		return results;
	}

	public void logAPICall(String message) {    // write a message with a time stamp to the server log 
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm/dd/yyyy HH:mm:ss.A");
		String timeNow = now.format(formatter);
		System.out.println(timeNow + "-" + message);
	}
}