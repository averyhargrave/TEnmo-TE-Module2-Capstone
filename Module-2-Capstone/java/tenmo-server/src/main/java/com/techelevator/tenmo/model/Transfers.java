package com.techelevator.tenmo.model;

public class Transfers {

	private long transferId;
	private long transferTypeId;
	private long transferStatusId;
	private int accountFrom;
	private int accountTo;
	private double amount;
	
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
	public int getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(int accountFrom) {
		this.accountFrom = accountFrom;
	}
	public int getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(int accountTo) {
		this.accountTo = accountTo;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "Transfers [transferId=" + transferId + ", transferTypeId=" + transferTypeId + ", transferStatusId="
				+ transferStatusId + ", accountFrom=" + accountFrom + ", accountTo=" + accountTo + ", amount=" + amount
				+ "]";
	}
	
	
	
}
