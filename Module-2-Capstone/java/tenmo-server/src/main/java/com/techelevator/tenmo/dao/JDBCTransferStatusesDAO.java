package com.techelevator.tenmo.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.TransferStatuses;

public class JDBCTransferStatusesDAO implements TransferStatusesDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCTransferStatusesDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public TransferStatuses searchByTransferStatusId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransferStatuses> getAllTransferStatuses() {
		// TODO Auto-generated method stub
		return null;
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
