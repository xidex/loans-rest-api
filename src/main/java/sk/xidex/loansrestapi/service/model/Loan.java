package sk.xidex.loansrestapi.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Loan {

	public Loan() {
	}

	public Loan(double amount) {
		this.amount = amount;
	}

	private double amount;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
