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

	public JDBCTransfersDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	AccountsDAO acctDAO;
		
	@Override
	public List<Transfers> getAllTransfers(Long id) {
		List<Transfers> list = new ArrayList<>();
		String sqlGetAllTransfers = "SELECT transfers.*, user1.username AS sender, user2.username AS recipient " +
				 					"FROM transfers " + 
									"JOIN accounts acct1 " +
									"ON transfers.account_from = acct1.account_id " + 
									"JOIN accounts acct2 " +
									"ON transfers.account_to = acct2.account_id " + 
									"JOIN users user1 " +
									"ON acct1.user_id = user1.user_id " + 
									"JOIN users user2 " +
									"ON acct2.user_id = user2.user_id " + 
									"WHERE acct1.user_id = ? " +
									"OR acct2.user_id = ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransfers, id, id);
		while (results.next() ) {
			Transfers transfer = mapRowToTransfers(results);
			list.add(transfer);
		}
		return list;
	}

	@Override
	public Transfers getTransferById(Long id) {
		Transfers transfer = new Transfers();
		String sqlGetTransferById = "SELECT transfers.*, user1.username AS sender, user2.username AS recipient, transfer_statuses.transfer_status_desc, trasnfer_types.transfer_type_desc " +
									"FROM transfers " + 
								    "JOIN accounts acct1 " +
								    "ON transfers.account_from = acct1.account_id " + 
								    "JOIN accounts acct2 " +
								    "ON transfers.account_to = acct2.account_id " + 
								    "JOIN users user1 " +
								    "ON acct1.user_id = user1.user_id " + 
								    "JOIN users user2 " +
								    "ON acct2.user_id = user2.user_id " + 
								    "JOIN transfer_statuses " +
								    "ON transfers.transfer_status_id = transfer_statuses.transfer_status_id " + 
								    "JOIN transfer_types " +
								    "ON transfers.transfer_type_id = transfer_types.transfer_type_id " + 
								    "WHERE transfers.transfer_id = ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferById, id);
		if (results.next()) {
			transfer = mapRowToTransfers(results);
		}
		return transfer;
	}

	@Override
	public String sendTransfer(Long sender, Long recipient, BigDecimal amount) {
		if (sender == recipient) {
			return "No embezzling on my watch!";
		}
		// some sort of if statement, if the sender has more money than the transfer amount, the transaction succeeds
		Accounts acct = new Accounts();
		acct = acctDAO.findByAccountId(sender);
		double bal = acct.getBalance().doubleValue();
		if(bal < amount.doubleValue()) {
			String sqlSendTransfer = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " + 
						 "VALUES(2,2,?,?,?) ";
			jdbcTemplate.update(sqlSendTransfer, sender, recipient, amount);
			acctDAO.addBalance(amount, recipient);
			acctDAO.minusBalance(amount, sender);		
			return "Transfer finished.";	
	} else {
		return "Sorry, zucchini, you ain't got the dough.";
		}
	}
	@Override
	public String requestTransfer(Long sender, Long recipient, BigDecimal amount) {
		if (sender == recipient) {
			return "You can't send money to yourself, knucklehead.";
		}
		// an if statement to check if sender acct has enough money to send requested amount
		Accounts acct = new Accounts();
		acct = acctDAO.findByAccountId(sender);
		double bal = acct.getBalance().doubleValue();
		if(bal < amount.doubleValue()) {
			String sqlRequestTransfer = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " + 
									    "VALUES(1,1,?,?,?) ";
			jdbcTemplate.update(sqlRequestTransfer, sender, recipient, amount);
			return "Request hath been sent.";
	} else {
		return "Couldn't process your request, but have some meatballs instead.";
		}
	}
	
	@Override
	public List<Transfers> getPendingRequests(Long id) {
		List<Transfers> transfers = new ArrayList<>();
		String sqlGetPendRequests = "SELECT transfers.*, user1.username AS sender, user2.username AS recipient " +
									"FROM transfers " + 
									"JOIN accounts acct1 " +
									"ON transfers.account_from = acct1.account_id " + 
									"JOIN accounts acct2 " +
									"ON transfers.account_to = acct2.account_id " + 
									"JOIN users user1 " +
									"ON acct1.user_id = user1.user_id " + 
									"JOIN users user2 " +
									"ON acct2.user_id = user2.user_id " + 
									"WHERE transfer_status_id = 1 " +
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
