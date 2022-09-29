package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Estoque;
import senai.sp.cotia.wms.model.ItemNota;
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.model.NotaFiscal;
import senai.sp.cotia.wms.model.Pedido;
import senai.sp.cotia.wms.model.Produto;
import senai.sp.cotia.wms.repository.EstoqueRepository;
import senai.sp.cotia.wms.repository.ItemNotaRepository;
import senai.sp.cotia.wms.repository.MovimentacaoRepository;
import senai.sp.cotia.wms.repository.NotaFiscalRepository;
import senai.sp.cotia.wms.repository.PedidoRepository;
import senai.sp.cotia.wms.type.Tipo;

@RestController
@CrossOrigin
@RequestMapping("api/pedido")
public class PedidoRestController {

		@Autowired
		private PedidoRepository pedidoRepo;
		
		@Autowired
		private ItemNotaRepository itemNotaRepository;
		
		@Autowired
		private NotaFiscalRepository nfRepo;

		
		@Autowired
		private MovimentacaoRepository movRepo;
		@Autowired
		private EstoqueRepository estoque;
		// MÉTODO PARA SALVAR
		@RequestMapping(value = "save")
		public ResponseEntity<Object> savePedido(@RequestBody Pedido pedido, HttpServletRequest request,
				HttpServletResponse response){
			//double total = pedido.totalPedido(pedido);
			
			try {
				for (ItemPedido itens : pedido.getItens()) {
					itens.setPedido(pedido);
					
				}
			
				//pedido.setValor(total);
				
			LocalDateTime time = LocalDateTime.now();
			
			
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			
			pedido.setDataPedido(time.format(fmt));
		
			pedidoRepo.save(pedido);
			saveMovimentacao(pedido);
		
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok().build();
		}
		
		@RequestMapping(value = "list", method = RequestMethod.GET)
		public Iterable<Pedido> listarPedidos(){	
			return pedidoRepo.findAll();
		}
		
		
		// MÉTODO PARA BUSCAR PEDIDO NO BANCO
		@RequestMapping(value = "/{id}", method = RequestMethod.GET)
		public ResponseEntity<Pedido> findPedido(@PathVariable("id") Long numPedido){
			// buscar pedido
			Optional<Pedido> pedido = pedidoRepo.findById(numPedido);
			
			// verificação de pedido
			if (pedido.isPresent()) {
				return ResponseEntity.ok(pedido.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		}
		
		// MÉTODO PARA SALVAR ATUALIZAÇÃO
		@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
		public ResponseEntity<Void> atualizarPedido(@RequestBody Pedido pedido, @PathVariable("id") Long idPedido){
			// validação de id
			if (idPedido != pedido.getNumPedido()) {
				throw new RuntimeException("ID inválido");
			}
			// salvar pedido atualizado
			pedidoRepo.save(pedido);
			// criar novo cabeçalho HTTP
			HttpHeaders header = new HttpHeaders();
			header.setLocation(URI.create("/api/pedido"));
			return new ResponseEntity<Void>(header, HttpStatus.OK);	
		}
		
		
		// MÉTODO PARA DELETAR PEDIDO
		@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
		public ResponseEntity<Void> excluirPedido(@PathVariable("id") Long numPedido){
			pedidoRepo.deleteById(numPedido);
			return ResponseEntity.noContent().build();
		}
		// metodo para procurar uma reserva à partir de qualquer atributo
		@RequestMapping(value = "/findbyall/{p}")
		public Iterable<Pedido> findByAll(@PathVariable("p") String param) {
			return pedidoRepo.procurarTudo(param);
		}
		
		public Object saveMovimentacao(Pedido pedido) {

			for(ItemPedido itens : pedido.getItens()) {
				Movimentacao movi  = new Movimentacao();
				movi.setPedido(pedido);
				movi.setProduto(itens.getProduto());
				movi.setTipo(Tipo.ENTRADA);
				LocalDateTime time = LocalDateTime.now();
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
				movi.setData(time.format(fmt));
				movRepo.save(movi);
				
				
			}
			
			return "deu certo";
		

		}
		
		public ResponseEntity<Object> saveItensNota(NotaFiscal nota) {
			try {
				for (ItemPedido itens : nota.getPedido().getItens()) {
					ItemNota itemNota = new ItemNota();
					itemNota.setNotaFiscal(nota);
					itemNota.setQuantidade(nota.getQuantidade());
					//itemNota.setProduto(nota.getPedido());
					itemNota.setItem(nota.getPedido().getItens());
					itemNotaRepository.save(itemNota);
				}
				
				return ResponseEntity.ok(HttpStatus.CREATED);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
		
		public ResponseEntity<Object> saveNotaFiscal(Pedido pedido){
			
			try {
				LocalDateTime time = LocalDateTime.now();
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
					NotaFiscal nota = new NotaFiscal();
					nota.setDataEmissao(time.format(fmt));
					nota.setPedido(pedido);
					nota.setValorTotal(pedido.getValor());
			//nfRepo.save();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok().build();
		}
		

		
	}
