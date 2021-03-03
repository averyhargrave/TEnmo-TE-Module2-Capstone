package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;

public class JDBCAccountsDAO implements AccountsDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCAccountsDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Accounts createAccount(Accounts account) {
		String sqlInsertAccount = "INSERT INTO accounts(account_id, user_id, balance) " +
	 			   				   "VALUES(?,?,?)"; 
		account.setAccountId(getNextAccountId());
		jdbcTemplate.update(sqlInsertAccount, account.getAccountId(), account.getUserId(), account.getBalance());
		return account;
	}

	@Override
	public Accounts searchByAccountId(Long id) {
		Accounts account = new Accounts();
		String sqlGetAccountById = "SELECT account_id, user_id, balance " +
								   "FROM accounts " +
								   "WHERE account_id = ?";
		SqlRowSet AccountById = jdbcTemplate.queryForRowSet(sqlGetAccountById, id);
		account = mapRowToAccounts(AccountById);
		return account;
	}

	@Override
	public List<Accounts> getAllAccounts() {
		List<Accounts> allAccounts = new ArrayList<>();
		String sqlGetAllAccounts = "SELECT account_id, user_id, balance " +
								   "FROM accounts";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllAccounts);
		while (results.next()) {
			Accounts sqlAccounts = mapRowToAccounts(results);
			allAccounts.add(sqlAccounts);
		}
		return allAccounts;
	}

	@Override
	public void updateAccountBalance(Accounts account, double balance) {
		String updateAccount = "UPDATE accounts " +
							   "SET balance = ?" +
				               "WHERE account_id = ?";
		account.setBalance(balance);
		jdbcTemplate.update(updateAccount, account.getBalance(), account.getAccountId());
	}

	@Override
	public void deleteAccount(Long id) {
		String deleteAccount = "DELETE " +
							   "FROM accounts " +
							   "WHERE account_id = ? ";
		jdbcTemplate.update(deleteAccount, id);
	}
	
	private Accounts mapRowToAccounts(SqlRowSet results) {
		Accounts account = new Accounts();
		account.setAccountId(results.getLong("account_id"));
		account.setUserId(results.getLong("user_id"));
		account.setBalance(results.getDouble("balance"));
		return account;
	}
	
	private long getNextAccountId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_account_id')");
		if(nextIdResult.next()) {               
			return nextIdResult.getLong(1);     
		} else {                                
			throw new RuntimeException("Something went wrong while getting an id for the new account");
		}
	}
}
