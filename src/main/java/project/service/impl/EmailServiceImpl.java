package project.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import project.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{

	int lengthOfCode = 6;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	private String codeCreated;

	@Override
	public String createCode() {
		String lowerAlphas = "abcdefghijklmnopqrstuvwxyz";
		String upperAlphas = lowerAlphas.toUpperCase();	
		String digits = "1234567890";		
		String fullCharacters = lowerAlphas + upperAlphas + digits;
		
		Random generator = new Random();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(digits.charAt(generator.nextInt(10)));
		
        for (int i = 1; i < lengthOfCode; i++) {
            int number = generator.nextInt(fullCharacters.length() - 1);
            char ch = fullCharacters.charAt(number);
            sb.append(ch);
        }
        return sb.toString();
	}

	@Override
	public String sendCodeToEmail(String email) {
		SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject("Code");
        
        codeCreated = createCode();
        
        msg.setText(codeCreated);

        javaMailSender.send(msg);
        
        return codeCreated;
	}

}
