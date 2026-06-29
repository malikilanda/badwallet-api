package com.badwallet.api.controller;

import com.badwallet.api.client.PaymentServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external")
@RequiredArgsConstructor
public class ProxyFacturesController {
    private final PaymentServiceClient paymentServiceClient;

    @GetMapping("/factures/{walletCode}/current")
    public ResponseEntity<?> getFacturesCourantes(
            @PathVariable String walletCode,
            @RequestParam(required = false) String unite) {
        return ResponseEntity.ok(paymentServiceClient.getFacturesCourantes(walletCode, unite));
    }

    @GetMapping("/factures/{walletCode}/periode")
    public ResponseEntity<?> getFacturesByPeriode(
            @PathVariable String walletCode,
            @RequestParam String debut,
            @RequestParam String fin) {
        return ResponseEntity.ok(paymentServiceClient.getFacturesByPeriode(walletCode, debut, fin));
    }
}
