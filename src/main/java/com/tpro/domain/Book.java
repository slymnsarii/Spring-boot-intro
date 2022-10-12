package com.tpro.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonProperty("bookName") //alttaki kod'da name'i degistirmiyoruz ama JSON ciktida bookName olmasini sagladik(Student'daki name ile karismasin diye)
	private String name;
	
	@JsonIgnore////sonsuz döngüye girmemesi için kısaca burada yeniden
	//student classına gitmemesi için bu annotationu kullandık
	//burda stackoverflow olmaması için yaptık. student-book arası gidip gelmemesi için
	@ManyToOne
	@JoinColumn(name="student_id") //joinColumn ici foregin key oluyordu, o yuzden id ile iliskilendiiryoruz
	private Student student;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Student getStudent() {
		return student;
	}
	
	
}
