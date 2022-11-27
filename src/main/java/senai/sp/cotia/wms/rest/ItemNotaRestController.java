package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.annotation.Privado;
import senai.sp.cotia.wms.model.ItemFornecedor;
import senai.sp.cotia.wms.model.ItemNota;
import senai.sp.cotia.wms.model.Pedido;
import senai.sp.cotia.wms.repository.ItemNotaRepository;

@CrossOrigin
@RestController
@RequestMapping("api/itemNota")
public class ItemNotaRestController {

	@Autowired
	private ItemNotaRepository itemNotaRepository;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ItemNota> findItensNota(@PathVariable("id") Long idItens) {
		// busca os itens
		Optional<ItemNota> itens = itemNotaRepository.findById(idItens);
		if (itens.isPresent()) {
			return ResponseEntity.ok(itens.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// atualiza os itens recebendo o id
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizarItensNota(@RequestBody ItemNota itens, @PathVariable("id") Long id) {
		// valida o ID
		if (id != itens.getId()) {
			throw new RuntimeException("ID Inválido");
		}
		// salva o item no BD
		itemNotaRepository.save(itens);
		// criar um cabeçalho HTTP
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/itemNota"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirItemNota(@PathVariable("id") Long idItens) {
		itemNotaRepository.deleteById(idItens);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<ItemNota> listarPedidos() {
		return itemNotaRepository.findAll();
	}

	// metodo para procurar um item à partir de qualquer atributo
	@GetMapping(value = "/findbyall/{p}")
	public List<ItemNota> findByAll(@PathVariable("p") String param) {
		return itemNotaRepository.procurarTudo(param);
	}

	//metodo para procurar um item à partir de qualquer atributo
	@RequestMapping(value = "teste/{codigo}", method = RequestMethod.GET)
	public List<ItemNota> teste(@PathVariable("codigo") Long param) {
		return itemNotaRepository.pegarNota(param);
	}
	
	@RequestMapping(value = "pega/{cod}", method = RequestMethod.GET)
	public List<ItemNota> vamo(@PathVariable("cod") String param) {
		Long id = Long.parseLong(param);
		return itemNotaRepository.pegarCod(id);
	}
}
