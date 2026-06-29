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
public class WalletCreationController {
    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<?> createWallet(@RequestBody Map<String, Object> body) {
        Wallet wallet = walletService.createWallet(
                (String) body.get("phoneNumber"),
                (String) body.get("email"),
                Double.valueOf(body.get("initialBalance").toString()),
                (String) body.get("code"),
                (String) body.get("currency")
        );
        return ResponseEntity.ok(wallet);
    }
}