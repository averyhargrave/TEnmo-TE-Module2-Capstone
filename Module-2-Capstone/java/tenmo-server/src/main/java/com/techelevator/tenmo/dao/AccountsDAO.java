package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Accounts;

public interface AccountsDAO {
	
	// create 
	public Accounts createAccount(Accounts account);
	
	// read
	public Accounts searchByAccountId(Long id);
	// getAllAccounts
	public List<Accounts> getAllAccounts();

	// update 
	public void updateAccountBalance(Accounts account, double balance);
	
	// delete 
	public void deleteAccount(Long id);
	
}
