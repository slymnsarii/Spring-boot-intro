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
//@Entity -->Entity'i DTO'da eklemiyoruz çünkü o Hibernate icin ve burda gerek yok
public class StudentDTO {
	//DTO kullanma sebebim; 1)hız 2)guvenlik(id, email vs kullanicinin degistirmemesi icin)
	
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
	
	public StudentDTO(Student student) { //Constructor(parametreli)-->obje dondurur
		//(^)StudentDTO constructor'ina Student parametresi girersek otomatik map'liyor
 //StudentDTO donecek ama parametre olarak POJO'daki student parametresini gonderiyoruz ve icerde onu map'liyoruz
		//(^)amacim database'den controller tarafina bir sey gidecekse, POJO class'im bunu DTO'ya ceviriyor
		this.id=student.getId(); //olusacak olan studentDTO'nun id'si, POJO class'in id'sine denk gelsin
		this.firstName=student.getName(); //this.firstName(studentDTO)=student.getName(POJO id)
		this.lastName=student.getLastName();
		this.grade=student.getGrade();
		this.email=student.getEmail();
		this.phoneNumber=student.getPhoneNumber();
		this.createDate=student.getCreateDate();
		
	}
	
	/*
	 public StudentDTO(
            @NotNull(message = "first name can not be null") @NotBlank(message = "last name can not be white space") @Size(min = 2, max = 25, message = "First name '${validatedValue}' must be between {min} and {max} long") String name,
            String lastName, Integer grade, @Email(message = "Provide valid email") String email, String phoneNumber) {

        this.firstName = name;
        this.lastName = lastName;
        this.grade = grade;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    
    Controller'da en altta
	 */
}