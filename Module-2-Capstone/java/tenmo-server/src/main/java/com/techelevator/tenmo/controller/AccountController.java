package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.User;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
	
		AccountsDAO acctDAO;
		private UserDAO userDAO;
		
		public AccountController (AccountsDAO acctDAO, UserDAO userDAO) {
			this.acctDAO = acctDAO;
			this.userDAO = userDAO;
		}

		@RequestMapping(path = "balance/{id}", method = RequestMethod.GET)
		public BigDecimal getBalance(@PathVariable Long id) {
			logAPICall("Called with the path: /balance" + id);
			BigDecimal balance = acctDAO.getBalance(id);
			return balance;
		}

		@RequestMapping(path = "listusers", method = RequestMethod.GET)
		public List<User> listUsers() {
			logAPICall("Called with the path: /listusers");
			List<User> users = userDAO.findAll();
			return users;
		}
		
		public void logAPICall(String message) {    // write a message with a time stamp to the server log 
			 LocalDateTime now = LocalDateTime.now();
		     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm/dd/yyyy HH:mm:ss.A");
		     String timeNow = now.format(formatter);
		     System.out.println(timeNow + "-" + message);
		 }
	}