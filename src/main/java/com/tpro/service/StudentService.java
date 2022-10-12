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

	//create student
	public void createStudent(Student student) {
		if (studentRepository.existsByEmail(student.getEmail())) {
			//kullanicinin girdigi email bilgisi benim database'imde var mi diye kontrol etmem lazim
			//email ile unique gelmesini istedigim icin existByEmail yaptik
			//burada exception eklenecek(handle)
			throw new ConflictException("Email is already exist!");
			//burda(^) ConflictException class'indaki message yerine gidecek olan mesaj
			
		}
		studentRepository.save(student); //repository'e gonderiyorum(save method'u JPA'dan geldi)
	}
	//find Student By ID
	public Student findStudent(Long id) {//bu method optional'dir senin bunu mutlaka handle etmen lazim
		//istedigi id database'de var mi?varsa gonderecek yoksa exception firlatir
		return studentRepository.findById(id).
				orElseThrow(()->new ResourceNotFoundException("Student not found with id : "+id)); 
	//orElseThrow:boyle bir id yoksa exception firlat
		
//(^)studentRepository'ne git, findById method'unu cagir, (id) ile tarama yap, varsa dondur(return) yoksa
//orElseThrow'a git exception'i firlat mesaji gonder
	}

	public void deleteStudent(Long id) {
		Student student =findStudent(id);
		studentRepository.delete(student);
	}

	//update student
	public void updateStudent(Long id, StudentDTO studentDTO) {
		//once kullanicinin girdigi email DB'de var mi yok mu ona bakariz
		boolean emailExist=studentRepository.existsByEmail(studentDTO.getEmail()); 
		//(^)bu bilginin false olmasi benim database'imde bu email'in olmadigini gosterir
		
		Student student=findStudent(id); 
		//(^)anlik olarak giris yapan kullanici bilgilerini Student objesine set ediyorum
		//bu method student obj donduruyor, bu dondurdugu obje sisteme giris yapan ve guncelleme yapmak isteyen
		//kullanicinin DB'deki gercek bilgilerini getirecek
		
		
		//email exist mi? ve anlik olarak gelen kullaniciya mi ait bunun kontrolu
		if (emailExist && !studentDTO.getEmail().equals(student.getEmail())) {
			//kendi email'ini update edebilir sadece baskasina ait email olmamali
			//ilk kısım:yeni email data'da var mi(baskasinin emailini girerse)
			//ikinci kisim: kendi emailini girerse'nin 	kontrolu
			//1:yeni email DB'de var mi? 2:yeni diye girdigi email eski girdigi email mi
			throw new ConflictException("Email is already exist");
		}
		
		//burda DTO uzerinden gelen guncel bilgileri DB uzerine yaziyoruz
		student.setName(studentDTO.getFirstName());//ben burda setId yapmadigim icin id'yi degistiremez kullanici
		student.setLastName(studentDTO.getLastName());
		student.setGrade(studentDTO.getGrade());
		student.setEmail(studentDTO.getEmail());
		student.setPhoneNumber(studentDTO.getPhoneNumber());
		
		studentRepository.save(student); //bilgileri Repository'e kaydedip DB'e gonderiyoruz
	}


	public Page<Student> getAllWithPage(Pageable pageable) {
		return studentRepository.findAll(pageable);
		
	}

	public List<Student> findStudent(String lastName) {
		
		return studentRepository.findByLastName(lastName); 
		//direk repo'ya gonderiyorum cunku hazir method kullaniyorum, parametre olarak (String lastName)'deki
		//parametreyi gonderiyorum
	}

	public List<Student> findAllEqualsGrade(Integer grade) {
		
		return studentRepository.findAllEqualsGrade(grade);
	}

	public StudentDTO findStudentDTOById(Long id) {
		//bana id geldi, ben DB method'larini kullanirsam bana Student POJO class'i gelecek,
		//benim bunu studentDTO'ya cevirmem lazim
		return studentRepository.
				findStudentDTOById(id).
				orElseThrow(()-> new ResourceNotFoundException("Student not found with id: " + id)); 
					//burda kullanicinin girdigi id'yi buraya gonderiyorum +id ekleyerek
					//burdaki +id, istersem id'yi gosteririm istemezsem gostermem(yazmazsam +id'yi)
		//benden id istiyor ama id yoksa orElseThrow'u kullaniyorum	
		
	}
	
	
	
	
	

}
