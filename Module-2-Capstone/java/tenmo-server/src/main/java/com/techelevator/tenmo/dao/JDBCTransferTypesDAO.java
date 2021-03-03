package com.techelevator.tenmo.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.tenmo.model.TransferTypes;

public class JDBCTransferTypesDAO implements TransferTypesDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCTransferTypesDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
		
	@Override
	public TransferTypes searchByTransferTypeId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransferTypes> getAllTransferTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateTransferType(TransferTypes updatedTransferType) {
		// TODO Auto-generated method stub
		
	}
	
	private TransferTypes mapRowToDepartment(SqlRowSet results) {
		TransferTypes transferType = new TransferTypes();
		transferType.setTransferTypeId(results.getLong("transfer_type_id"));
		transferType.setTransferTypeDesc(results.getString("transfer_type_desc"));
		return transferType;
	}

}
