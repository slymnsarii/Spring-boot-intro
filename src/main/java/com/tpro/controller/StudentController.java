package com.tpro.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tpro.domain.Student;
import com.tpro.service.StudentService;
//controller'in isi request'den gelen istekleri  service'e gondermek
//controller katmaninda logic islemler yapamayiz, logic islemler service katmaninda yapilir
@RestController //proje RestFullAPI(Rest mimari ile uretildigi icin) oldugu icin
@RequestMapping("/students") //gelen requestleri "/students" ile maple
public class StudentController {

	@Autowired //suanda StudentService turunde studentService adinda bir obje bean olarak 
	//olusturulmaya aday olarak IOC containerinda duruyor, ben bunu istedigim anda(ihtiyac duydugum anda) 
	//Singleton Dizayn Pantern'e giderek bir tane ureterek bana verecek
	//(@Autowired annotation ile dependency injection yapmış oluyoruz, IOC containerda bulunan 
	//StudentService data türündeki objeyi new keywordunu kullanmadan uygulama içinde kullanmamı sağlıyor)
	private StudentService studentService;
	
	@GetMapping("/welcome") //localhost:8080/students/welcome 
							//(bana bu endpointle gelirsen bu asagidaki method'u calistir) //Get bilgi ister
	public String welcome() {
		return "Welcome to Student Controller";
	}
	
	// Get All Students
	@GetMapping //herhangi bir endpoint girmezsem yukardaki "/students" i alir(@RequestMapping'deki)
	/*
	 Burda donen bir method girmem gerek.Bana bir request geldigi zaman client'e response olarak 
	 https status kodunu gondermem lazim(200,300..) ve kullaniciya bir mesaj gondermem lazim(kullanicin istedigi bilgiler)
	 Bunun icin Springboot'da ResponseEntity diye bir yapi var.
	 Controller kisminda yazdigimiz method'larin hemen hemen hepsinde ResponseEntity yapisini kullaniriz.
	 ResponseEntity diamond operatoru ile calisir
	 
	 */
	public ResponseEntity<List<Student>> getAll() {//yapinin esnek olmasi icin array yerine collections'lari
		List<Student> students = studentService.getAll(); // kullaniyoruz(List)
		//(^)Controller service'e gidecegi icin, gitmesini de @Autowired ile service'e injekte ederek yaparak gidecegiz
		//burda map<> de donebiliriz ama projelerin %90'inda ResponseEntity kullanilir
		//getAll() method'undan bana exception gelecek mi kontrolunu service'de kontrol ediyoruz
		return ResponseEntity.ok(students); 
	}
	//ayni endpoint'e ayni mapping ile gelemezsiniz yani bi daha alta default endpoint icin @GetMapping yazamazsin
	//cunku endpoint ile method ayni olur ve karisir sistem
	
	//endpoint'i postman'da test edemeyiz cunku db bos, o yuzden test edebilmek icin create method'u olusturuyoruz
	
	//Create new Student
	@PostMapping ////herhangi bir endpoint girmezsem yukardaki "/students" i alir(@RequestMapping'deki)
	//bir kisi "/student" endpoint'i ile get methodu'yla gelirse @GetMapping, post ile gelirse @PostMapping calisir
	public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody Student student){
		//RequestBody:Bana gelen request'in body'sindeki data'yi(JSon) istiyorum demek
		//@RequestBody Student student(@RequestBody calismasi):request'en gelen JSon'i bendeki student'a maple(ata),
		//Boylece Student'a JSon'i POJO class'ina eklemis oluyorum
		//@Valid:request'ten gelen JSon'in bendeki Student POJO'mo uyuyor mu diye bakiyor
		studentService.createStudent(student); //service'e gondermek icin
		Map<String, String> map =new HashMap<>();
		map.put("message","Student is created succesfuly");
		map.put("status","true"); //test olarak duzgun olarak olustu demek(kullaniciya mesaj)
		return new ResponseEntity<>(map,HttpStatus.CREATED);
		
	}
	
	//Get a Student by ID by RequestParam
	@GetMapping("/query") //localhost:8080/students/1 benim istedigim id'yi endpoint'den cagirmak istersem
	public ResponseEntity<Student> getStudent(@RequestParam("id") Long id){
		Student student =studentService.findStudent(id); //burda student data turunde student isminde doner
		return ResponseEntity.ok(student);
		
	}
	
	//Get a Student by ID by PathVariable(tek bir sey istedigimde PathVariable kullanirim)
	@GetMapping("/{id}")
	public ResponseEntity<Student> getStudentWithPath(@PathVariable("id") Long id){
		Student student =studentService.findStudent(id); 
		return ResponseEntity.ok(student);
	}
	
	//Delete Student
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable("id") Long id){
		studentService.deleteStudent(id);
		Map<String, String> map =new HashMap<>();
		map.put("message","Student is deleted succesfuly");
		map.put("status","true"); //test olarak duzgun olarak olustu demek(kullaniciya mesaj)
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
}










