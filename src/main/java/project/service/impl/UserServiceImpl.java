package project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.repository.UserRepository;
import project.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepo;
		
	static String lowerCharacters = "abcdefghijklmnopqrstuvwxyz";
	
	static String upperCharacters = "ABCDEFGHIJKLMNOPQRSTUVXYWZ";
	
	static String digits = "1234567890";
	
	@Override
	public boolean usernameIsAvailable(String username) {
		if(userRepo.findByUsername(username)!=null) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isInvalidUsername(String username) {
		String additionalString = "._"; 
		
		String fullUsernameCharacters = lowerCharacters + digits + additionalString;
		for(int i = 0; i < username.length(); i++) {
			char user = username.charAt(i);
			
			if((fullUsernameCharacters.indexOf(user))==-1) {
				return true;
			}
		}	
		return false;
	}

	@Override
	public boolean isInvalidPassword(String password) {
		int count = 0;
		String additionalString = "*#$._"; 
		String fullPasswordCharacters = lowerCharacters + upperCharacters + digits + additionalString;
		for(int i = 0; i < password.length(); i++) {
			char pass = password.charAt(i);
			if(count == 0) {
				if((upperCharacters.indexOf(pass))!=-1) {
				count++;
				}
			}
			if((fullPasswordCharacters.indexOf(pass))==-1) {
				return true;
			}
		}
		if(count == 0) {
			return true;
		}
		return false;
	}
	


}
