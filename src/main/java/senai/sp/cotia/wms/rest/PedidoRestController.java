package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.model.NotaFiscal;
import senai.sp.cotia.wms.model.Pedido;
import senai.sp.cotia.wms.model.Produto;
import senai.sp.cotia.wms.repository.NotaFiscalRepository;
import senai.sp.cotia.wms.repository.PedidoRepository;

@RestController
@CrossOrigin
@RequestMapping("api/pedido")
public class PedidoRestController {

		@Autowired
		private PedidoRepository pedidoRepo;
		
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
		
	}
