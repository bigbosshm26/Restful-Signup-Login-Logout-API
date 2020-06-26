package project.service;

public interface EmailService {

	String createCode();
	
	String sendCodeToEmail(String email);
}
