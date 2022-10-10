package com.tpro.dto;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.tpro.domain.Student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
	
	
	private Long id; 
	
	
	@NotNull(message="first name can not be null")
	@NotBlank(message="last name can not be white space")
	@Size(min=2, max=25, message="First name '${validatedValue}' must be between {min} and {max} long")
	
	private String firstName ;
	
	private String lastName;
	
	private Integer grade;
	
	@Email(message="Provide valid email") // xxx@yy.com -- aaaddd.com
	private String email;
	
	private String phoneNumber;
	
	private LocalDateTime createDate = LocalDateTime.now();
	
	public StudentDTO(Student student) { //Constructor-->obje dondurur
		this.id=student.getId();
		this.firstName=student.getName();
		this.lastName=student.getLastName();
		this.grade=student.getGrade();
		this.email=student.getEmail();
		this.phoneNumber=student.getPhoneNumber();
		this.createDate=student.getCreateDate();
		
	}
}