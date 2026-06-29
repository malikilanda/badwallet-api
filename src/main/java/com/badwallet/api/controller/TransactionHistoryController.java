package com.badwallet.api.controller;

import com.badwallet.api.model.Transaction;
import com.badwallet.api.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class TransactionHistoryController {
    private final WalletService walletService;

    @GetMapping("/{phone}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String phone) {
        return ResponseEntity.ok(walletService.getTransactionHistory(phone));
    }
}
