package com.tpro.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tpro.domain.Role;
import com.tpro.domain.User;
import com.tpro.exception.ResourceNotFoundException;
import com.tpro.repository.UserRepository;



@Service
public class UserDetailServiceImpl implements UserDetailsService{
	//(^)UserDetailsService interface oldugu icin bu interface'in icindeki method'lari override etmeliyiz

	@Autowired //Benim yapimdaki user'lari gonderebilmem icin,
	UserRepository userRepository; //UserRepository class'ina enjekte etmem gerek
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//DB'deki user'larin gerekli olan bilgileri bu method'un icine gomulecek
		//Benim yapimdaki user'lari bu method'un icine gonderip, bu method sayesinde de user'larimi,
		//UserDetails olarak bize dondurecek
		//Burda projemiz geregi username olarak gelmis ama gercek projede Email uzerinden islem yurur
		
		User user=userRepository.findByUserName(username).orElseThrow(//burdaki User ile if'in icindeki user ayni degil(asagidaki security userdetails'in user'i)
										()-> new ResourceNotFoundException("user not found username"+username));
		//(^)username uzerinden DB'ye gidip bu username isminde bir user var mi diye sorgulamalar yapmam lazim
		//varsa POJO User class'indan yani User data turunde user isimnde bir obje olustur ona ata ve bu user'i 
		//elde edip bunu UserDetails'e cevirmek amacim, user yoksa exception'i firlat ve  mesaji dondur
		
		//Bir user objemiz olusursa bu null olabilir eger null'sa else'in icindekini degilse if'nin icindekini dondur
		//Ama eger bana gelen user null degilse UserDetails'e cevirmem lazim
		if(user!=null) {//elime gelen user'in ozelliklerini kullanarak UserDetails turunde obje turetmek istiyorum,
			//bunu yapmak icin de SpringSecurity'nin package yapisindan yararlaniyoruz
			return new org.springframework.security.core.userdetails. //elimdeki User'i buraya gommem lazim
				User(user.getUserName(),user.getPassword(),buildGrantedAuthorities(user.getRoles()));
			//user icin userName, password, role'leri gondermem lazim, authentication icin bunlar onemli(firstName, lastName onemli degil)
			
		}else {
			throw new UsernameNotFoundException("User not found username"+username);
		}
	
		
	}
	
	//role ozelligi security katmaninda simpleGrantedAuthority yapisinda olmasi gerekiyor
	//bundan dolayi buildGrantedAuthorities method'unu olusturmamiz lazim SpringSecurity'nin anlamasi icin
	//bu method direk cagrilabilsin diye(obje uretip obje uzerinden cagrilmasin diye) static olarak urettik
	private static List<SimpleGrantedAuthority> buildGrantedAuthorities(final Set<Role> roles){
		List<SimpleGrantedAuthority> authorities=new ArrayList<>();
		for(Role role:roles) {//Role'e gidip sirasiyla admin,student vs. aliyorum tek tek
			//role enum yapisinda oldugu icin getName().name() yazdik(normalde getName() yeterli olurdu)
			authorities.add(new SimpleGrantedAuthority(role .getName().name()));
		}
		return authorities;
	}

}
