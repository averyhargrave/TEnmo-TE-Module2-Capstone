package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfers;

public interface TransfersDAO {
	
	// CRUD
	
	//create
	public Transfers createTransfers (Transfers newTransfer);
	
	// read
	public Transfers searchByTransferId (Long id);

	// get all transfers
	public List<Transfers> getAllTransfers ();
	
	// delete 
	public void deleteTransfer (Long id);
}
