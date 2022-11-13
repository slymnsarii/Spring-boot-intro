package com.tpro.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tpro.domain.Student;
import com.tpro.dto.StudentDTO;
import com.tpro.service.StudentService;
//controller'in isi request'den gelen istekleri  service'e gondermek
//controller katmaninda logic islemler yapamayiz, logic islemler service katmaninda yapilir
@RestController //proje RestFullAPI(Rest mimari ile uretildigi icin) oldugu icin
@RequestMapping("/students") //gelen requestleri "/students" ile maple
public class StudentController {
	//kullanicinin aldigi hatayi log dosyasina gidip hangi saat'te aldigina bakip ona gore sorunu cozuyoruz
	//logglamayi hangi method'da istiyorsam o method'un class'inda yapiyorum
	//logglama; hata kayitlarini tutar hatanin nerden geldigini(hibernate vs.) tutar,
	//hangi seviyede logglama yapmak istedigimizi biz belirtiyoruz
	//(Trace, debug, info, warn, error, fatal)logglamayi info yaparsan info ve yukarisini logglar
	Logger logger = LoggerFactory.getLogger(StudentController.class); //LoggerFactory:logger ureten fabrika
		

	@Autowired //@Autowired new'leme yapmadan obje olusturuyor; suanda StudentService turunde studentService adinda bir obje bean olarak 
	//olusturulmaya aday olarak IOC containerinda duruyor, ben bunu istedigim anda(ihtiyac duydugum anda) 
	//Singleton Dizayn Pantern'e giderek bir tane ureterek bana verecek
	//(@Autowired annotation ile dependency injection yapmış oluyoruz, IOC containerda bulunan 
	//StudentService data türündeki objeyi new keywordunu kullanmadan uygulama içinde kullanmamı sağlıyor)
	private StudentService studentService;
	
	/* logger icin yoruma aldim
	@GetMapping("/welcome") //localhost:8080/students/welcome 
							//(bana bu endpointle gelirsen bu asagidaki method'u calistir) //Get bilgi ister
	public String welcome() { //bean (obje) olusturduk
		return "Welcome to Student Controller";
	}
	
	logglama icin asagiya yaptim aynisini
	*/
	
	@GetMapping("/welcome")
	public String welcome(HttpServletRequest request) {//HttpServletRequest:requestlere ulasmak icin
		
		logger.warn("-----------Welcome{}",request.getServletPath());
		return "Welcome to Student Controller";
	}
	 
	// Get All Students
	@GetMapping //herhangi bir endpoint girmezsem yukardaki "/students" i alir(@RequestMapping'deki)
	@PreAuthorize("hasRole('ADMIN')") //bu method'u sadece ADMIN'ler calistirsin
	
	/*
	 Burda donen bir method girmem gerek.Bana bir request geldigi zaman client'e response olarak 
	 https status kodunu gondermem lazim(200,300..) ve kullaniciya bir mesaj gondermem lazim(kullanicin istedigi bilgiler)
	 Bunun icin Springboot'da ResponseEntity diye bir yapi var.
	 Controller kisminda yazdigimiz method'larin hemen hemen hepsinde ResponseEntity yapisini kullaniriz.
	 ResponseEntity diamond operatoru ile calisir
	 
	 */
	public ResponseEntity<List<Student>> getAll() {//yapinin esnek olmasi icin array yerine collections'lari
		List<Student> students = studentService.getAll(); // kullaniyoruz(List)
		//(^)Controller service'e gidecegi icin, gitmesini de @Autowired ile service'e injekte ederek yaparak gidecegiz
		//burda map<> de donebiliriz ama projelerin %90'inda ResponseEntity kullanilir
		//getAll() method'undan bana exception gelecek mi kontrolunu service'de kontrol ediyoruz
		return ResponseEntity.ok(students); 
	}
	//ayni endpoint'e ayni mapping ile gelemezsiniz yani bi daha alta default endpoint icin @GetMapping yazamazsin
	//cunku endpoint ile method ayni olur ve karisir sistem
	
	//endpoint'i postman'da test edemeyiz cunku db bos, o yuzden test edebilmek icin create method'u olusturuyoruz
	
