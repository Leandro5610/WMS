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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Enderecamento;
import senai.sp.cotia.wms.model.Estoque;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.repository.EstoqueRepository;

@RestController
@CrossOrigin
@RequestMapping("api/estoque")
public class EstoqueRestController {
	
	@Autowired
	private EstoqueRepository repository;
	
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveEstoque(@RequestBody Estoque estoque, HttpServletRequest request,
			HttpServletResponse response, HttpSession session){
			try {
				repository.save(estoque);
				return ResponseEntity.ok(HttpStatus.CREATED);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Estoque> listEstoque(){
		return repository.findAll();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteEstoque(@PathVariable("id") Long idEstoque){
		repository.deleteById(idEstoque);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateEstoque(@RequestBody Estoque estoque, @PathVariable("id" )Long id ){
		if(id != estoque.getId()) {
			throw new RuntimeException("Id Inválido");
		}
		repository.save(estoque);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/estoque"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Estoque> findEsotque(@PathVariable("id") Long idEstoque, HttpServletRequest request, HttpServletResponse response){
		Optional<Estoque> estoque = repository.findById(idEstoque);
		if(estoque.isPresent()) {
			return ResponseEntity.ok(estoque.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	
	
}
