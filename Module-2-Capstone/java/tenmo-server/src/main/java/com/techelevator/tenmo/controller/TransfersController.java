package com.techelevator.tenmo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.model.Transfers;

@RestController
public class TransfersController {
	
		TransfersDAO transDAO;
	
 public TransfersController (TransfersDAO transDAO) {
	 this.transDAO = transDAO;
 }

 @RequestMapping (path= "/transfers",method = RequestMethod.GET) 
 public List<Transfers> listAllTransfers () {
 	logAPICall("Called with the path: /transfers");   // log the API call to the server
 	List <Transfers> theTransfers;
 	
 	theTransfers = transDAO.getAllTransfers();
 	
 	return theTransfers;
 	
 }



public void logAPICall(String message) {    // write a message with a time stamp to the server log 
	 LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm/dd/yyyy HH:mm:ss.A");
    String timeNow = now.format(formatter);
    System.out.println(timeNow + "-" + message);
}
}