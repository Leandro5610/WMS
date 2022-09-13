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
import senai.sp.cotia.wms.model.Professor;
import senai.sp.cotia.wms.model.Turma;
import senai.sp.cotia.wms.repository.ProfessorRepository;

@RestController
@CrossOrigin
@RequestMapping("api/professor")
public class ProfessorRestController {
	
	@Autowired
	private ProfessorRepository repo;
	
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object cadastrarProfessor(@RequestBody Professor professor) {
		try {
			// salvar o usuário no banco de dados
			repo.save(professor);
			return ResponseEntity.ok(HttpStatus.CREATED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Professor> getProfessores(@PathVariable("id")Long idUni){
		Optional<Professor> prof = repo.findById(idUni);
		if(prof.isPresent()) {
			 return ResponseEntity.ok(prof.get());
		 }else {
			 return ResponseEntity.notFound().build();
		 }
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deletarProfessor(@PathVariable("id") Long id) {
		repo.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizarProfessor(@PathVariable("id") Long id, @RequestBody Professor professor){
			if(id != professor.getId() ) {
				throw new RuntimeException("id invalido");	
			}
				repo.save(professor);
				HttpHeaders header = new HttpHeaders();
				header.setLocation(URI.create("/api/professor"));
				return new ResponseEntity<Void>(header, HttpStatus.OK);
	
	}
	// metodo para procurar uma reserva à partir de qualquer atributo
		@RequestMapping(value = "/findbyall/{p}")
		public Iterable<Professor> findByAll(@PathVariable("p") String param) {
			return repo.procurarTudo(param);
		}

	
	
	
}
