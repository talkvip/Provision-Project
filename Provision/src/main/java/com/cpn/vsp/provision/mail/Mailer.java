package com.cpn.vsp.provision.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class Mailer {

	@Autowired
	MailSender mailSender;
	
	public void sendMail(String aFrom, String aTo, String aSubject, String aBody){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(aFrom);
		message.setTo(aTo);
		message.setSubject(aSubject);
		message.setText(aBody);
		mailSender.send(message);
	}
}
