package com.tpro.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data //@RequiredArgsConstructor'i getirir bu da sadece gerekli seyleri getirir(userName,password gibi)
//Ama ben burda @RequiredArgsConstructor'i kullanmak istemiyorum o yuzden @Data kullanmam sikinti yaratir
//Boylece POJO class'im gereksiz yere buyuyor
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_user") //Tablomun class adi ile degil de kendi belirledigim isimle olusmasini istersem
@Entity //POJO class'im DB'de bir tablo ile eslenecek(maplenecek)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 25, nullable = false)
	private String firstName; //burdaki firstName ile lastName POJO class(Student)'imdaki firstName ile lastName olacak
	
	@Column(length = 25, nullable = false)
	private String lastName;
	
	@Column(length = 25, nullable = false, unique = true) //her userName'in ismi farkli olsun
	private String userName; //Bu User'a userName uzerinden ulasmak istersek hazir method'lar tarafindan
	
	@Column(length = 255, nullable = false)
	private String password;
	
	
	/*Burda Set yerine List kullansaydim 2.admin'i ekledigimde 2 admin gorunur, 
	ama Set yaparsam ilkini siler, ikinciyi yeniden atiyor yani 2.admin'i koymuyor oraya
	Set interface oldugu icin, esitligin sag tarafina set diyemeyiz, 
	o interface'den turetilen concrete class'lardan yazmamiz lazim(HashSet gibi)
	*/
	
	/*
	 User->Bir kullanici hem user hem admin olabilir=>OneToMany
	 Role-->Admin rolunu birden fazla kullanici alabilir=>OneToMany
	 O yuzden ikisini de burda @ManyToMany ile iliskilendiririm
	 */
	
	/*
 ManyToMany'de 3.tablo icin(user'daki ile role'deki id'lerin eslesmesi icin) Bunu saglayan @JoinTable'dir.
@JoinColumns; Student'ta hangi field'i alicam, bide karsi tarafta(role) hangi field'i alicam o ise yariyor
inverseJoinColums; karsi taraftaki(role) POJO class'a git, ordaki hangi colomn'u burayla map'layecegimi istiyor
	 */
	@JoinTable(name="tbl_user_role",joinColumns =@JoinColumn(name="user_id"),inverseJoinColumns =@JoinColumn(name="role_id"))
	@ManyToMany(fetch = FetchType.EAGER)//birden fazla rolu oldugu zaman cekmem gerektiginde hepsi bana gelsin(Fetch)
	//Cok fazla tablo kullanacaksam LAZY kullanirim cunku EAGER bir tablonun butun Forekey,Primekey,
	//rollerini, book'larini her seyi getirir o yuzden eager ihtiyacsa yazariz
	private Set<Role>roles=new HashSet<>();
	
	@JsonIgnore
	@OneToOne(mappedBy = "user") //Her bir student bir tane user oldugu icin
	private Student student; //User'la student'i iliskilendirmek icin
}
