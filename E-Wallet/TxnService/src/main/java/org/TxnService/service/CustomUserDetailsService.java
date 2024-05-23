package org.TxnService.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String response = restTemplate.getForObject("http://localhost:8083/user-onboarding/get/user?username="+username,String.class);

        System.out.println("Response: "+response);
        JSONObject jsonObject = new JSONObject(response);
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        String role = jsonObject.getString("role");

        if (response==null){
            throw new UsernameNotFoundException("User Not Found");
        }

        UserDetails user = User.builder().username(phone).password(password).roles(role).build();
        return user;

    }
}
