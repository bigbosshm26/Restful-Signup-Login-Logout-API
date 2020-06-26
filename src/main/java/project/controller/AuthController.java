package project.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import project.exception.BadRequestException;
import project.exception.ProjectException;
import project.model.User;
import project.model.User.Gender;
import project.payload.ApiResponse;
import project.payload.ChangePasswordRequest;
import project.payload.ForgotPasswordRequest;
import project.payload.LoginRequest;
import project.payload.SignUpRequest;
import project.payload.ForgotPasswordCodeRequest;
import project.payload.VerifyRequest;
import project.repository.UserRepository;
import project.service.EmailService;
import project.service.UserService;


@RestController
@RequestMapping("/api")
public class AuthController {
		
	private String codeCreated;
	
	private String codeForgotPassword;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
		
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new ProjectException(HttpStatus.BAD_REQUEST, "Username is already taken!");
		}
		if (userService.isInvalidUsername(signUpRequest.getUsername())) {
			throw new ProjectException(HttpStatus.BAD_REQUEST, "Username is invalid, it must not have any upperAlpha "
					+ "and must not have any another special characters except those characters:  ._");
		}
		if (userService.isInvalidPassword(signUpRequest.getPassword())) {
			throw new ProjectException(HttpStatus.BAD_REQUEST, "Password is invalid, it must have at least 1 upperAlpha "
					+ "and must not have any another special characters except those characters:  *#$._");
		}
		
		String username = signUpRequest.getUsername();

		String password = passwordEncoder.encode(signUpRequest.getPassword());
		
		Gender gender = signUpRequest.getGender();

		Date dateOfBirth = signUpRequest.getDateOfBirth();
		
		int phoneNumber = signUpRequest.getPhoneNumber();
		
		String email = signUpRequest.getEmail();
		
		User user = new User(username, password, gender, dateOfBirth, phoneNumber, email);

		userRepository.save(user);
		
		//send code to email to verify
		codeCreated = emailService.sendCodeToEmail(user.getEmail());

		return ResponseEntity.ok().body(new ApiResponse(Boolean.TRUE, "User registered successfully!"));
	}
	
	@PostMapping("/verify")
	public ResponseEntity<ApiResponse> verifyUser(@Valid @RequestBody VerifyRequest verifyRequest) {
		
		String username = verifyRequest.getUsername();
		
		User user = userRepository.findByUsername(username).orElseThrow(() -> new ProjectException(HttpStatus.BAD_REQUEST, "Not correct username or password!")); 
		
		String password = passwordEncoder.encode(verifyRequest.getPassword());
		
		if(password==user.getPassword()) {
				
			String code = verifyRequest.getCode();
		
			if(codeCreated == code) {
				
				return ResponseEntity.ok().body(new ApiResponse(Boolean.TRUE, "Verified Email!"));
			}
			else {
				
				throw new BadRequestException("Not correct code!");
			}
		}
		else {
			throw new BadRequestException("Not correct username or password!"); 
		}
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
		
		String username = loginRequest.getUsername();
		
		User user = userRepository.findByUsername(username).orElseThrow(() -> new ProjectException(HttpStatus.BAD_REQUEST, "Not correct username or password"));
		
		String password = passwordEncoder.encode(loginRequest.getPassword());
		
		if(password==user.getPassword()) {
			return ResponseEntity.ok().body(new ApiResponse(Boolean.TRUE, "Logged in successfully"));
		}
		throw new BadRequestException("Can't log in because of incorrect username or password");
	}
	
	@PostMapping("/resend-code")
	public ResponseEntity<ApiResponse> resendCode(User user){
		
		String resendedCode = emailService.sendCodeToEmail(user.getEmail());
		
		return ResponseEntity.ok().body(new ApiResponse(Boolean.TRUE, "Sended code to email!"));
	}
	
	@PostMapping("/send-forgotpass-code")
	public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest){
		
		String email = forgotPasswordRequest.getEmail();
		
		codeForgotPassword = emailService.sendCodeToEmail(email);
		
		return ResponseEntity.ok().body(new ApiResponse(Boolean.TRUE, "Sended code to email!"));
	}
	
	@PostMapping("/forgotpass-code")
	public ResponseEntity<ApiResponse> forgotPasswordCode(@Valid @RequestBody ForgotPasswordCodeRequest forgotPasswordRequest){
		
		String code = forgotPasswordRequest.getCode();
		
		if(code == codeForgotPassword) {
			
			return ResponseEntity.ok().body(new ApiResponse(Boolean.TRUE, "True code, let's change your password!"));
		}
		throw new BadRequestException("Wrong code!"); 
	
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
		
		String email = changePasswordRequest.getEmail();
		
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ProjectException(HttpStatus.BAD_REQUEST, "Not correct email!"));
		
		String newPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
		
		user.setPassword(newPassword);
		
		return ResponseEntity.ok().body(new ApiResponse(Boolean.TRUE, "Changed your password successfully!"));
	}
}
