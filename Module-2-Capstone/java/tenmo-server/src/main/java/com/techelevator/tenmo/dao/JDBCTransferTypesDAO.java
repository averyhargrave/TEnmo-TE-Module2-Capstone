package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.TransferStatuses;
import com.techelevator.tenmo.model.TransferTypes;

public class JDBCTransferTypesDAO implements TransferTypesDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCTransferTypesDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
		
	@Override
	public TransferTypes searchByTransferTypeId(Long id) {
		TransferTypes transferType = new TransferTypes();
		String sqlGetTransferTypeById = "SELECT transfer_type_id, transfer_type_desc " +
								        "FROM transfer_types " +
								        "WHERE transfer_type_id = ?";
		SqlRowSet transferTypeById = jdbcTemplate.queryForRowSet(sqlGetTransferTypeById, id);
		transferType = mapRowToTransferTypes(transferTypeById);
		return transferType;
	}

	@Override
	public List<TransferTypes> getAllTransferTypes() {
		List<TransferTypes> allTransferTypes = new ArrayList<>();
		String sqlGetAllTransferTypes = "SELECT transfer_type_id, transfer_type_desc " +
								        "FROM transfer_types";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransferTypes);
		while (results.next()) {
			TransferTypes sqlTransferTypes = mapRowToTransferTypes(results);
			allTransferTypes.add(sqlTransferTypes);
		}
		return allTransferTypes;
	}
	@Override
	public void updateTransferType(TransferTypes updatedTransferType) {
		// TODO Auto-generated method stub
		
	}
	
	private TransferTypes mapRowToTransferTypes(SqlRowSet results) {
		TransferTypes transferType = new TransferTypes();
		transferType.setTransferTypeId(results.getLong("transfer_type_id"));
		transferType.setTransferTypeDesc(results.getString("transfer_type_desc"));
		return transferType;
	}

}
