package project.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class VerifyRequest {
		
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;

	@NotBlank
	@Size(min = 6, max = 6)
	private String code;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
