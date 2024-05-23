package org.TxnService.Repository;

import org.TxnService.model.Transaction;
import org.TxnService.model.TxnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    @Transactional
    @Modifying
    @Query("update transaction as t set t.txnStatus=:status, t.txnMessage=:message where t.txnId=:txnId")
    void updateTransaction(String txnId, TxnStatus status, String message);

    List<Transaction> findBySenderId(String username);
}
