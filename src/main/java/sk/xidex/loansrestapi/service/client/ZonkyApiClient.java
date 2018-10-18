package sk.xidex.loansrestapi.service.client;

import org.springframework.http.ResponseEntity;
import sk.xidex.loansrestapi.service.model.Loan;

import java.util.List;

public interface ZonkyApiClient {

    ResponseEntity<List<Loan>> getLoans(String rating, int pageNr, int pageSize);
}
