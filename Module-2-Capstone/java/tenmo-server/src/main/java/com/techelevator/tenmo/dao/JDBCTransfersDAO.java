package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
@Component
public class JDBCTransfersDAO implements TransfersDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCTransfersDAO(DataSource dataSource, AccountsDAO acctDAO) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.acctDAO =acctDAO;
	}
	
	AccountsDAO acctDAO;
		
	@Override
	public List<Transfers> getAllTransfers(Long id) {
		List<Transfers> list = new ArrayList<>();
		String sqlGetAllTransfers = "SELECT * " +
				 					"FROM transfers " +  
									"WHERE account_from = ? " +
				 					"OR account_to = ? ";
									
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransfers, id, id);
		while(results.next()) {
			Transfers transfer = mapRowToTransfers(results);
			list.add(transfer);
		}
		return list;
	}

	@Override
	public Transfers getTransferById(Long id) {
		Transfers transfer = new Transfers();
		String sqlGetTransferById = "SELECT * " +
									"FROM transfers " + 
								    "WHERE transfer_id = ? ";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferById, id);
		if (results.next()) {
			transfer = mapRowToTransfers(results);
		}
		return transfer;
	}

	@Override
	public String sendTransfer(Long sender, Long recipient, BigDecimal amount) {
		if (sender == recipient) {
			return "Are you seriously trying to launder money right now?";
		}
		// some sort of if statement, if the sender has more money than the transfer amount, the transaction succeeds
		Accounts acct = new Accounts();
		acct = acctDAO.findByAccountId(sender);
		double bal = acct.getBalance().doubleValue();
		if(bal > amount.doubleValue()) {
			String sqlSendTransfer = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " + 
						             "VALUES(2,2,?,?,?) ";
			jdbcTemplate.update(sqlSendTransfer, sender, recipient, amount);
			acctDAO.addBalance(amount, recipient);
			acctDAO.minusBalance(amount, sender);		
			return "You have successfully sent more than enough money to buy a Diet Mtn Dew.";	
	} else {
		return "You don't have enough for this transfer, but you might be able to afford a Diet Mtn Dew.";
		}
	}
	@Override
	public String requestTransfer(Long sender, Long recipient, BigDecimal amount) {
		if (sender == recipient) {
			return "You can't request money from yourself, knucklehead.";
		}
		// an if statement to check if sender acct has enough money to send requested amount
		Accounts acct = new Accounts();
		acct = acctDAO.findByAccountId(sender);
		double bal = acct.getBalance().doubleValue();
		if(bal > amount.doubleValue()) {
			String sqlRequestTransfer = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " + 
									    "VALUES(1,1,?,?,?) ";
			jdbcTemplate.update(sqlRequestTransfer, sender, recipient, amount);
			return "Jason hasn't stolen this money yet, so your request has posted.";
	} else {
		return "It seems as though Jason has stolen all of the money from this account.";
		}
	}
	
	@Override
	public List<Transfers> getPendingRequests(Long id) {
		List<Transfers> transfers = new ArrayList<>();
		String sqlGetPendRequests = "SELECT transfers.* " +
									"FROM transfers " + 
									"JOIN transfer_statuses " + 
									"ON transfers.transfer_status_id = transfer_statuses.transfer_status_id " +
									"WHERE transfers.transfer_status_id = 1 " +
									"AND (account_from = ? OR account_to = ?) ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetPendRequests, id, id);
		while (results.next()) {
			Transfers transfer = mapRowToTransfers(results);
			transfers.add(transfer);
		}
		return transfers;
	}
	
	@Override
	public String updateTransferRequest(Transfers transfer, Long id) {
		if (id == 3) {
			String sqlUpdateTransfer = "UPDATE transfers " +
						 			   "SET transfer_status_id = ? " +
						 			   "WHERE transfer_id = ? ";
			jdbcTemplate.update(sqlUpdateTransfer, id, transfer.getTransferId());
			return "Update completed!";
		}
		// have to find the balance in the acct
		// we have to find the account for the fromAccount id and check the balance to see if it's negative
		Accounts acct = new Accounts();
		acct = acctDAO.findByAccountId(transfer.getAccountFrom());
		double bal = acct.getBalance().doubleValue();
		if((bal - transfer.getAmount().doubleValue()) >= 0.00) {
			String sqlUpdateTransfer = "UPDATE transfers " +
									   "SET transfer_status_id = ? " +
									   "WHERE transfer_id = ? ";
			jdbcTemplate.update(sqlUpdateTransfer, id, transfer.getTransferId());
			acctDAO.addBalance(transfer.getAmount(), transfer.getAccountTo());
			acctDAO.minusBalance(transfer.getAmount(), transfer.getAccountFrom());
			return "Update completed!";		
		}
		else { 
			return "If you ain't got no money, take your broke @$$ home!";
		}
	}

	@Override
	public void deleteTransfer(Long id) {
		String deleteTransfer = "DELETE " +
								"FROM transfers " +
								"WHERE transfer_id = ? ";
		jdbcTemplate.update(deleteTransfer, id);
	}

	private Transfers mapRowToTransfers(SqlRowSet results) {
		Transfers transfer = new Transfers();
		transfer.setTransferId(results.getLong("transfer_id"));
		transfer.setTransferTypeId(results.getLong("transfer_type_id"));
		transfer.setTransferStatusId(results.getLong("transfer_status_id"));
		transfer.setAccountFrom(results.getLong("account_from"));
		transfer.setAccountTo(results.getLong("account_to"));
		transfer.setAmount(results.getBigDecimal("amount"));
		return transfer;
	}
}
