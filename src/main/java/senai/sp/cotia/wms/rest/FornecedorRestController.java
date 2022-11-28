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
import senai.sp.cotia.wms.model.Fornecedor;
import senai.sp.cotia.wms.repository.FornecedorRepository;

@RestController
@CrossOrigin
@RequestMapping("api/fornecedor")
public class FornecedorRestController {
	@Autowired
	private FornecedorRepository repository;

	
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveFornecedor(@RequestBody Fornecedor fornecedor, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		
		try {
			repository.save(fornecedor);
			return ResponseEntity.ok(HttpStatus.CREATED);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Fornecedor> listFornecedor() {
		return repository.findAll();
	}

	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteFornecedor(@PathVariable("id") Long idFornecedor) {
		repository.deleteById(idFornecedor);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateFornecedor(@RequestBody Fornecedor fornecedor, @PathVariable("id") Long id) {
		if (id != fornecedor.getId()) {
			throw new RuntimeException("Id Inválido");
		}
		repository.save(fornecedor);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/fornecedor"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);

	}

	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Fornecedor> findFornecedor(@PathVariable("id") Long idFornecedor, HttpServletRequest request,
			HttpServletResponse response) {
		Optional<Fornecedor> fornecedor = repository.findById(idFornecedor);
		if (fornecedor.isPresent()) {
			return ResponseEntity.ok(fornecedor.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	
	@RequestMapping(value = "/findbyall/{p}", method = RequestMethod.GET)
	public Iterable<Fornecedor> findByAll(@PathVariable("p") String param) {
		return repository.procurarTudo(param);
	}
}
