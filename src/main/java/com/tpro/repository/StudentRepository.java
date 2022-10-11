package com.tpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tpro.domain.Student;
import com.tpro.dto.StudentDTO;

//repository database ile iletisime gecer, yani SQL datalarimi(query'lerimi) burda yazmam lazim,
//onu da JpaRepository kutuphanesinden yapiyorum boylece SQL kodlari yazmama gerek kalmayacak
//Ancak JpaRepository'de olmayan ozel bir query yapacaksam o zaman burda SQL query kodunu yazacagim

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
	//T(ilk):POJO class'in ismi, ID(ikinci):POJO class'indaki ID'yi hangi data turu ile set ettim onu istiyor
	
	boolean existsByEmail(String email); 
	//Spring Data JPA icinde existById() var fakat Spring Data JPA bize sondaki eki istedigimiz degisken ismi ile
	//degistirmemize izin veriyor(ByEmail, ByGreade..), mevcut method'u bu sekilde turetebiliyoruz.

	List<Student> findByLastName(String lastName);

	//JPQL ile yazalim:
	@Query("SELECT s from Student s WHERE s.grade=:pGrade") 
	//(^)kendi query'imi yaziyorum //pGrade alttaki (@Param("pGrade")Integer grade)'den alıyor
	List<Student> findAllEqualsGrade(@Param("pGrade")Integer grade);
	
	
	//Native Query(SQL)
		@Query(value="SELECT * from Student s WHERE s.grade=:pGrade", nativeQuery=true)
		List<Student> findAllEqualsGradeWithSQL(@Param("pGrade")Integer grade);
	//JPQL
		@Query("SELECT new com.tpro.dto.StudentDTO(s) FROM Student s WHERE s.id=:id")
		Optional<StudentDTO> findStudentDTOById(@Param("id")Long id); //Optional: ya gelmezse orElseThrow'dan dolayi
}

	
