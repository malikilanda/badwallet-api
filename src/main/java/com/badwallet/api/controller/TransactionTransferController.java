package com.badwallet.api.controller;

import com.badwallet.api.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class TransactionTransferController {
    private final WalletService walletService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody Map<String, Object> body) {
        walletService.transfer(
                (String) body.get("senderPhone"),
                (String) body.get("receiverPhone"),
                Double.valueOf(body.get("amount").toString())
        );
        return ResponseEntity.ok(Map.of("message", "Transfert effectué avec succès"));
    }
}