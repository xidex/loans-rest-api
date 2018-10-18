package sk.xidex.loansrestapi.ws;

public class LoansAverageResponse {

	private double average;

	public LoansAverageResponse(double average) {
		this.average = average;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}
}
