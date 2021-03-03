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

/*******************************************************************************************************
 * This is where you code any API controllers you may create
********************************************************************************************************/
@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
		AccountsDAO acctDAO;
		private UserDAO userDAO;
		
		public AccountController (AccountsDAO acctDAO, UserDAO userDAO) {
			this.acctDAO = acctDAO;
			this.userDAO = userDAO;
		}
		

		@RequestMapping (path= "/accounts",method = RequestMethod.GET) 
		public List<Accounts> listAllAccounts () {
			logAPICall("Called with the path: /accounts");   // log the API call to the server
			List <Accounts> theAccounts;
			theAccounts = acctDAO.getAllAccounts();
			return theAccounts;
		}

		@RequestMapping(path = "balance/{id}", method = RequestMethod.GET)
		public BigDecimal getBalance(@PathVariable Long id) {
			logAPICall("Called with the path: /accounts/" + id);
			BigDecimal balance = acctDAO.getAccountBalance(id);
			return balance;
		}

		public void logAPICall(String message) {    // write a message with a time stamp to the server log 
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm/dd/yyyy HH:mm:ss.A");
			String timeNow = now.format(formatter);
			System.out.println(timeNow + "-" + message);
		}
}

	