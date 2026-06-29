package com.badwallet.api.service;

import com.badwallet.api.model.Wallet;
import com.badwallet.api.model.Transaction;
import com.badwallet.api.repository.WalletRepository;
import com.badwallet.api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Wallet createWallet(String phoneNumber, String email, Double initialBalance, String code, String currency) {
        if (walletRepository.existsByPhoneNumber(phoneNumber)) throw new RuntimeException("Numéro déjà utilisé");
        if (walletRepository.existsByEmail(email)) throw new RuntimeException("Email déjà utilisé");
        if (walletRepository.existsByCode(code)) throw new RuntimeException("Code déjà utilisé");
        Wallet wallet = new Wallet();
        wallet.setPhoneNumber(phoneNumber);
        wallet.setEmail(email);
        wallet.setBalance(initialBalance);
        wallet.setCode(code);
        wallet.setCurrency(currency);
        return walletRepository.save(wallet);
    }

    public Page<Wallet> getAllWallets(Pageable pageable) {
        return walletRepository.findAll(pageable);
    }

    public Wallet getWalletByPhone(String phoneNumber) {
        return walletRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Portefeuille non trouvé"));
    }

    public Wallet getWalletById(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portefeuille non trouvé"));
    }

    @Transactional
    public Wallet deposit(Long walletId, Double amount, String paymentMethod) {
        Wallet wallet = getWalletById(walletId);
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
        Transaction tx = new Transaction();
        tx.setWallet(wallet);
        tx.setType("DEPOSIT");
        tx.setAmount(amount);
        tx.setFees(0.0);
        tx.setDescription("Dépôt via " + paymentMethod);
        transactionRepository.save(tx);
        return wallet;
    }

    @Transactional
    public Wallet withdraw(String phoneNumber, Double amount) {
        Wallet wallet = getWalletByPhone(phoneNumber);
        double fees = Math.min(amount * 0.01, 5000);
        double total = amount + fees;
        if (wallet.getBalance() < total) throw new RuntimeException("Solde insuffisant");
        wallet.setBalance(wallet.getBalance() - total);
        walletRepository.save(wallet);
        Transaction tx = new Transaction();
        tx.setWallet(wallet);
        tx.setType("WITHDRAW");
        tx.setAmount(amount);
        tx.setFees(fees);
        tx.setDescription("Retrait avec frais de " + fees + " CFA");
        transactionRepository.save(tx);
        return wallet;
    }

    @Transactional
    public void transfer(String senderPhone, String receiverPhone, Double amount) {
        Wallet sender = getWalletByPhone(senderPhone);
        Wallet receiver = getWalletByPhone(receiverPhone);
        if (sender.getBalance() < amount) throw new RuntimeException("Solde insuffisant");
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        walletRepository.save(sender);
        walletRepository.save(receiver);
        Transaction txSender = new Transaction();
        txSender.setWallet(sender);
        txSender.setType("TRANSFER");
        txSender.setAmount(amount);
        txSender.setFees(0.0);
        txSender.setDescription("Transfert vers " + receiverPhone);
        transactionRepository.save(txSender);
        Transaction txReceiver = new Transaction();
        txReceiver.setWallet(receiver);
        txReceiver.setType("TRANSFER");
        txReceiver.setAmount(amount);
        txReceiver.setFees(0.0);
        txReceiver.setDescription("Transfert reçu de " + senderPhone);
        transactionRepository.save(txReceiver);
    }

    public List<Transaction> getTransactionHistory(String phoneNumber) {
        Wallet wallet = getWalletByPhone(phoneNumber);
        return transactionRepository.findByWalletOrderByCreatedAtDesc(wallet);
    }

    @Transactional
    public void seedWallets(int numWallets, int eventsPerWallet) {
        for (int i = 1; i <= numWallets; i++) {
            String code = String.format("WLT-%07d", i);
            if (walletRepository.existsByCode(code)) continue;
            Wallet wallet = new Wallet();
            wallet.setPhoneNumber("+22177000000" + i);
            wallet.setEmail("user" + i + "@badwallet.com");
            wallet.setBalance(50000.0);
            wallet.setCode(code);
            wallet.setCurrency("XOF");
            walletRepository.save(wallet);
            for (int j = 1; j <= eventsPerWallet; j++) {
                Transaction tx = new Transaction();
                tx.setWallet(wallet);
                tx.setType("DEPOSIT");
                tx.setAmount(1000.0 * j);
                tx.setFees(0.0);
                tx.setDescription("Transaction seed #" + j);
                transactionRepository.save(tx);
            }
        }
    }

    @Transactional
    public void enregistrerPaiement(Wallet wallet, Double amount, String serviceName) {
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);
        Transaction tx = new Transaction();
        tx.setWallet(wallet);
        tx.setType("PAYMENT");
        tx.setAmount(amount);
        tx.setFees(0.0);
        tx.setDescription("Paiement " + serviceName);
        transactionRepository.save(tx);
    }
}