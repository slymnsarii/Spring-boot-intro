package com.tpro.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity //DB'de tablo ile iliskilendirmek icin
@Setter
@NoArgsConstructor
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonProperty("bookName") //Kod kisminda name olarak kalsin ama 
	//JSON formatinda bunun adinin bookName olmasini saglamak(Student'daki name ile karismasin diye)
	//kodda koyacagin isim aciklayici olabilir o yuzden kisa bi isim yazip, on tarafta(JSON) isim veriyoruz
	private String name;
	
	@JsonIgnore////sonsuz döngüye girmemesi için(JSON yapiyor bunu) kısaca burada yeniden
	//student classına gitmemesi için bu annotationu kullandık
	//burda stackoverflow olmaması için yaptık. student-book arası gidip gelmemesi için
	
	
	//bir student'in birden fazla kitabi olsun istiyorum yani many-->book, one-->student tarafinda olur
	@ManyToOne//bir student'in birden fazla kitabi olsun istiyorsam
	@JoinColumn(name="student_id") //buraya student_id tablo'sunu olusturun diyoruz
	//(^)joinColumn ici foregin key oluyordu, o yuzden diger tarafta student_id ile iliskilendiriyoruz
	//student_id bilgisi asagidaki student'a set ediyoruz
	private Student student; //bu class'i student objesi ile iliskilendirmek icin(maplemek,birlestirmek)

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Student getStudent() {
		return student;
	}
	
	
}
