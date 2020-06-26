package project.payload;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import lombok.AllArgsConstructor;
import lombok.Data;
import project.model.User.Gender;

@Data
@AllArgsConstructor
public class SignUpRequest {

	@NotNull
	@Size(min = 8, message = "Username must be at least 8 characters long")
	private String username;
	
	@NotNull
	@Size(min = 8, message = "Password must be at least 8 characters long")
	private String password;
		
	@NotNull(message = "Gender is required")
	private Gender gender;
	
	@Pattern(regexp = "((0[1-9])|(1[0-2]))-(([0-2]\\d)|([3][01]))-(\\d{4})",
			message = "Must be formatted MM/DD/YYYY")
	private Date dateOfBirth;
	
	@Digits(integer = 10, fraction = 0, message = "Invalid Phone Number")
	private int phoneNumber;
	
	@NotNull(message = "Email is required")
	@Email
	private String email;
	
}
