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

@Service
public class ZonkyApiServiceImpl implements ZonkyApiService {

	private static final String LIST_LOANS = "https://private-anon-09c3ac41f9-zonky.apiary-proxy.com/loans/marketplace";

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
		Integer pageNr = 0;
		headers.add("x-page", pageNr.toString());
		headers.add("x-size", "1000");
		do {
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
			response = restTemplate.exchange(
					url,
					HttpMethod.GET,
					entity,
					new ParameterizedTypeReference<List<Loan>>() {
					}
			);
			response.getBody().parallelStream().mapToDouble(Loan::getAmount).average().ifPresent(averages::add);
			pageNr++;
			headers.set("x-page", pageNr.toString());
		} while (!response.getBody().isEmpty());

		return averages.parallelStream().mapToDouble(Double::doubleValue).average().orElse(0);
	}
}
