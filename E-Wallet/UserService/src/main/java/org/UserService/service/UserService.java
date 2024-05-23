package org.UserService.service;

import constants.CommonConstants;
import org.UserService.Repository.UserRepository;
import org.UserService.model.User;
import org.UserService.model.enums.UserStatus;
import org.UserService.model.enums.UserType;
import org.UserService.request.CreateUserRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    public User onboardNewUser(CreateUserRequest createUserRequest){
        String password = createUserRequest.getPassword();
        User user = createUserRequest.toUser();
        user.setPassword(passwordEncoder.encode(password));
        user.setUserStatus(UserStatus.ACTIVE);
        user.setRole(UserType.USER);

        userRepository.save(user);

        // Data has been saved in database, now pushing in the kafka
        JSONObject request = new JSONObject();
        request.put(CommonConstants.USER_NAME, user.getName());
        request.put(CommonConstants.USER_EMAIL, user.getEmail());
        request.put(CommonConstants.USER_PHONE, user.getPhone());
        request.put(CommonConstants.USER_IDENTIFIER, user.getUserIdentifier());
        request.put(CommonConstants.USER_IDENTIFIER_STRING, user.getUserIdentifier().toString());
        request.put(CommonConstants.USER_IDENTIFIER_VALUE, user.getUserIdentifierValue());
        request.put(CommonConstants.USER_ID, Integer.toString(user.getId()));
        kafkaTemplate.send(CommonConstants.USER_TOPIC_NAME, request.toString());
        System.out.println("Data has been sent to Kafka");
        return user;
    }

    public String fetchUserBYUsername(String username){
        User user = userRepository.findByPhone(username);
        JSONObject responseObject = new JSONObject();
        responseObject.put("phone",user.getPhone());
        responseObject.put("password",user.getPassword());
        responseObject.put("role",user.getRole().toString());
        System.out.println("Sending response: "+responseObject);
        return responseObject.toString();
    }
}
