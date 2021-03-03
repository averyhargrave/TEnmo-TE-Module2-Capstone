package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

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
		String sqlGetAllTransfers = "SELECT transfers.*, u.username AS sender, v.username AS recipient FROM transfers" + 
									"JOIN accounts a ON transfers.account_from = a.account_id " + 
									"JOIN accounts b ON transfers.account_to = b.account_id " + 
									"JOIN users u ON a.user_id = u.user_id " + 
									"JOIN users v ON b.user_id = v.user_id " + 
									"WHERE a.user_id = ? OR b.user_id = ?";
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
		String sqlGetTransferById = "SELECT transfers.*, u.username AS sender, v.username AS recipient, transfer_statuses.transfer_status_desc, trasnfer_types.transfer_type_desc FROM transfers " + 
								    "JOIN accounts a ON t.account_from = a.account_id " + 
								    "JOIN accounts b ON t.account_to = b.account_id " + 
								    "JOIN users u ON a.user_id = u.user_id " + 
								    "JOIN users v ON b.user_id = v.user_id " + 
								    "JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id " + 
								    "JOIN transfer_types ON transfers.transfer_type_id = transfer_types.transfer_type_id " + 
								    "WHERE transfers.transfer_id = ?";
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
		if (amount.compareTo(acctDAO.getBalance(sender)) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
			String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " + 
						 "VALUES(2,2,?,?,?)";
			jdbcTemplate.update(sql, sender, recipient, amount);
			acctDAO.addBalance(amount, recipient);
			acctDAO.minusBalance(amount, sender);		
			return "Transfer finished.";
		} else {
			return "You might be poor or something, idk lol.";
		}
	}
	
	@Override
	public String requestTransfer(Long sender, Long recipient, BigDecimal amount) {
		if (sender == recipient) {
			return "Yeah right, nice try, bozo.";
		}
		if (amount.compareTo(new BigDecimal(0)) == 1) {
			String sqlRequestTransfer = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " + 
									    "VALUES(1,1,?,?,?)";
			jdbcTemplate.update(sqlRequestTransfer, sender, recipient, amount);
			return "Request hath been sent.";
		} else {
			return "Could not send the request, but some meatballs could make you feel better.";			
		}
	}
	
	@Override
	public List<Transfers> getPendingRequests(Long id) {
		List<Transfers> transfers = new ArrayList<>();
		String sqlGetPendRequests = "SELECT t.*, u.username AS userFrom, v.username AS userTo FROM transfers t " + 
									"JOIN accounts a ON t.account_from = a.account_id " + 
									"JOIN accounts b ON t.account_to = b.account_id " + 
									"JOIN users u ON a.user_id = u.user_id " + 
									"JOIN users v ON b.user_id = v.user_id " + 
									"WHERE transfer_status_id = 1 AND (account_from = ? OR account_to = ?)";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetPendRequests, id, id);
		while (results.next()) {
			Transfers transfer = mapRowToTransfers(results);
			transfers.add(transfer);
		}
		return transfers;
	}
	
	@Override
	public String updateTransferRequest(Transfers transfer, Long statusId) {
		if (statusId == 3) {
			String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
			jdbcTemplate.update(sql, statusId, transfer.getTransferId());
			return "Update complete!";	
		}
		if (!(acctDAO.getBalance(transfer.getAccountFrom()).compareTo(transfer.getAmount()) == -1)) {
			String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?;";
			jdbcTemplate.update(sql, statusId, transfer.getTransferId());
			acctDAO.addBalance(transfer.getAmount(), transfer.getAccountTo());
			acctDAO.minusBalance(transfer.getAmount(), transfer.getAccountFrom());
			return "Update completed!";			
		} else {
			return "Insufficient funds";
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
