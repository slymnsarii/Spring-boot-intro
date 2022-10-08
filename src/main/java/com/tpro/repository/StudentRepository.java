package com.tpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpro.domain.Student;

//repository database ile iletisime gecer, yani SQL datalarimi(query'lerimi) burda yazmam lazim,
//onu da JpaRepository kutuphanesinden yapiyorum boylece SQL kodlari yazmama gerek kalmayacak
//Ancak JpaRepository'de olmayan ozel bir query yapacaksam o zaman burda SQL query kodunu yazacagim

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
	//T(ilk):POJO class'in ismi, ID(ikinci):POJO class'indaki ID'yi hangi data turu ile set ettim onu istiyor
	
	boolean existsByEmail(String email); 
	//Spring Data JPA icinde existById() var fakat Spring Data JPA bize sondaki eki istedigimiz degisken ismi ile
	//degistirmemize izin veriyor
	

}
