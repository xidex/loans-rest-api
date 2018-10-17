package sk.xidex.loansrestapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.xidex.loansrestapi.service.model.Loan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZonkyApiServiceImpl implements ZonkyApiService {

    private static final String LIST_LOANS = "https://private-anon-09c3ac41f9-zonky.apiary-proxy.com/loans/marketplace";

    int pageSize = 1000;
    private RestTemplate restTemplate;

    @Autowired
    public ZonkyApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public double calculateAverageLoanAmount(String rating) {
        String url = LIST_LOANS + "?rating__eq=" + rating + "&fields=amount";
        List<Double> averages = new ArrayList<>();
        ResponseEntity<List<Loan>> response;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        int pageNr = 0;
        headers.add("x-page", Integer.toString(pageNr));
        headers.add("x-size", Integer.toString(pageSize));
        do {
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Loan>>() {
                    }
            );
            if (response.getBody() == null) {
                break;
            }
            averages.addAll(response.getBody().parallelStream().map(Loan::getAmount).collect(Collectors.toList()));
            if (response.getBody().size() < pageSize) {
                break;
            }
            pageNr++;
            headers.set("x-page", Integer.toString(pageNr));
        } while (!response.getBody().isEmpty());

        return averages.parallelStream().mapToDouble(Double::doubleValue).average().orElse(0);
    }
}
