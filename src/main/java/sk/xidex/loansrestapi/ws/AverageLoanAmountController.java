package sk.xidex.loansrestapi.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sk.xidex.loansrestapi.service.ZonkyApiService;

@RestController
public class AverageLoanAmountController {

    private final ZonkyApiService zonkyApiService;

    @Autowired
    public AverageLoanAmountController(ZonkyApiService zonkyApiService) {
        this.zonkyApiService = zonkyApiService;
    }

    @GetMapping(path = "/average-loan-amount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Double calculateAverageLoanAmount(@RequestParam(value = "rating") final String rating) {
        return zonkyApiService.calculateAverageLoanAmount(rating);
    }
}


