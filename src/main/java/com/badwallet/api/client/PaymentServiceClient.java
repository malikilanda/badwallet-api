package com.badwallet.api.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentServiceClient {
    private final RestTemplate restTemplate;
    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    public void seedFactures(List<String> walletCodes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> request = new HttpEntity<>(walletCodes, headers);
        restTemplate.postForObject(paymentServiceUrl + "/api/factures/seed", request, Object.class);
    }

    public Object payerFactureMoisCourant(String walletCode, String serviceName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> body = Map.of("walletCode", walletCode, "serviceName", serviceName);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(paymentServiceUrl + "/api/factures/pay/current", request, Object.class);
    }

    public Object payerFacturesSpecifiques(String walletCode, String serviceName, List<String> references) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = Map.of("walletCode", walletCode, "serviceName", serviceName, "factureReferences", references);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(paymentServiceUrl + "/api/factures/pay/specifiques", request, Object.class);
    }

    public Object getFacturesCourantes(String walletCode, String unite) {
        String url = paymentServiceUrl + "/api/factures/" + walletCode + "/current";
        if (unite != null) url += "?unite=" + unite;
        return restTemplate.getForObject(url, Object.class);
    }

    public Object getFacturesByPeriode(String walletCode, String debut, String fin) {
        String url = paymentServiceUrl + "/api/factures/" + walletCode + "/periode?debut=" + debut + "&fin=" + fin;
        return restTemplate.getForObject(url, Object.class);
    }
}