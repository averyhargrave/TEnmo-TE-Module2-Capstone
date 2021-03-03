package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.TransferStatuses;

public interface TransferStatusesDAO {
	
	// update, read
	
	// read
	public TransferStatuses searchByTransferStatusId (Long id);
	
	public List<TransferStatuses> getAllTransferStatuses ();
	
	//update
	public void updateTransferStatus (TransferStatuses updatedTransferStatus);
	
	
	
	

}
