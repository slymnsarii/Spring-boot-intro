package com.tpro.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.tpro.domain.Student;
import com.tpro.dto.StudentDTO;
import com.tpro.exception.ConflictException;
import com.tpro.exception.ResourceNotFoundException;
import com.tpro.repository.StudentRepository;

@Service
public class StudentService {
	
	@Autowired //enjekte edilmesi icin
	private StudentRepository studentRepository; //bunu yazarak service katmani repo'ya gidip ordan bir sey isteyecek 

	public List<Student> getAll() {
		
		return studentRepository.findAll(); //enjekte ettigim seyi yaziyorum
		//findAll() method'u otomatik olarak repository'e gidiyor, JpaRepository'deki hazir olan 
		//findAll'i cagriyor, findAll'da otomatik olarak database'e gidiyor ve 
		//orda olan butun Student listesini cagriyor
	}

	public void createStudent(Student student) {
		if (studentRepository.existsByEmail(student.getEmail())) {
			//kullanicinin girdigi email bilgisi benim database'imde var mi diye kontrol etmem lazim
			//email ile unique gelmesini istedigim icin existByEmail yaptik
			//burada exception eklenecek(handle)
			throw new ConflictException("Email is already exist!");
			//burda(^) ConflictException class'indaki message yerine gidecek olan mesaj
			
		}
		studentRepository.save(student);
	}
	//find Student By ID
	public Student findStudent(Long id) {//bu method optional'dir senin bunu mutlaka handle etmen lazim
		return studentRepository.findById(id).
				orElseThrow(()->new ResourceNotFoundException("Student not found with id : "+id)); 
	}

	public void deleteStudent(Long id) {
		Student student =findStudent(id);
		studentRepository.delete(student);
	}

	//update student
	public void updateStudent(Long id, StudentDTO studentDTO) {
		boolean emailExist=studentRepository.existsByEmail(studentDTO.getEmail()); 
		//(^)bu bilginin false olmasi benim database'imde bu email'in olmadigini gosterir
		Student student=findStudent(id); //anlik olarak giris yapan kullanici bilgilerini Student objesine set ediyorum
		//email exist mi? ve anlik olarak gelen kullaniciya mi ait bunun kontrolu
		if (emailExist && !studentDTO.getEmail().equals(student.getEmail())) {
			//??ilk kısım:yeni email data'da var mi ve ikinci kisim: baskasinin emailini girerse(not true) exception firlatir
			throw new ConflictException("Email is already exist");
		}
		
		student.setName(studentDTO.getFirstName());
		student.setLastName(studentDTO.getLastName());
		student.setGrade(studentDTO.getGrade());
		student.setEmail(studentDTO.getEmail());
		student.setPhoneNumber(studentDTO.getPhoneNumber());
		
		studentRepository.save(student);
	}

	public Page<Student> getAllWithPage(Pageable pageable) {
		return studentRepository.findAll(pageable);
		
	}
	
	
	
	
	

}
