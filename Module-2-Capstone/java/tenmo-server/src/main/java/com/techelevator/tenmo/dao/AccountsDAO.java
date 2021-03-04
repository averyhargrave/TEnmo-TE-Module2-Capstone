package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Accounts;

public interface AccountsDAO {
	
	// Read
	public BigDecimal getBalance(Long id);
	
	public Accounts findByUserId(Long id);
	
	public Accounts findByAccountId(Long id);
	
	
	// Update
	public BigDecimal addBalance(BigDecimal amountToAdd, Long id);
	
	public BigDecimal minusBalance(BigDecimal amountToSubtract, Long id);
	
	// Delete
	public void deleteAccount(Long accountId);

}
