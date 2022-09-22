package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.hibernate.boot.model.naming.ImplicitTenantIdColumnNameSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import senai.sp.cotia.wms.model.Enderecamento;
import senai.sp.cotia.wms.model.Estoque;
import senai.sp.cotia.wms.model.ItemFornecedor;
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.repository.MovimentacaoRepository;
import senai.sp.cotia.wms.type.Tipo;

@CrossOrigin
@Controller
@RequestMapping("/api/movimentacao")
public class MovimentacaoRestController {

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;

	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveMovimentacao(@RequestBody Movimentacao itens, Estoque est, Enderecamento end) {
		if (itens.getTipo() == Tipo.ENTRADA) {
			realizarEntrada(itens);
		} else {
			realizarSaida(itens);
			debitar(itens, est, end);
		}

		try {
			movimentacaoRepository.save(itens);
			return ResponseEntity.ok(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

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
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizarMovimentacao(@RequestBody Movimentacao movimentacao,
			@PathVariable("id") Long id) {
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

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirMovimentacao(@PathVariable("id") Long idMovimentacao) {
		movimentacaoRepository.deleteById(idMovimentacao);
		return ResponseEntity.noContent().build();
	}

	// metodo para procurar um item à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}")
	public List<Movimentacao> findByAll(@PathVariable("p") String param) {
		return movimentacaoRepository.procurarTudo(param);
	}

	@RequestMapping(value = "teste", method = RequestMethod.GET)
	public ResponseEntity<Object> realizarEntrada(Movimentacao mov) {
		LocalDateTime time = LocalDateTime.now();

		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

		mov.setData(time.format(fmt));

		mov.setTipo(Tipo.ENTRADA);

		return ResponseEntity.ok().build();
	}

	public Object realizarSaida(Movimentacao mov) {
		LocalDateTime time = LocalDateTime.now();

		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

		mov.setData(time.format(fmt));

		mov.setTipo(Tipo.SAIDA);

		return mov;

	}

	
	public ResponseEntity<Object> debitar(Movimentacao mov, Estoque estoq, Enderecamento endereco) {
		mov.setTipo(Tipo.SAIDA);
		int cap = estoq.getCapacidade();
		int disp = estoq.getDisponivel();
		int saldo = estoq.getSaldo();
		estoq.setSaldo(cap, disp, saldo);
		endereco.setAndar(null);
		endereco.setCorredor(null);
		endereco.setEdificio(null);
		endereco.setModulo(null);
		endereco.setId(null);
		endereco.setDemanda(null);

		return ResponseEntity.ok().build();
	}
	
	

}
