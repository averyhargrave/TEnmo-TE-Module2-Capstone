package com.techelevator.tenmo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techelevator.tenmo.dao.TransferTypesDAO;
import com.techelevator.tenmo.model.TransferTypes;

public class TransferTypesController {
	
		TransferTypesDAO transTypeDAO;
	
 public TransferTypesController (TransferTypesDAO transTypeDAO) {
	 this.transTypeDAO = transTypeDAO;
 }

 
 @RequestMapping (path= "/transfertypes",method = RequestMethod.GET) 
 public List<TransferTypes> listAllTransfers () {
 	logAPICall("Called with the path: /transferstatuses");   // log the API call to the server
 	List <TransferTypes> theTransferTypes;
 	
 	theTransferTypes = transTypeDAO.getAllTransferTypes();
 	
 	return theTransferTypes;
 	
 }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public void logAPICall(String message) {    // write a message with a time stamp to the server log 
	 LocalDateTime now = LocalDateTime.now();
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm/dd/yyyy HH:mm:ss.A");
     String timeNow = now.format(formatter);
     System.out.println(timeNow + "-" + message);
 }
}
