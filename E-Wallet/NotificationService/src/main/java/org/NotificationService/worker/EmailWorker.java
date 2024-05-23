package org.NotificationService.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailWorker {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmailNotification(String name, String email, String identifier){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Welcome to E-Wallet Application");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText("Hi "+name+",\n"+" Welcome to JBDL Wallet Application, you have been successfully onboarded and verified with your "+identifier);
        javaMailSender.send(simpleMailMessage);
    }
}
