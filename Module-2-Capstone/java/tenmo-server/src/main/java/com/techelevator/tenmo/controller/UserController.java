package com.techelevator.tenmo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techelevator.tenmo.dao.UserDAO;


public class UserController {

		UserDAO userDAO;

 public UserController (UserDAO userDAO) {
	 this.userDAO = userDAO;
 }
		




public void logAPICall(String message) {    // write a message with a time stamp to the server log 
	 LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm/dd/yyyy HH:mm:ss.A");
    String timeNow = now.format(formatter);
    System.out.println(timeNow + "-" + message);
}
}