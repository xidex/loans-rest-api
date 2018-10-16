package sk.xidex.loansrestapi.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import sk.xidex.loansrestapi.service.model.Loan;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ZonkyApiServiceTest {

	@Mock
	private RestTemplate restTemplate;

	@Autowired
	private ZonkyApiService zonkyApiService;

	@Ignore
	public void calculateAverageLoanAmount_incorrectRatingGiven_zeroAverageReturned() {
		Mockito.when(restTemplate.exchange(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(ParameterizedTypeReference.class)))
				.thenReturn(createResponse())
				.thenReturn(createSecondResponse());

		double average = zonkyApiService.calculateAverageLoanAmount("D");
		Assert.assertEquals(1750, average, 0.0);
	}

	private ResponseEntity<List<Loan>> createResponse() {
		List<Loan> loans = new ArrayList<>();
		loans.add(new Loan(1000));
		loans.add(new Loan(2000));
		loans.add(new Loan(3000));
		return new ResponseEntity<>(loans, HttpStatus.OK);
	}

	private ResponseEntity<List<Loan>> createSecondResponse() {
		List<Loan> loans = new ArrayList<>();
		loans.add(new Loan(1000));
		return new ResponseEntity<>(loans, HttpStatus.OK);
	}
}
