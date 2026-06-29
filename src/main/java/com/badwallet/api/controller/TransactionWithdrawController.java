package com.badwallet.api.controller;

import com.badwallet.api.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class TransactionWithdrawController {
    private final WalletService walletService;

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(walletService.withdraw(
                (String) body.get("phoneNumber"),
                Double.valueOf(body.get("amount").toString())
        ));
    }
}