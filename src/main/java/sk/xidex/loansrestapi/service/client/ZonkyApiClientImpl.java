package sk.xidex.loansrestapi.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sk.xidex.loansrestapi.service.model.Loan;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ZonkyApiClientImpl implements ZonkyApiClient {

	private static final String LIST_LOANS = "https://private-anon-09c3ac41f9-zonky.apiary-proxy.com/loans/marketplace";
	private RestTemplate restTemplate;

	@Autowired
	public ZonkyApiClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	@Async
	public CompletableFuture<List<Loan>> getLoans(String rating, int pageNr, int pageSize) {
		String url = LIST_LOANS + "?rating__eq=" + rating + "&fields=amount";
		HttpHeaders headers = prepareHeaders(pageNr, pageSize);
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		ResponseEntity<List<Loan>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Loan>>() {
		});
		return CompletableFuture.completedFuture(responseEntity.getBody() != null ? responseEntity.getBody() : Collections.emptyList());
	}

    @Override
    public int getNumberOfLoans(String rating) {
        String url = LIST_LOANS + "?rating__eq=" + rating + "&fields=amount";
        HttpEntity<String> entity = new HttpEntity<>("parameters", prepareHeaders(0, 1));
        ResponseEntity<List<Loan>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Loan>>() {
        });
		return Integer.valueOf(responseEntity.getHeaders().get("x-total").get(0));
    }

    private HttpHeaders prepareHeaders(int pageNr, int pageSize) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		httpHeaders.add("x-page", Integer.toString(pageNr));
		httpHeaders.add("x-size", Integer.toString(pageSize));
		return httpHeaders;
	}
}
