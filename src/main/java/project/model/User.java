package project.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = { "username" }))
public class User implements UserDetails{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@NotNull
	@Size(min = 8, message = "Username must be at least 8 characters long")
	@Column(name = "username")
	private String username;
	
	@NotNull
	@Size(min = 8, message = "Password must be at least 8 characters long")
	@Column(name = "password")
	private String password;
	
	@NotNull(message = "Gender is required")
	@Column(name = "gender")
	private Gender gender;
	
	@Pattern(regexp = "((0[1-9])|(1[0-2]))-(([0-2]\\d)|([3][01]))-(\\d{4})",
			message = "Must be formatted MM/DD/YYYY")
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@Digits(integer = 10, fraction = 0, message = "Invalid Phone Number")
	@Column(name = "phone_number")
	private int phoneNumber;
	
	@NotNull(message = "Email is required")
	@Email
	@Column(name = "email")
	private String email;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	public static enum Gender{
		MALE, FEMALE, LGBT
	}
	
	public User(String username, String password, Gender gender, Date dateOfBirth, int phoneNumber, String email) {
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities == null ? null : new ArrayList<>(authorities);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
