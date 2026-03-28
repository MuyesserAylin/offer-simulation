package com.simulation.offer_system.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	public void sendEmail(String toEmail, String subject, String body) {
	
        SimpleMailMessage message = new SimpleMailMessage();
       
        message.setFrom("savuranmuyesseraylin@gmail.com"); 
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        System.out.println("Mail başarıyla kuyruğa alındı ve gönderildi!");
    }

}
