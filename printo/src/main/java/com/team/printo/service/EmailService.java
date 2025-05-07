package com.team.printo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.team.printo.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String formEmail;
	
	
	public void sendCode(User user,String subject) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(formEmail);
			helper.setTo(user.getEmail());
			helper.setSubject(subject);

			String htmlContent = """
				<html>
				  <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
				    <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
				      <h2 style="color: #333333;">Welcome to Printo 🎉</h2>
				      <p style="font-size: 16px; color: #555555;">Hi <strong>%s</strong>,</p>
				      <p style="font-size: 16px; color: #555555;">Thank you for Contact Us. Please use the following confirmation code:</p>
				      <div style="text-align: center; margin: 30px 0;">
				        <span style="font-size: 24px; color: #ffffff; background-color: #007BFF; padding: 10px 20px; border-radius: 4px; letter-spacing: 2px;">%s</span>
				      </div>
				      <p style="font-size: 14px; color: #999999;">If you didn’t request this, you can safely ignore this email.</p>
				      <p style="font-size: 14px; color: #999999;">— The Printo Team</p>
				    </div>
				  </body>
				</html>
			""".formatted(user.getFirstName(), user.getConfirmationCode());

			helper.setText(htmlContent, true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Failed to send confirmation email", e);
		}
	}
	
	

	public void sendPasswordResetCode(User user) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(formEmail);
			helper.setTo(user.getEmail());
			helper.setSubject("Reset Your Password");

			String htmlContent = """
				<html>
				  <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
				    <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
				      <h2 style="color: #333333;">Welcome to Printo 🎉</h2>
				      <p style="font-size: 16px; color: #555555;">Hi <strong>%s</strong>,</p>
				      <p style="font-size: 16px; color: #555555;">Thank you for Contact Us. Please use the following confirmation code:</p>
				      <div style="text-align: center; margin: 30px 0;">
				        <span style="font-size: 24px; color: #ffffff; background-color: #007BFF; padding: 10px 20px; border-radius: 4px; letter-spacing: 2px;">%s</span>
				      </div>
				      <p style="font-size: 14px; color: #999999;">If you didn’t request this, you can safely ignore this email.</p>
				      <p style="font-size: 14px; color: #999999;">— The Printo Team</p>
				    </div>
				  </body>
				</html>
			""".formatted(user.getFirstName(), user.getConfirmationCode());

			helper.setText(htmlContent, true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Failed to send confirmation email", e);
		}
	}
	

	

}