	//Create new Student
	@PostMapping ////herhangi bir endpoint girmezsem yukardaki "/students" i alir(@RequestMapping'deki)
	//bir kisi "/student" endpoint'i ile get methodu'yla gelirse @GetMapping, post ile gelirse @PostMapping calisir
	@PreAuthorize("hasRole('STUDENT')")//bu method'u sadece STUDENT'lar calistirsin
	public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody Student student){
		//RequestBody:Bana gelen request'in body'sindeki data'yi(JSon) istiyorum demek
		//@RequestBody Student student(@RequestBody calismasi):request'en gelen JSon'i bendeki student'a maple(ata),
		//Boylece Student'a JSon'i POJO class'ina eklemis oluyorum
		//@Valid:request'ten gelen JSon'in bendeki Student POJO'mo uyuyor mu diye bakiyor(bu ogrencinin bilgilerini kontrol et(@Notnull vs.))
		studentService.createStudent(student); //service'e gondermek icin, icine student yazdik cunku create edilebilmesi icin POJO class'i lazim
		Map<String, String> map =new HashMap<>();
		map.put("message","Student is created succesfuly");
		map.put("status","true"); //test olarak duzgun olarak olustu demek(kullaniciya mesaj)
		return new ResponseEntity<>(map,HttpStatus.CREATED);
		
	}
	
	//Get a Student by ID by RequestParam
	@GetMapping("/query") //localhost:8080/students/query?id=1 benim istedigim id'yi endpoint'den cagirmak istersem
	//endpoint'imde bir id ile @GetMapping geliyor, endpoint'inde gorunen yazdigimiz id ile database'e gidip,
	//Student'i bulup on tarafa gonderiyoruz
	@PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")//bu method'u hem ADMIN'ler hem STUDENT'lar calistirsin
	public ResponseEntity<Student> getStudent(@RequestParam("id") Long id){
		//(^)getStudent method'unun @RequestParam ile gelecegini soyluyorum
		//@RequestParam:@GetMapping("/query") ile @RequestParam("id")'daki id'yi map'ler
		//(^)benim aldigim query'yi POJO class'indaki Long data turundeki id'ye map'liyoruz(set ediyoruz)
		//birden fazla deger alacaksam @RequestParam kullanirim(postman'de aralarinda & kullanarak)
		
		Student student =studentService.findStudent(id); //burda student data turunde student isminde doner
		//(^)gelen id'yi studentService kismina refer ederek ordan method'u cagriyoruz
		//bu method(findStudent); bu id varsa eger Student data turundeki student degiskenine atayacak
		
		return ResponseEntity.ok(student);//istedigi id'yi ResponseEntity'e gonderiyorum
		
	}
	
	
	//Get a Student by ID by PathVariable(tek bir sey istedigimde PathVariable kullanirim)
	
	//Postman uzerinde sorgulama yapilirken @PathVariable ile birden fazla deger alinabilir.
	//DB uzerinden bir tane veri cekmek isteniyorsa @PathVariable kullanilir.
	@GetMapping("/{id}") //localhost:8080/students/1
	@PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
	public ResponseEntity<Student> getStudentWithPath(@PathVariable("id") Long id){//calisma prensibi param gibi
	
		Student student =studentService.findStudent(id); 
		return ResponseEntity.ok(student);
	}
	
	//Delete Student
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable("id") Long id){
		studentService.deleteStudent(id); //service class'ina gonderiyorum
		Map<String, String> map =new HashMap<>();
		map.put("message","Student is deleted succesfuly");
		map.put("status","true"); //test olarak duzgun olarak olustu demek(kullaniciya mesaj)
		return new ResponseEntity<>(map,HttpStatus.OK);
		
		/*OR
		 @DeleteMapping("/{id}")
	public ResponseEntity<Student> deleteStudents(@PathVariable("id")Long id){
		Student student=studentService.deleteStudent(id);
		return ResponseEntity.ok(student);
	}
		 */
		
	}
	
	//Update Student, DTO kulllanilacak
	@PutMapping("{id}") //  localhost:8082/students/1
	public ResponseEntity<Map<String, String>> updateStudent(@PathVariable("id") Long id, @RequestBody StudentDTO studentDTO){
												//@RequestBody StudentDTO studentDTO:JSON datalari eklemek icin
		
		studentService.updateStudent(id,studentDTO); //service'e gonderiyoruz
		Map<String, String> map =new HashMap<>();//bu map satirlari on tarafa mesaj gondermek icin
		map.put("message","Student is updated succesfuly");
		map.put("status","true"); 
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	//pageable(dataları parcalara ayirmak icin)
	@GetMapping("/page")
	//Asagidaki talep frondend tarafindan gelecek
	public ResponseEntity<Page<Student>> getAllWithPage(@RequestParam("page")int page,//kac sayfa(hangi sayfa)
														@RequestParam("size")int size,//her sayfada kac tane
														@RequestParam("sort")String prop,//siralama(alfabe)
														@RequestParam("direction")Direction direction//dogal siralama
														){
		Pageable pageable=PageRequest.of(page,size,Sort.by(direction,prop));
		Page<Student>studentPage=studentService.getAllWithPage(pageable);
		//studentService katmanindan getAllWithPage method'unu cagriyorum, icerisine arguman olarak
//yukarida olusturdugum pageable objesini atiyorum, pageable icerisinde yukarda olusturdugum kisitlamalar var
		//Bu ozelliklerle studentService katmanindan bir sey istiyorsam buna karsilik olarak POJO class'i
		//student oldugu icin student'tan gelecek ama list formatinda[] köseli parantez olarak gelmemesi lazim cunku pageabla atiyorum
		//Dolayisiyla donen yapi Page generic yapisinda icerisinde Student olan objelerin oldugu yapi gelecek
		
//Client(on taraf, frontend) side pageable:Server'dan butun datalari client'e gonderme(cok mantikli degil)
//Server(backend arka taraf(controller,servis,repo,DB..)) side pageable:binlerce data'yi page'lere bolmek backend'de

		return ResponseEntity.ok(studentPage); //on tarafa da ResponseEntity'i ok diyerek 200 kodu icinde de 
												//argument olarak studentPage gonderiyorum
		
	}
	
	//ayni endpoint'e farkli get put ile gidebilirim ama ikiside get ise farkli endpoint vermek daha mantikli
	
	//Get By LastName
	@GetMapping("/querylastname") // student/querylastname //kullanici bana soyismi "kaya" olanlari getir derse
	public ResponseEntity<List<Student>> getStudentByLastName(@RequestParam("lastName")String lastName){ 
		//ayni soyisimden cok fazla olmaz diye page yapmadik,list yaptik
		List<Student> list=studentService.findStudent(lastName);
		//(^)Burda esitligin sag tarafi once yazilir cunku burda studentService'den bir method cagirmam lazim,
		//dolayisiyla lastname'leri istedigim icin Student'lar donecek o yuzden sol tarafa liste seklinde
		//Student'lari yaziyorum
		return ResponseEntity.ok(list);
	}
	
	//GetAllstudentsBygrade(JPQL)
	@GetMapping("/grade/{grade}") //localhost:8080/students/grade/60 //@PathVariable dedigim icin {grade} yazdim
	public ResponseEntity<List<Student>>getStudentsEqualsGrade(@PathVariable("grade")Integer grade){
													//(^)burdaki grade Getmapping icindeki {grade}'den geliyor
											  //POJO class'imdaki grade integer old. icin Integer'a mapp'liyoruz
		List<Student>list=studentService.findAllEqualsGrade(grade);
		return ResponseEntity.ok(list);
	}
	
	//DB'den direk DTO olarak datami alsam?
	@GetMapping("/query/dto")
	public ResponseEntity<StudentDTO> getStudentDTO(@RequestParam("id")Long id){
		StudentDTO studentDTO=studentService.findStudentDTOById(id);
   return ResponseEntity.ok(studentDTO); //parantez icinde bi ust satirda ne donmesini istiyorsam onu giriyorum
	}
	
	/*
	 
	 //studentDTO constructorini sorgu yaparken degil getmapping icinde kullandik.Buna olanak saglayan yapi da
      // @ResponseBody yapisidir.
        
      
        @GetMapping("/rest-service/student/{id}")
        @ResponseBody
        public StudentDTO getStudentsWithService(@PathVariable Long id) {
            Student student=studentService.findStudent(id);
            StudentDTO studentDTO=new StudentDTO(student.getName(),student.getLastName(),student.getGrade(),
                    student.getPhoneNumber(),student.getEmail());
            return studentDTO;
        }
	 
	 DTO'da devami
	 */
	
	
	
	
	
}










