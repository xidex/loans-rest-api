package sk.xidex.loansrestapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.xidex.loansrestapi.service.client.ZonkyApiClient;
import sk.xidex.loansrestapi.service.model.Loan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ZonkyApiServiceImpl implements ZonkyApiService {

	private ZonkyApiClient zonkyApiClient;
	int pageSize = 1000;

	@Autowired
	public ZonkyApiServiceImpl(ZonkyApiClient zonkyApiClient) {
		this.zonkyApiClient = zonkyApiClient;
	}

	@Override
	public double calculateAverageLoanAmount(String rating) {
		int numberOfLoans = this.getNumberOfLoans(rating);
		int numberOfRequests = numberOfLoans / pageSize + (numberOfLoans % pageSize == 0 ? 0 : 1);
		List<CompletableFuture<List<Loan>>> loansFutureList = new ArrayList<>();
		for (int i = 0; i < numberOfRequests; i++) {
			loansFutureList.add(zonkyApiClient.getLoans(rating, i, pageSize));
		}

		return loansFutureList.stream()
				.map(CompletableFuture::join)
				.flatMap(List::stream)
				.mapToDouble(Loan::getAmount)
				.average()
				.orElse(0);
	}

	private int getNumberOfLoans(String rating) {
		return zonkyApiClient.getNumberOfLoans(rating);
	}
}
