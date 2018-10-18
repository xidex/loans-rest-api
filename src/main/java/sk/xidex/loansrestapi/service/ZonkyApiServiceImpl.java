package sk.xidex.loansrestapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sk.xidex.loansrestapi.service.client.ZonkyApiClient;
import sk.xidex.loansrestapi.service.model.Loan;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Double> averages = new ArrayList<>();
        int pageNr = 0;
        ResponseEntity<List<Loan>> response;
        do {
            response = zonkyApiClient.getLoans(rating, pageNr, pageSize);
            if (response.getBody() == null) {
                break;
            }
            averages.addAll(response.getBody().parallelStream().map(Loan::getAmount).collect(Collectors.toList()));
            if (response.getBody().size() < pageSize) {
                break;
            }
            pageNr++;
        } while (!response.getBody().isEmpty());

        return averages.parallelStream().mapToDouble(Double::doubleValue).average().orElse(0);
    }
}
