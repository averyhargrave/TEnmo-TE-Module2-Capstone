package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.TransferStatuses;
import com.techelevator.tenmo.model.Transfers;

public class JDBCTransferStatusesDAO implements TransferStatusesDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCTransferStatusesDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public TransferStatuses searchByTransferStatusId(Long id) {
		TransferStatuses transferStatus = new TransferStatuses();
		String sqlGetTransferStatusById = "SELECT transfer_status_id, transfer_status_desc " +
								          "FROM transfer_statuses " +
								          "WHERE transfer_status_id = ?";
		SqlRowSet transferStatusById = jdbcTemplate.queryForRowSet(sqlGetTransferStatusById, id);
		transferStatus = mapRowToTransferStatuses(transferStatusById);
		return transferStatus;
	}

	@Override
	public List<TransferStatuses> getAllTransferStatuses() {
		List<TransferStatuses> allTransferStatuses = new ArrayList<>();
		String sqlGetAllTransferStatuses = "SELECT transfer_status_id, transfer_status_desc " +
								    	   "FROM transfer_statuses";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransferStatuses);
		while (results.next()) {
			TransferStatuses sqlTransferStatuses = mapRowToTransferStatuses(results);
			allTransferStatuses.add(sqlTransferStatuses);
		}
		return allTransferStatuses;
	}

	@Override
	public void updateTransferStatus(TransferStatuses updatedTransferStatus) {
		// TODO Auto-generated method stub
		
	}

	private TransferStatuses mapRowToTransferStatuses(SqlRowSet results) {
		TransferStatuses transferStatuses = new TransferStatuses();
		transferStatuses.setTransferStatusId(results.getLong("transfer_status_id"));
		transferStatuses.setTransferStatusDesc(results.getString("transfer_status_desc"));
		return transferStatuses;
	}
	
}
