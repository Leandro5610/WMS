package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import senai.sp.cotia.wms.model.Turma;
import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.UnidadeMedida;
import senai.sp.cotia.wms.repository.UnidadeMedidaRepository;

@RestController
@CrossOrigin
@RequestMapping("api/unidade")
public class UnidadeMedidaRestController {
	@Autowired
	private UnidadeMedidaRepository repo;
	
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object cadastrarMedida(@RequestBody UnidadeMedida unidade) {
		try {
			// salvar o usuário no banco de dados
			repo.save(unidade);
			return ResponseEntity.ok(HttpStatus.CREATED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UnidadeMedida> getUnidades(@PathVariable("id")Long idUni){
		Optional<UnidadeMedida> unidade = repo.findById(idUni);
		if(unidade.isPresent()) {
			 return ResponseEntity.ok(unidade.get());
		 }else {
			 return ResponseEntity.notFound().build();
		 }
	}
		
		
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deletaUnidades(@PathVariable("id") Long id) {
		repo.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizaUnidades(@PathVariable("id")Long id, UnidadeMedida unidade){
			if(id != unidade.getId() ) {
				throw new RuntimeException("id invalido");	
			}
				repo.save(unidade);
				HttpHeaders header = new HttpHeaders();
				header.setLocation(URI.create("/api/unidade"));
				return new ResponseEntity<Void>(header, HttpStatus.OK);
	
	}
	// metodo para procurar uma reserva à partir de qualquer atributo
		@RequestMapping(value = "/findbyall/{p}")
		public Iterable<UnidadeMedida> findByAll(@PathVariable("p") String param) {
			return repo.procurarTudo(param);
		}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<UnidadeMedida> listAluno(){
		return repo.findAll();
	}
	
	
}
