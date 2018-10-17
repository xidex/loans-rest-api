package sk.xidex.loansrestapi.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sk.xidex.loansrestapi.service.model.Loan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ZonkyApiServiceTest {

	@Mock
	private RestTemplate restTemplate;
	@InjectMocks
	private ZonkyApiServiceImpl zonkyApiService;

	@Test
	public void calculateAverageLoanAmount_ratingGiven_averageReturned() {
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(), ArgumentMatchers.<ParameterizedTypeReference<List<Loan>>>any()))
				.thenReturn(createResponse())
				.thenReturn(createSecondResponse());
		zonkyApiService.pageSize = 3;

		double average = zonkyApiService.calculateAverageLoanAmount("D");
		Assert.assertEquals(1750, average, 0.0);
		zonkyApiService.pageSize = 1000;
	}

	@Test
	public void calculateAverageLoanAmount_incorrectRatingGiven_zeroReturned() {
		Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(), ArgumentMatchers.<ParameterizedTypeReference<List<Loan>>>any()))
				.thenReturn(new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK));

		double average = zonkyApiService.calculateAverageLoanAmount("x");
		Assert.assertEquals(0, average, 0.0);
	}

	private ResponseEntity<List<Loan>> createResponse() {
		List<Loan> loans = new ArrayList<>();
		loans.add(new Loan(1000));
		loans.add(new Loan(2000));
		loans.add(new Loan(3000));
		return new ResponseEntity<>(loans, HttpStatus.OK);
	}

	private ResponseEntity<List<Loan>> createSecondResponse() {
		return new ResponseEntity<>(Collections.singletonList(new Loan(1000)), HttpStatus.OK);
	}
}
