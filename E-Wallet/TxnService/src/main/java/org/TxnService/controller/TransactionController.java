package org.TxnService.controller;

import org.TxnService.model.TransactionResponse;
import org.TxnService.service.TransactionService;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/txn-service")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping("/initiate/transaction")
    public String initiateTransaction(@RequestParam(value = "amount", required = false) String amount,
                                      @RequestParam(value = "receiver", required = false) String receiver,
                                      @RequestParam(value = "purpose", required = false) String purpose
                                     ){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       return transactionService.initiateTransaction(userDetails.getUsername(),receiver,amount,purpose);

    }

    @RequestMapping("/transaction/history")
    public List<TransactionResponse> getTransactionHistory(@RequestParam(value = "mobile", required = false) String mobile){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return transactionService.getTransaction(mobile);

    }
}
