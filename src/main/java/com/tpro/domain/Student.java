package com.tpro.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.tpro.domain.enums.userRole;

import io.micrometer.core.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter //bir degerin setter degerlerini istemiyorsak bunu sileriz
@AllArgsConstructor //butun parametreli constructor'lar
@NoArgsConstructor //parametresiz constructor

//pom dosyasindaki <optional>true</optional> true olmasi-->bu proje canliya cikana kadar lombok kullanilacak,
//canliya ciktigi anda artik lombok'u kullanmaya ihtiyac yok, canliya ciktigi anda benim POJO class'imda 
//zaten getter ve setterlar fiziksel olarak olmali

//Hibernate ile olan iliskisini saglamak icin @Entity kullaniriz
@Entity //tablo olusturuyor, column'lardaki tablo basliklarini ayarliyor asagidakilerle(name,lastname vs.)
public class Student {
	
	@Id //Hangisinin unique olmasini saglamam lazim(id'yi seciyoruz,
		//id'yi otomatik olarak hibernate'nin olusturmasini istiyoruz)
		//@Id hibernate'e "POJO class'indaki private Long id olarak tanimladigim variable
	    //senin tablondaki unique olan id'ye denk geliyor" bilgisini veriyor
	
	@GeneratedValue(strategy = GenerationType.IDENTITY) //id'yi otomatik olarak hibernate'nin olusturmasini 
													//istiyoruz, unique olarak, icerisine de hangi strateji ile
														//bu id'yi uretsin istiyorsam ona gore yaziyorum
	
	private Long id; //null --bos birakirsam id'yi  --null old. icin exception firlatmaz
			//Wrapper class kullanmamizin sebebi eger deger atanmazsa null olarak tanimlansin
	//int olarak tanimlasaydik default olarak 0 degeri verecekti ve ikinci bir id girdigimde s??k??nt?? olurdu
	//private int id; //0 --id vermezsen 0 atar ve exception firlatir
	
	@NotNull(message = "first name can not be null") // null deger olmasini istemiyorsam (API'deki validation controlu)
	@NotBlank(message = "last name can not be white space") //bosluk karakteri girmesin diye, sadece String'ler kullanir (API'deki validation controlu)
	@Size(min = 2,max = 25, message = "First name '${validatedValue}' must be between{min} and {max} long") //(API'deki validation controlu)
	//${validatedValue}' ibaresi kullanicinin girmis oldugu degeri buraya getiriyor
	@Column(nullable = false, length=25)//@Column:kullandigimiz field'lerin tabloda bir column'a denk gelmesini istiyorsam
	//nullable kullan??c?? name'e illa bir seyler girsin diye, lenth de en fazla girecegi karakter(hibernate controlu)
	private String name;
	
	@Column(nullable = false, length=25)
	private String lastName;
	
	@Column
	private Integer grade;
	
	@Column(nullable = false, length=50, unique = true) //unique olmasi ayni email'lerin girilmesini onlemek
	@Email(message = "Provide valid email") //email'in belli ozelliklerde olmasini istiyorsam ve   (API'deki validation controlu)
											//yanlis degerde email girerse bir mesaj vermek icin
	private String email;
	
	@Column
	private String phoneNumber;
	
	@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="MM/dd/yyyy HH:mm:ss", timezone="Turkey")
	//(^)JSON formatinda duzgun cikti gelsin istiyorsam time'i
	private LocalDateTime createDate=LocalDateTime.now(); //islemin yapildigi anda yapilmasini istersem
	
	@OneToMany(mappedBy = "student") //mapped by ile buraya student tablo'sunu olusturma dedik
	private List<Book>books=new ArrayList<>(); //bir student'in birden fazla kitabi olacagi icin 
					 //collection kullaniyoruz(books isminde list data turunde bir degisken olusturuyoruz)
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;//User class'i ile iliski kurmak icin
	
}
