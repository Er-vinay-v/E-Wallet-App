package org.WalletService.consumer;

import constants.CommonConstants;
import models.enums.UserIdentifier;
import org.WalletService.model.Wallet;
import org.WalletService.service.WalletService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OnboardedUserConsumer {

    @Value("${wallet.initial.amount}")
    String amount;

    @Autowired
    WalletService walletService;

    @KafkaListener(topics = CommonConstants.USER_TOPIC_NAME, groupId = "wallet-group")
    public void listenNewUser(String msg){
        System.out.println("Wallet service received data from kafka");
        System.out.println(msg);
        JSONObject jsonObject = new JSONObject(msg);
        String name = jsonObject.optString(CommonConstants.USER_NAME);
        String email = jsonObject.optString(CommonConstants.USER_EMAIL);
        String phone = jsonObject.optString(CommonConstants.USER_PHONE);
       // UserIdentifier userIdentifier = jsonObject.optEnum(UserIdentifier.class, CommonConstants.USER_IDENTIFIER);
        String userIdentifier = jsonObject.optString(CommonConstants.USER_IDENTIFIER_STRING);
        String userIdentifierValue = jsonObject.optString(CommonConstants.USER_IDENTIFIER_VALUE);
        String userId = jsonObject.optString(CommonConstants.USER_ID);


        Wallet wallet = Wallet.builder()
                .userId(userId)
                .phoneNo(phone)
                .userIdentifier(userIdentifier)
                .userIdentifierValue(userIdentifierValue)
                .balance(Double.parseDouble(amount)).build();

        walletService.save(wallet);

        System.out.println("Wallet Has been created");

    }
}
