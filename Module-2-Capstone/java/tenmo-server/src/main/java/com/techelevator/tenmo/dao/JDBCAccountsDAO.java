package com.techelevator.tenmo.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Accounts;

public class JDBCAccountsDAO implements AccountsDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCAccountsDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Accounts createAccount(Accounts account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Accounts searchByAccountId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Accounts> getAllAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAccount(Accounts updatedAccount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAccount(Long id) {
		// TODO Auto-generated method stub
		
	}

	private Accounts mapRowToAccounts(SqlRowSet results) {
		Accounts account = new Accounts();
		account.setAccountId(results.getLong("account_id"));
		account.setUserId(results.getLong("user_id"));
		account.setBalance(results.getDouble("balance"));
		return account;
	}
	
}
