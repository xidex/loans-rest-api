package sk.xidex.loansrestapi.service.client;

import sk.xidex.loansrestapi.service.model.Loan;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ZonkyApiClient {

	CompletableFuture<List<Loan>> getLoans(String rating, int pageNr, int pageSize);

	int getNumberOfLoans(String rating);
}
