package com.badwallet.api.controller;

import com.badwallet.api.model.Wallet;
import com.badwallet.api.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class TransactionDepositController {
    private final WalletService walletService;

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Wallet> deposit(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(walletService.deposit(
                id,
                Double.valueOf(body.get("amount").toString()),
                (String) body.get("paymentMethod")
        ));
    }
}