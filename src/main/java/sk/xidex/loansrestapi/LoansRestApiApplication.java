package sk.xidex.loansrestapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import sk.xidex.loansrestapi.service.ZonkyApiServiceImpl;

@SpringBootApplication
public class LoansRestApiApplication {

	private static final Logger log = LoggerFactory.getLogger(LoansRestApiApplication.class);

	public static void main(String[] args) {
//		SpringApplication.run(LoansRestApiApplication.class, args);
		ZonkyApiServiceImpl zonkyApiService = new ZonkyApiServiceImpl(new RestTemplateBuilder().setConnectTimeout(5000).setReadTimeout(5000).build());
		log.info("Average: " + zonkyApiService.calculateAverageLoanAmount("D"));
	}
}
