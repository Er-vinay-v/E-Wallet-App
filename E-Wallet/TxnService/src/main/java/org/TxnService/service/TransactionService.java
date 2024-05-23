package org.TxnService.service;

import org.TxnService.Repository.TransactionRepository;
import org.TxnService.model.Transaction;
import org.TxnService.model.TransactionResponse;
import org.TxnService.model.TxnStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public String initiateTransaction(String sender, String receiver, String amount,String purpose){
        String txnId = UUID.randomUUID().toString();
        Transaction transaction = Transaction.builder()
                .senderId(sender)
                .receiverId(receiver)
                .amount(Double.parseDouble(amount))
                .purpose(purpose)
                .txnId(txnId)
                .txnStatus(TxnStatus.INITIATED).build();

        transactionRepository.save(transaction);

        JSONObject txnObject = new JSONObject();
        txnObject.put("sender", sender);
        txnObject.put("receiver", receiver);
        txnObject.put("amount",amount);
        txnObject.put("txnId", txnId);

        kafkaTemplate.send("txn-details",txnObject.toString());

        return txnId;
    }

    @KafkaListener(topics = "txn-update", groupId = "txn-update-group")
    public void listenUpdatedTransaction(String msg){
        System.out.println("Transaction Update data Received: "+msg);

        JSONObject response = new JSONObject(msg);
        String txnId = response.getString("txnId");
        String status = response.getString("status");
        String message = response.getString("message");

        TxnStatus txnStatus = TxnStatus.valueOf(status);

        transactionRepository.updateTransaction(txnId,txnStatus,message);

        System.out.println("Transaction Updated successfully");
    }


    public List<TransactionResponse> getTransaction(String username){
        List<Transaction> list = transactionRepository.findBySenderId(username);
        List<TransactionResponse> ans =new ArrayList<>();

        for (Transaction t: list){
            TransactionResponse ts = new TransactionResponse();
            ts.setAmount(Double.toString(t.getAmount()));
            ts.setStatus(t.getTxnStatus());
            ts.setPurpose(t.getPurpose());
            ts.setSentTo(t.getReceiverId());
            ans.add(ts);
        }

        return ans;
    }
}
