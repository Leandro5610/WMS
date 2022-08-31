package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.util.List;
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

import senai.sp.cotia.wms.annotation.Privado;
import senai.sp.cotia.wms.model.ItemFornecedor;
import senai.sp.cotia.wms.model.ItemNota;
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.repository.ItensPedidoRepository;
import senai.sp.cotia.wms.repository.ItensFornecedorRepository;

@CrossOrigin
@Controller
@RequestMapping("/api/itemPedido")
public class ItensPedidoRestController {

	@Autowired
	private ItensPedidoRepository itemPedidoRepository;
	
	@Privado
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveItensPedido(@RequestBody ItemPedido itens) {
		try {
			itemPedidoRepository.save(itens);
			return ResponseEntity.ok(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@Privado
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
	
		// atualiza os itens recebendo o id
		@Privado
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
		
		@Privado
		@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
		public ResponseEntity<Void> excluirItemPedido(@PathVariable("id") Long idItens) {
			itemPedidoRepository.deleteById(idItens);
			return ResponseEntity.noContent().build();
		}

		// metodo para procurar um item à partir de qualquer atributo
		@RequestMapping(value = "/findbyall/{p}")
		public List<ItemPedido> findByAll(@PathVariable("p") String param) {
			return itemPedidoRepository.procurarTudo(param);
		}
	
	
}
