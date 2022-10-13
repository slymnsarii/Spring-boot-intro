package com.tpro.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration //Configuration yapacagimi soyluyorum
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //security'mi ..
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception { //bu method WebSecurityConfigurerAdapter'den geliyor
		//dinamik bir web sayfasi yapmayacaksak, disable yapmamiz lazim, guvenlik olarak sorumlulugu biz aliyoruz
		http.csrf().disable(). //Api urettigimiz icin crsf'e gerek kalmiyor
		authorizeHttpRequests(). //bana gelen butun HttpRequests'leri authorize et
		antMatchers("/","index.html","/css/*").permitAll().//bu endpoint'lere security uygulansin //yukarida ve asagida yazdiklarindan muaf tut
		anyRequest(). //yukaridakilerin disindikilari
		authenticated(). //authenticate et
		and(). //
		httpBasic(); //Basic autheptication yapacagimi belirttim
		//(^)her requestim'de kullanici adi ve sifre ile gelicem ve sifremi hep endcode etmem lazim
		
	}
	
	//InMemory olarak user'lari olusturuyoruz(gecici objeler, herhangi bir yere kaydolmuyor)
	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		UserDetails userIsmail= User.builder().
				username("ismail").
				password(passwordEncoder().encode("akdogan")).
				roles("ADMIN").
				build();
		UserDetails userSuleyman=User.builder().
				username("suleyman").
				password(passwordEncoder().encode("sari")).
				roles("STUDENT").
				build();
		UserDetails userTarik=User.builder().
				username("tarik").
				password(passwordEncoder().encode("kose")).
				roles("STUDENT","ADMIN").
				build();
		
		return new InMemoryUserDetailsManager(new UserDetails [] {userIsmail,userSuleyman,userTarik});
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	
	
	
	
	
	
	
	
	
}
