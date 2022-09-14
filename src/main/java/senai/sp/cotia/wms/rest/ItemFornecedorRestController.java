package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.annotation.Privado;
import senai.sp.cotia.wms.model.ItemFornecedor;
import senai.sp.cotia.wms.repository.ItemFornecedorRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/itemFornecedor")
public class ItemFornecedorRestController {

	@Autowired
	private ItemFornecedorRepository itensfornecedorRepository;

	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveItensFornecedor(@RequestBody ItemFornecedor itens) {
		try {
			itensfornecedorRepository.save(itens);
			return ResponseEntity.ok(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ItemFornecedor> findItensFornecedor(@PathVariable("id") Long idItens) {
		// busca os itens
		Optional<ItemFornecedor> itens = itensfornecedorRepository.findById(idItens);
		if (itens.isPresent()) {
			return ResponseEntity.ok(itens.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// atualiza os itens recebendo o id
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizarItens(@RequestBody ItemFornecedor itens, @PathVariable("id") Long id) {
		// valida o ID
		if (id != itens.getId()) {
			throw new RuntimeException("ID Inválido");
		}
		// salva o item no BD
		itensfornecedorRepository.save(itens);
		// criar um cabeçalho HTTP
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/itemFornecedor"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirItensFornecedor(@PathVariable("id") Long idItens) {
		itensfornecedorRepository.deleteById(idItens);
		return ResponseEntity.noContent().build();
	}

	
	// metodo para procurar um item à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}")
	public Iterable<ItemFornecedor> findByAll(@PathVariable("p") String param) {
		return itensfornecedorRepository.procurarTudo(param);
	}


}
