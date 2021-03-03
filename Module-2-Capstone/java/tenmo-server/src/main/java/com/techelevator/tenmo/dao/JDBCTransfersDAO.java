package com.techelevator.tenmo.dao;

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
		
	@Override
	public Transfers createTransfers(Transfers newTransfer) {
		String sqlInsertTransfer = "INSERT INTO transfers(transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
					 			   "VALUES(?,?,?,?,?,?)"; 
		newTransfer.setTransferId(getNextTransferId());
		jdbcTemplate.update(sqlInsertTransfer, newTransfer.getTransferId(), newTransfer.getTransferTypeId(), newTransfer.getTransferStatusId(), newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());
		return newTransfer;
	}

	@Override
	public Transfers searchByTransferId(Long id) {
		Transfers transfer = new Transfers();
		String sqlGetTransferById = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
								    "FROM transfers " +
									"WHERE transfer_id = ?";
		SqlRowSet TransferById = jdbcTemplate.queryForRowSet(sqlGetTransferById, id);
		transfer = mapRowToTransfers(TransferById);
		return transfer;
	}

	@Override
	public List<Transfers> getAllTransfers() {
		List<Transfers> allTransfers = new ArrayList<>();
		String sqlGetAllTransfers = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
								    "FROM transfers";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransfers);
		while (results.next()) {
			Transfers sqlTransfers = mapRowToTransfers(results);
			allTransfers.add(sqlTransfers);
		}
		return allTransfers;
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
		transfer.setAccountFrom(results.getInt("account_from"));
		transfer.setAccountTo(results.getInt("account_to"));
		transfer.setAmount(results.getDouble("amount"));
		return transfer;
	}
	
	private long getNextTransferId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_transfer_id')");
		if(nextIdResult.next()) {               
			return nextIdResult.getLong(1);     
		} else {                                
			throw new RuntimeException("Something went wrong while getting an id for the new transfer");
		}
	}
	
}
