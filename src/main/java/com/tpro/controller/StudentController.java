package com.tpro.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpro.domain.Student;
import com.tpro.service.StudentService;
//controller'in isi service'e gondermek
@RestController
@RequestMapping("/students") //gelen requestleri /students ile maple
public class StudentController {

	@Autowired //suanda StudentService turunde studentService adinda ...?
	private StudentService studentService;
	
	@GetMapping("/welcome") //localhost:8080/students/welcome (bana bu endpointle gelirsen bu asagidaki method'u calistir) //Get bilgi ister
	public String welcome() {
		return "Welcome to Student Controller";
	}
	
	// Get All Students
	@GetMapping //herhangi bir endpoint girmezsem yukardaki /students i alir(@RequestMapping'deki)
	public ResponseEntity<List<Student>> getAll() {
		List<Student> students = studentService.getAll();
		return ResponseEntity.ok(students); //ResposeEntity kullanim amaci 200 olarak okey gondermek
	}
	
	//Create new Student
	@PostMapping
	public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody Student student){
		studentService.createStudent(student);
		Map<String, String> map =new HashMap<>();
		map.put("message","Student is created succesfuly");
		map.put("status","true");
		return new ResponseEntity<>(map,HttpStatus.CREATED);
	}
	
}
