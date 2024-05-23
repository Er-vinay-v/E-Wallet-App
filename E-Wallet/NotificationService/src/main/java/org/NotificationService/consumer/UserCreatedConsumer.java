package org.NotificationService.consumer;

import constants.CommonConstants;
import models.enums.UserIdentifier;
import org.NotificationService.worker.EmailWorker;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserCreatedConsumer {

    @Autowired
    EmailWorker emailWorker;


    @KafkaListener(topics = CommonConstants.USER_TOPIC_NAME, groupId = "email-group")
    public void listenNewlyCreatedUser(String msg){
        JSONObject jsonObject = new JSONObject(msg);
        String name = jsonObject.optString(CommonConstants.USER_NAME);
        String email = jsonObject.optString(CommonConstants.USER_EMAIL);
      //  String phone = jsonObject.optString(CommonConstants.USER_PHONE);
        UserIdentifier userIdentifier = jsonObject.optEnum(UserIdentifier.class, CommonConstants.USER_IDENTIFIER);

        emailWorker.sendEmailNotification(name, email,userIdentifier.toString());
    }
}
