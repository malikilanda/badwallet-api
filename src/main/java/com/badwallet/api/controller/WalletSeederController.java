package com.badwallet.api.controller;

import com.badwallet.api.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletSeederController {
    private final WalletService walletService;

    @PostMapping("/seed")
    public ResponseEntity<?> seed(
            @RequestParam(defaultValue = "10") int numWallets,
            @RequestParam(defaultValue = "100") int eventsPerWallet) {
        walletService.seedWallets(numWallets, eventsPerWallet);
        return ResponseEntity.ok(Map.of("message", "Seeding terminé", "wallets", numWallets));
    }
}