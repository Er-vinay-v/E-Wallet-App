package org.WalletService.consumer;

import org.WalletService.worker.TransactionWorker;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionConsumer {

    @Autowired
    TransactionWorker transactionWorker;

    @KafkaListener(topics = "txn-details", groupId = "txn-group")
    public void listenTransaction(String msg){
        System.out.println("Transaction Data Received: "+msg);
        JSONObject jsonObject = new JSONObject(msg);

        String sender = jsonObject.getString("sender");
        String receiver = jsonObject.getString("receiver");
        String amount = jsonObject.getString("amount");
        String txnId = jsonObject.getString("txnId");

        transactionWorker.processTransaction(sender,receiver,amount,txnId);
    }
}
