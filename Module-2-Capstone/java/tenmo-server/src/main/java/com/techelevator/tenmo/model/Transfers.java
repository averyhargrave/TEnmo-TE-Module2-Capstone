package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfers {

	private long transferId;
	private long transferTypeId;
	private long transferStatusId;
	private Long accountFrom;
	private Long accountTo;
	private BigDecimal amount;
	
	public long getTransferId() {
		return transferId;
	}
	public void setTransferId(long transferId) {
		this.transferId = transferId;
	}
	public long getTransferTypeId() {
		return transferTypeId;
	}
	public void setTransferTypeId(long transferTypeId) {
		this.transferTypeId = transferTypeId;
	}
	public long getTransferStatusId() {
		return transferStatusId;
	}
	public void setTransferStatusId(long transferStatusId) {
		this.transferStatusId = transferStatusId;
	}
	public Long getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(Long accountFrom) {
		this.accountFrom = accountFrom;
	}
	public Long getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(Long accountTo) {
		this.accountTo = accountTo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "Transfers [transferId=" + transferId + ", transferTypeId=" + transferTypeId + ", transferStatusId="
				+ transferStatusId + ", accountFrom=" + accountFrom + ", accountTo=" + accountTo + ", amount=" + amount
				+ "]";
	}
	
	
	
}
