package senai.sp.cotia.wms.rest;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.MultipartFilter;

import com.google.firestore.v1.Write;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.repository.AlunoRepository;
import senai.sp.cotia.wms.util.FireBaseUtil;
@CrossOrigin

@RestController
@RequestMapping("api/aluno")
public class AlunoRestController {
	@Autowired
	private AlunoRepository repository;
	@Autowired
	private FireBaseUtil fire;
	
	
	
	
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveAluno(@RequestBody Aluno aluno, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException{
		
		
		String base64ImageString = aluno.getImagem().replace("data:image/png;base64,", "");
		byte[] decode = Base64.getDecoder().decode(base64ImageString);
		
		String foto = aluno.getImagem();
		
		File f = new File ("teste.png");
		FileOutputStream fos = null;
		
		
			try {
				 fos = new FileOutputStream (f);
				 fos.write (decode);
				repository.save(aluno);
			
				return ResponseEntity.ok(HttpStatus.CREATED);
			} catch (Exception e) {
				// TODO: handle exception
				if (fos != null) try { fos.close(); } catch (IOException ex) {}
				
				e.printStackTrace();
				return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
	}
	
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Aluno> findAluno(@PathVariable("id") Long idAluno, HttpServletRequest request, HttpServletResponse response){
		Optional<Aluno> aluno = repository.findById(idAluno);
		if(aluno.isPresent()) {
			return ResponseEntity.ok(aluno.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateAluno(@RequestBody Aluno aluno, @PathVariable("id" )Long id ){
		if(id != aluno.getId()) {
			throw new RuntimeException("Id Inválido");
		}
		repository.save(aluno);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/aluno"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteAluno(@PathVariable("id") Long idAluno){
		repository.deleteById(idAluno);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Aluno> listAluno(){
		ArrayList<Aluno> list = new ArrayList<Aluno>();
		list = (ArrayList<Aluno>) repository.findAll();
		System.out.println(list);
		return repository.findAll();
	}

	// metodo para procurar uma reserva à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}")
	public Iterable<Aluno> findByAll(@PathVariable("p") String param) {
		return repository.procurarTudo(param);
	}
}

