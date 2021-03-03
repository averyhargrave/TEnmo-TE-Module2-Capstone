package com.techelevator.tenmo.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Transfers;

public class JDBCTransfersDAO implements TransfersDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCTransfersDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
		
	@Override
	public Transfers createTransfers(Transfers newTransfer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateTransfer(Transfers updatedTransfer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Transfers searchByTransferId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transfers> getAllTransfers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTransfer(Long id) {
		// TODO Auto-generated method stub
		
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
	
}
