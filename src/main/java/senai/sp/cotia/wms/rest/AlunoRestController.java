package senai.sp.cotia.wms.rest;


import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.repository.AlunoRepository;

@RestController
@RequestMapping("api/aluno")
public class AlunoRestController {
	@Autowired
	private AlunoRepository repository;
	
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveAluno(@RequestBody Aluno aluno, HttpServletRequest request,
			HttpServletResponse response, HttpSession session){
			try {
				repository.save(aluno);
				return ResponseEntity.ok(HttpStatus.CREATED);
			} catch (Exception e) {
				// TODO: handle exception
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
		return repository.findAll();
	}

	// metodo para procurar uma reserva à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}")
	public Iterable<Aluno> findByAll(@PathVariable("p") String param) {
		return repository.procurarTudo(param);
	}
}

