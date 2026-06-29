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
public class WalletConsultationController {
    private final WalletService walletService;

    @GetMapping("/{phone}")
    public ResponseEntity<Wallet> getByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(walletService.getWalletByPhone(phone));
    }

    @GetMapping("/{phone}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String phone) {
        Wallet wallet = walletService.getWalletByPhone(phone);
        return ResponseEntity.ok(Map.of(
                "phoneNumber", wallet.getPhoneNumber(),
                "balance", wallet.getBalance(),
                "currency", wallet.getCurrency()
        ));
    }
}