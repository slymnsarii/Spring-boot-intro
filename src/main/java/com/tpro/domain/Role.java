package com.tpro.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tpro.domain.enums.userRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_role")
@Entity
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING) //Normalde enum degerleri int olarak kaydedilir(1,2,3.. gibi), 
	//(^)Tablo'ya enum int degeri ile degil STRING ifade ile kaydedilsin istiyorsam, 
	//(^)burda role tablo'mu(Role POJO class'imi) enum type ile birlestirdim
	@Column(length = 30, nullable = false)
	private userRole name;
	
	@Override
	public String toString() { //toString geldigi zaman ne cikacak onu kendim yapmak istiyorum
		
		return "Role[name=" + name+"]"; //Role class'inin toString'ini calistirirsam ekrana 
										//"Role[name=" + name+"]" olarak userRole'un name'i dinamik gelecek 
	}
}
