package co.edu.icesi.ci.thymeval.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import co.edu.icesi.ci.thymeval.validategroups.EditValidation;
import co.edu.icesi.ci.thymeval.validategroups.UserEndingInfo;
import co.edu.icesi.ci.thymeval.validategroups.UserStartInfo;
import lombok.Data;

@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;
	
	
	@NotBlank(groups = UserStartInfo.class)
	@Size(min = 3, groups = UserStartInfo.class)
	private String username;
	
	@NotBlank(groups = {UserStartInfo.class, EditValidation.class})
	@Size(min = 8, groups = {UserStartInfo.class, EditValidation.class})
	private String password;
	
	@NotNull(groups = {UserStartInfo.class, EditValidation.class})
	@Past(groups = {UserStartInfo.class, EditValidation.class})
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	
	
	@NotBlank(groups = {UserEndingInfo.class, EditValidation.class})
	@Size(min = 2, groups = {UserEndingInfo.class, EditValidation.class})
	private String name;
	
	@NotBlank(groups = UserEndingInfo.class)
	@Email(groups = UserEndingInfo.class)
	private String email;
	
	@NotNull(groups = {UserEndingInfo.class, EditValidation.class})
	private UserType type;
	
	@NotNull(groups = {UserEndingInfo.class, EditValidation.class})
	private UserGender gender;
	
	@Transient
	private String newPassword;
	
//	@OneToMany
//	private List<Appointment> appointments;
}