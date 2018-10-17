package sk.xidex.loansrestapi.ws;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sk.xidex.loansrestapi.handler.RestResponseEntityExceptionHandler;
import sk.xidex.loansrestapi.service.ZonkyApiService;
import sk.xidex.loansrestapi.service.ZonkyApiServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AverageLoanAmountControllerTest {

	private MockMvc mockMvc;
	@Mock
	private ZonkyApiService zonkyApiService;
	@InjectMocks
	private AverageLoanAmountController controller;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setControllerAdvice(new RestResponseEntityExceptionHandler())
				.build();
	}

	@Test
	public void calculateAverageLoanAmount_serviceAvailable_okResponse() throws Exception {
		Mockito.when(zonkyApiService.calculateAverageLoanAmount(Mockito.anyString()))
				.thenReturn(0.0);

		this.mockMvc.perform(get("/average-loan-amount?rating=D"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Ignore
	public void calculateAverageLoanAmount_readTimeout_503Response() throws Exception {
		this.zonkyApiService = new ZonkyApiServiceImpl(new RestTemplateBuilder().setReadTimeout(5).build());
		this.controller = new AverageLoanAmountController(zonkyApiService);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setControllerAdvice(new RestResponseEntityExceptionHandler())
				.build();

		this.mockMvc.perform(get("/average-loan-amount?rating=D"))
				.andDo(print())
				.andExpect(status().isServiceUnavailable());
	}
}
