package senai.sp.cotia.wms.rest;


import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.model.Produto;
import senai.sp.cotia.wms.repository.MovimentacaoRepository;
import senai.sp.cotia.wms.repository.ProdutoRepository;
import senai.sp.cotia.wms.type.Tipo;

@RestController
@CrossOrigin
@RequestMapping("api/movimentacao")
public class MovimentacaoRestController {

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	
	@RequestMapping(value = "save/{id}", method = RequestMethod.POST)
	public Object saveMovimentacao(@PathVariable("id") Long idCode, Movimentacao movimentacao, Produto produto) {
		
		if (idCode != produto.getCodProduto()) {
			throw new RuntimeException("ID Inválido");
		}
			movimentacao.setProduto(produto);
			movimentacao.setTipo(Tipo.ENTRADA);
			LocalDateTime time = LocalDateTime.now();
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			movimentacao.setData(time.format(fmt));
			movimentacaoRepository.save(movimentacao);

		return "deu certo";
	}
	

	
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object cadastrarMovimentacao(@RequestBody Movimentacao mov) {
		try {
			// salvar o usuário no banco de dados
			movimentacaoRepository.save(mov);
			return ResponseEntity.ok(HttpStatus.CREATED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
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

	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Movimentacao> listarMovimentacao() {
		return movimentacaoRepository.findAll();

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
	
	@RequestMapping(value = "/findbyall/{p}", method = RequestMethod.GET)
	public List<Movimentacao> findByAll(@PathVariable("p") String param) {
		return movimentacaoRepository.procurarTudo(param);
	}
	
	@RequestMapping(value = "listar/{a}&{c}&{e}", method = RequestMethod.GET)
	public List<Movimentacao> listMov(@PathVariable("c")String dateStart, @PathVariable("e")String dateEnd, @PathVariable("a")String produto) {
		return movimentacaoRepository.dataProduto(produto, dateStart, dateEnd);
	}

	/*
	  @RequestMapping(value = "teste", method = RequestMethod.GET) public
	  ResponseEntity<Object> realizarEntrada(Movimentacao mov) {
	  
	  LocalDateTime time = LocalDateTime.now(); DateTimeFormatter fmt =
	  DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	  mov.setData(time.format(fmt)); mov.setTipo(Tipo.ENTRADA); return
	  ResponseEntity.ok().build(); }
	 */

	/*
	  public Object realizarSaida(Movimentacao mov) {
	 
	  LocalDateTime time = LocalDateTime.now(); DateTimeFormatter fmt =
	  DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	  mov.setData(time.format(fmt)); mov.setTipo(Tipo.SAIDA);
	  
	  return mov;
	  
	  }
	 */

	/*
	  public ResponseEntity<Object> adicionar(Movimentacao mov, Enderecamento
	  enderecamento, Estoque est, ItemPedido itens) { int saldo = est.getSaldo();
	 //est.setDiponivel(); int qtd = itens.getQuantidade(); est.setSaldo(qtd);
	  return ResponseEntity.ok().build();
	  
	  }
	 */
	
	/*@RequestMapping(value = "saldo/{p}", method = RequestMethod.GET)
	public int getSaldo(@PathVariable("p") Long produto) {
		int total = 0;
		for (int i = 0; i < movimentacaoRepository.findByTipoEntrada(produto).size(); i++) {
			System.out.println(i);
			
			for (int e = 0; e < movimentacaoRepository.findByTipoSaida(produto).size(); e++) {
				System.out.println(e);
				
				total = i - e;
				System.out.println(total);
				
			}
			
		}
		
		return total;
	}*/
	
	

	@GetMapping(value = "/mostraSaldo")
	public int  saldo() {
		Produto p = new Produto();
		return p.getSaldo();
	}
	
	
	

}
