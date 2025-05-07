package com.team.printo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.team.printo.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String formEmail;
	
	
	public void sendConfirmationCode(User user) {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		simpleMailMessage.setFrom("bodynafea2@gmail.com");
		simpleMailMessage.setTo(user.getEmail());
		simpleMailMessage.setSubject("Confirmation Code");
		simpleMailMessage.setText("Your Confirmation Code : " + user.getConfirmationCode());
		javaMailSender.send(simpleMailMessage);
		
	}
	
	public void sendPasswordResetCode(User user) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		simpleMailMessage.setFrom("bodynafea2@gmail.com");
		simpleMailMessage.setTo(user.getEmail());
		simpleMailMessage.setSubject("Reset Your Password");
		simpleMailMessage.setText("Your password reset code is: " + user.getConfirmationCode());
		javaMailSender.send(simpleMailMessage);
	}
	

}
