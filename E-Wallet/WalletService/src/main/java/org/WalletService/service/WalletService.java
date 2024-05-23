package org.WalletService.service;

import jakarta.transaction.Transactional;
import org.WalletService.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletService extends JpaRepository<Wallet,Integer> {

    Wallet findByPhoneNo(String phone);

    @Transactional
    @Modifying
    @Query("update wallet w set w.balance=w.balance+:amount where w.phoneNo=:account")
    void updateWallet(String account, double amount);
}
