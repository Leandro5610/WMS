	package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.util.Optional;

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

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Turma;
import senai.sp.cotia.wms.repository.TurmaRepository;

@RestController
@CrossOrigin
@RequestMapping("api/turma")
public class TurmaRestController {
	@Autowired
	private TurmaRepository repo;
	
	
	
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object cadastrarMedida(@RequestBody Turma turma) {
		try {
			// salvar o usuário no banco de dados
			repo.save(turma);
			return ResponseEntity.ok(HttpStatus.CREATED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Turma> getTurma(@PathVariable("id")Long idTurma){
		Optional<Turma> turma = repo.findById(idTurma);
		if(turma.isPresent()) {
			 return ResponseEntity.ok(turma.get());
		 }else {
			 return ResponseEntity.notFound().build();
		 }
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deletaUnidades(@PathVariable("id") Long idTurma) {
		repo.deleteById(idTurma);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizaTurma(@PathVariable("id")Long id, @RequestBody Turma turma){
			if(id != turma.getId() ) {
				throw new RuntimeException("id invalido");	
			}
				repo.save(turma);
				HttpHeaders header = new HttpHeaders();
				header.setLocation(URI.create("/api/turma"));
				return new ResponseEntity<Void>(header, HttpStatus.OK);
				
	
	}
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Turma> listAluno(){
		return repo.findAll();
	}
	// metodo para procurar uma reserva à partir de qualquer atributo
		@RequestMapping(value = "/findbyall/{p}")
		public Iterable<Turma> findByAll(@PathVariable("p") String param) {
			return repo.procurarTudo(param);
		}

}
