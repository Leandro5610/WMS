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
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.repository.MovimentacaoRepository;

@CrossOrigin
@Controller
@RequestMapping("/api/movimentacao")
public class MovimentacaoRestController {

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;
	
	
	@Privado
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveMovimentacao(@RequestBody Movimentacao itens) {
		try {
			movimentacaoRepository.save(itens);
			return ResponseEntity.ok(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@Privado
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Movimentacao> findMovimentaco(@PathVariable("id") Long idMovimentacao) {
		// busca os itens
		Optional<Movimentacao> movimentacao = movimentacaoRepository.findById(idMovimentacao);
		if (movimentacao.isPresent()) {
			return ResponseEntity.ok(movimentacao.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// atualiza os itens recebendo o id
			@Privado
			@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
			public ResponseEntity<Void> atualizarMovimentacao(@RequestBody Movimentacao movimentacao, @PathVariable("id") Long id) {
				// valida o ID
				if (id != movimentacao.getId()) {
					throw new RuntimeException("ID Inválido");
				}
				// salva o item no BD
				movimentacaoRepository.save(movimentacao);
				// criar um cabeçalho HTTP
				HttpHeaders header = new HttpHeaders();
				header.setLocation(URI.create("/api/movimentacao"));
				return new ResponseEntity<Void>(header, HttpStatus.OK);

			}
			
			@Privado
			@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
			public ResponseEntity<Void> excluirMovimentacao(@PathVariable("id") Long idMovimentacao) {
				movimentacaoRepository.deleteById(idMovimentacao);
				return ResponseEntity.noContent().build();
			}

			/*
			// metodo para procurar um item à partir de qualquer atributo
			@RequestMapping(value = "/findbyall/{p}")
			public List<Movimentacao> findByAll(@PathVariable("p") String param) {
				return movimentacaoRepository.procurarTudo(param);
			}
			*/
	
}
