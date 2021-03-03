package com.techelevator.tenmo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techelevator.tenmo.dao.TransferStatusesDAO;
import com.techelevator.tenmo.model.TransferStatuses;
import com.techelevator.tenmo.model.Transfers;



public class TransferStatusesController {
	
		TransferStatusesDAO transStatDAO;
	
public TransferStatusesController (TransferStatusesDAO transStatDAO) {	
	this.transStatDAO = transStatDAO;
}


@RequestMapping (path= "/transferstatuses",method = RequestMethod.GET) 
public List<TransferStatuses> listAllTransfers () {
	logAPICall("Called with the path: /transferstatuses");   // log the API call to the server
	List <TransferStatuses> theTransferStats;
	
	theTransferStats = transStatDAO.getAllTransferStatuses();
	
	return theTransferStats;
	
}




public void logAPICall(String message) {    // write a message with a time stamp to the server log 
	 LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm/dd/yyyy HH:mm:ss.A");
    String timeNow = now.format(formatter);
    System.out.println(timeNow + "-" + message);
}
}
