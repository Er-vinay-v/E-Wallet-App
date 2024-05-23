package org.WalletService.worker;

import jakarta.transaction.Transactional;
import org.WalletService.model.Wallet;
import org.WalletService.service.WalletService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionWorker {


    @Autowired
    WalletService walletService;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public void processTransaction(String sender, String receiver, String amount, String txnId){

        Wallet senderWallet = walletService.findByPhoneNo(sender);
        Wallet receiverWallet = walletService.findByPhoneNo(receiver);

        String message = "";
        String status = "";
        double senderbalance = senderWallet.getBalance();
        if (senderWallet==null){
            status = "FAILED";
            message = "sender account does not exist";
        }

        else if (receiverWallet==null){
            status = "FAILED";
            message = "receiver account does not exist";
            return;
        }

        else if (senderbalance<Double.parseDouble(amount)){
            status = "FAILED";
            message = "Not Sufficient Balance";
            return;
        }

        else {
            processWalletTransaction(sender, receiver, amount);
            status = "SUCCESS";
            message = "Your transaction is successfull";
        }


        // pushing data to kafka

        JSONObject txnObject = new JSONObject();
        txnObject.put("txnId",txnId);
        txnObject.put("status",status);
        txnObject.put("message",message);

        kafkaTemplate.send("txn-update",txnObject.toString());

    }

    @Transactional
    public void processWalletTransaction(String sender, String receiver, String amount){
        walletService.updateWallet(sender,-(Double.parseDouble(amount)));
        walletService.updateWallet(receiver,Double.parseDouble(amount));

    }
}
