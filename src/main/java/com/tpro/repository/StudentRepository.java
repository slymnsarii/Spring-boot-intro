package com.tpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpro.domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

	boolean existsByEmail(String email); //repository database ile iletisime gecer
	//T(ilk):POJO class'in ismi, ID(ikinci):POJO class'indaki ID'yi hangi data turu ile ...?

}
