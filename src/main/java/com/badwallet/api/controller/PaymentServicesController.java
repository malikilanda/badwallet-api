package com.badwallet.api.controller;

import com.badwallet.api.client.PaymentServiceClient;
import com.badwallet.api.service.WalletService;
import com.badwallet.api.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class PaymentServicesController {
    private final WalletService walletService;
    private final PaymentServiceClient paymentServiceClient;

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody Map<String, Object> body) {
        String phoneNumber = (String) body.get("phoneNumber");
        String serviceName = (String) body.get("serviceName");
        Double amount = Double.valueOf(body.get("amount").toString());
        Wallet wallet = walletService.getWalletByPhone(phoneNumber);
        if (wallet.getBalance() < amount) throw new RuntimeException("Solde insuffisant");
        walletService.enregistrerPaiement(wallet, amount, serviceName);
        return ResponseEntity.ok(paymentServiceClient.payerFactureMoisCourant(wallet.getCode(), serviceName));
    }

    @PostMapping("/pay-factures")
    public ResponseEntity<?> payFactures(@RequestBody Map<String, Object> body) {
        String phoneNumber = (String) body.get("phoneNumber");
        String serviceName = (String) body.get("serviceName");
        List<String> references = (List<String>) body.get("factureReferences");
        Double totalAmount = Double.valueOf(body.get("totalAmount").toString());

        Wallet wallet = walletService.getWalletByPhone(phoneNumber);
        if (wallet.getBalance() < totalAmount) throw new RuntimeException("Solde insuffisant");

        walletService.enregistrerPaiement(wallet, totalAmount, serviceName);
        return ResponseEntity.ok(paymentServiceClient.payerFacturesSpecifiques(wallet.getCode(), serviceName, references));
    }
}