package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfers;

public interface TransfersDAO {
	
	// compare to Zelle transfer
	// send request 
	// receive request 
	// history of transfers
	// search person by name, phone, user-name 
	
	// Read
	public List<Transfers> getAllTransfers(Long id);
	
	public Transfers getTransferById(Long id);
	
	public List<Transfers> getPendingRequests(Long id);
	
	// Update
	public String sendTransfer(Long sender, Long recipient, BigDecimal amount);
	
	public String requestTransfer(Long sender, Long recipient, BigDecimal amount);
	
	public String updateTransferRequest(Transfers transfer, Long statusId);
	
	// Delete
	public void deleteTransfer(Long id);
	
}
