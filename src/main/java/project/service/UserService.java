package project.service;

public interface UserService {

	boolean usernameIsAvailable(String username);
	
	boolean isInvalidPassword(String password);
	
	boolean isInvalidUsername(String username);	
	
	
}
