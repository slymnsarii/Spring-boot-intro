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

@Repository //Component islemlerini de bu yapar
public interface StudentRepository extends JpaRepository<Student, Long>{
	//T(ilk):POJO class'in ismi, ID(ikinci):POJO class'indaki ID'yi hangi data turu ile set ettim onu istiyor
	/*
	 JpaRepository icinde olan method'lar:
	 1-findAll():DB'deki butun ogrencileri getirmek icin
	 2-findAllById():id ile ilgili bir sey istiyorsam bu method'u kullanirim
	 3-findAll(Sort sort):stundet'lari parca parca cekmek icin (JpaRepository extends PagingAndSortingRepository)
	 4-save():kaydetme(CRUD) (PagingAndSortingRepository extends CrudRepository)
	 5-findById():herhangi bir student'i id ile isteme (PagingAndSortingRepository extends CrudRepository)
	 6-existById(ID id): bu id'li student DB'de var mi (PagingAndSortingRepository extends CrudRepository)
	 7-count():bu ozellikte kac tane student var DB'de (PagingAndSortingRepository extends CrudRepository)
	 8-deleteById(ID id):bu id'li student'i sil (PagingAndSortingRepository extends CrudRepository)
	 */
	
	boolean existsByEmail(String email); 
	//Spring Data JPA icinde existById() var fakat Spring Data JPA bize sondaki eki istedigimiz degisken ismi ile
	//degistirmemize izin veriyor(ByEmail, ByGreade..), mevcut method'u bu sekilde turetebiliyoruz.

	List<Student> findByLastName(String lastName);//findBy turetilebilir bir method oldugu icin arka planda 
	//parametre olan argument olarak ekledigim lastName'i aliyor DB'den gidip ordan alip getiriyor

	//JPQL ile yazalim:	
	@Query("SELECT s from Student s WHERE s.grade=:pGrade") 
	//(^)kendi query'imi yaziyorum //pGrade alttaki (@Param("pGrade")Integer grade)'deki grade'den aliyor
	//pGrade'i asagidaki grade'e map'lemek icin @Param("pGrade) ekliyorum(=: bi yerden variable alacagim demek)
  //yani Integer grade'deki grade, @Param("pGrade")'daki PGrade'e, PGrade'de  Query icindeki =:pGrade'e gomuyor
	//yani sunu yapmis oluyorum--> pGrade=grade
	
	List<Student> findAllEqualsGrade(@Param("pGrade")Integer grade);
	
	//Native Query(SQL)
		@Query(value="SELECT * from Student s WHERE s.grade=:pGrade", nativeQuery=true)
		List<Student> findAllEqualsGradeWithSQL(@Param("pGrade")Integer grade);
		
		
	//JPQL ile:
		@Query("SELECT new com.tpro.dto.StudentDTO(s) FROM Student s WHERE s.id=:id")
		//(^)burda StudentDTO'da yaptigim map'leme isini JPQL ile burda yapiyorum
		//StudentDTO(s)'deki (s) Student'in as edilmis hali yani oraya Student'i gomuyorum
		Optional<StudentDTO> findStudentDTOById(@Param("id")Long id); 
	//(^)Eger repo'dan gelen student'i DTO'ya cevirmek istersem findStudentDTOById method'unu her yerde kullanirim
		//Optional: ya gelmezse ya olmazsa anlaminda orElseThrow'dan dolayi	
	
		
		
}

	
