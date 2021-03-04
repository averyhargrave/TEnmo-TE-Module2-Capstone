package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
@Component
public class JDBCAccountsDAO implements AccountsDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCAccountsDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public BigDecimal getBalance(Long id) {
		String sqlGetBal = "SELECT balance " +
						   "FROM accounts " +
						   "WHERE user_id = ? ";
		BigDecimal bal = null;
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetBal, id);
		if (results.next()) {
			bal = results.getBigDecimal("balance");
		}
		return bal;
	}

	@Override
	public BigDecimal addBalance(BigDecimal amountToAdd, Long id) {
		Accounts acct = findByAccountId(id);
		BigDecimal newBal = acct.getBalance().add(amountToAdd);
		System.out.println(newBal);
		String sqlAddBal = "UPDATE accounts " +
						   "SET balance = ? " +
						   "WHERE user_id = ? ";
		jdbcTemplate.update(sqlAddBal, newBal, id);
		return acct.getBalance();
	}

	@Override
	public BigDecimal minusBalance(BigDecimal amountToSubtract, Long id) {
		Accounts account = findByAccountId(id);
		BigDecimal minusBal = account.getBalance().subtract(amountToSubtract);
		String sqlMinusBal = "UPDATE accounts " +
						     "SET balance = ? " +
						     "WHERE user_id = ? ";
		jdbcTemplate.update(sqlMinusBal, minusBal, id);
		return account.getBalance();
	}

	@Override
	public Accounts findByUserId(Long id) {
		Accounts acct = new Accounts();
		String sqlFindByUserId = "SELECT * " +
								 "FROM accounts " +
								 "WHERE user_id = ? ";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlFindByUserId, id);
		acct = mapRowToAccounts(result);
		return acct;
	}

	@Override
	public Accounts findByAccountId(Long id) {
		Accounts acct = new Accounts();
		String sqlFindByAcctId = "SELECT * " +
								 "FROM accounts " +
								 "WHERE account_id = ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindByAcctId, id);
		if (results.next()) {
			acct = mapRowToAccounts(results);
		}
		return acct;
	}

	@Override
	public void deleteAccount(Long id) {
		String deleteAcct = "DELETE " +
							"FROM accounts " +
							"WHERE account_id = ? ";
		jdbcTemplate.update(deleteAcct, id);
	}

	private Accounts mapRowToAccounts(SqlRowSet results) {
		Accounts account = new Accounts();
		account.setAccountId(results.getLong("account_id"));
		account.setUserId(results.getLong("user_id"));
		account.setBalance(results.getBigDecimal("balance"));
		return account;
	}


	
}
