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


@Configuration //Configuration yapacagimi soyluyorum, bu class'im configuration class'i diyorum
@EnableWebSecurity //WebSecurity'imi enable hale getir diyorum
@EnableGlobalMethodSecurity(prePostEnabled = true) //security'm method method guvenligi saglayip calissin istiyorsam(yani su method'lari admin'ler su method'lari student'lar yapsin gibi)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception { //bu method WebSecurityConfigurerAdapter'den geliyor
//dinamik bir web sayfasi yapmayacaksak,csrf'i disable yapmamiz lazim, cunku put methodlarini error verdiriyor
//sanki yaptigimiz application'in dinamik web sayfasi yapmisiz gibi davranmaya calisiyor SpringSecurity,
//guvenlik olarak sorumlulugu biz aliyoruz
		//crsf:mesela browser'da banka sayfasina girdin, ayni zamanda yanda farkli tab actiniz mail'i,
		//art niyetli olanlar size bir link gonderip mail'e ona tiklarsak yan tarafta ayni section icinde
		//diger tab'da acik olan banka bilgilerine girerek onlara ulasabilir
		//security'de bana gelen http  request'lerini authorize etmem lazim
		http.csrf().disable(). //Api urettigimiz icin crsf'e gerek kalmiyor, put method'lari duzgun calissin diye(csrf() korumasi iptal edildi API'de ihtiyac olmadigi icin
		authorizeHttpRequests(). //bana gelen butun HttpRequests'leri authorize et
		antMatchers("/","index.html","/css/*,/js/*").permitAll().//bu endpoint'lere security uygulanmasin //yukarida ve asagida yazdiklarindan muaf tut
		anyRequest(). //yukaridakilerin disindikilerle gelirsen hepsini
		authenticated(). //authenticate et
		and(). //yukaridakileri yap daha sonra and'den sonra gelenleri yap
		httpBasic(); //yukaridakilerin hepsini Basic autheptication yapacagimi belirttim
		//(^)Basic:her requestim'de kullanici adi ve sifre ile gelicem ve sifremi hep endcode etmem lazim
		
	}
	
	//InMemory olarak user'lari olusturuyoruz
//(^) gecici objeler, herhangi bir yere kaydolmuyor, herhangi bir DB'ye ihtiyac duymayan programla beraber ayaga kalkan,durdugu anda duran
	@Override
	@Bean //Springboot'un icerisinde donen degeri(UserDetailsService) method'unu kullanabilmem icin @Bean kullanilir(obje olarak kullanabilmem icin)
	//Ben class'i kendim olusturmuyorsam, bu class'dan hazir obje uretmesini istiyorsam buna @Bean derim(baskasinin method'unu override ediyorum)
	//Bu benim yazdigim method olsaydi burayi @Override etmezdim ve @Component eklerdim
	protected UserDetailsService userDetailsService() { //SpringSecurity'nin anlayacagi user objelerini olusturmamizi sagliyor
		UserDetails userIsmail= User.builder(). //SpringSecurity user'dan anlamaz, user'lari UserDetails ile girmemi ister //burdaki User domain'deki user degil(SpringSecurity_core dosyasindan geliyor)
				username("ismail").
				password(passwordEncoder().encode("akdogan")). //Springboot sifreyi encode olarak ister
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
		//burada parametreli constructor donduruyorum
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() { //sifreyi encoder etmesi icin PasswordEncoder method'unu kullaniyoruz
		return new BCryptPasswordEncoder(10); //10 defa encode etsin
	}
	
	
	
	
	
	
	
	
	
}
