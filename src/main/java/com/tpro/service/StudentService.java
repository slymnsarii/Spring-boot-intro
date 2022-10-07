package com.tpro.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tpro.domain.Student;
import com.tpro.repository.StudentRepository;

@Service
public class StudentService {
	
	@Autowired //enjekte edilmesi icin
	private StudentRepository studentRepository;

	public List<Student> getAll() {
		
		return studentRepository.findAll();
	}

	public void createStudent(Student student) {
		if (studentRepository.existsByEmail(student.getEmail())) { //email ile unique gelmesini istedigim icin existByEmail yaptik
			//burada exception eklenecek
		}
		
	}

}
