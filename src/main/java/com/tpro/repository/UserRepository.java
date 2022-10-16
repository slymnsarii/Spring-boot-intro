package com.tpro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpro.domain.User;
import com.tpro.exception.ResourceNotFoundException;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	//User'lari UserName ile DB'den bulunmasini saglayan method
	Optional<User> findByUserName(String userName) throws ResourceNotFoundException;
	//(^)Return type'i bir user olmasi lazim ama yoksa NullPointerException'dan kurtulmak icin findBy'li
	//method'larda Optional yapiyoruz, donerse al, ama donmezse exception firlat
	
}
