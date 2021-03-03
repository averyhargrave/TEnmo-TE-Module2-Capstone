package com.techelevator.tenmo.dao;

import java.util.List;


import com.techelevator.tenmo.model.TransferTypes;

public interface TransferTypesDAO {
	
	    // update, read
	
		// read
		public TransferTypes searchByTransferTypeId (Long id);
		
		public List<TransferTypes> getAllTransferTypes ();
		
		//update
		public void updateTransferType (TransferTypes updatedTransferType);

}
