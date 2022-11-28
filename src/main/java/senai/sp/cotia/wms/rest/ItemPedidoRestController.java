package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.repository.ItemPedidoRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/itemPedido")
public class ItemPedidoRestController {

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ItemPedido> findItemPedido(@PathVariable("id") Long idItens) {
		// busca os itens
		Optional<ItemPedido> itens = itemPedidoRepository.findById(idItens);
		if (itens.isPresent()) {
			return ResponseEntity.ok(itens.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<ItemPedido> listarPedidos() {
		return itemPedidoRepository.findAll();
	}

	// atualiza os itens recebendo o id
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizarItemPedido(@RequestBody ItemPedido itens, @PathVariable("id") Long id) {
		// valida o ID
		if (id != itens.getId()) {
			throw new RuntimeException("ID Inválido");
		}
		// salva o item no BD
		itemPedidoRepository.save(itens);
		// criar um cabeçalho HTTP
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/itemPedido"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirItemPedido(@PathVariable("id") Long idItens) {
		itemPedidoRepository.deleteById(idItens);
		return ResponseEntity.noContent().build();
	}

	// metodo para procurar um item à partir de qualquer atributo
	@GetMapping(value = "/findbyall/{p}")
	public List<ItemPedido> findByAll(@PathVariable("p") String param) {
		return itemPedidoRepository.procurarTudo(param);
	}

	@RequestMapping(value = "/findbypedido/{codigo}")
	public List<ItemPedido> findAllByPedido(@PathVariable("codigo") Long param) {
		return itemPedidoRepository.pegarItens(param);
	}

}
