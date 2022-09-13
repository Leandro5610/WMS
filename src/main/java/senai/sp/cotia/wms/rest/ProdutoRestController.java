package senai.sp.cotia.wms.rest;

import java.net.URI;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Pedido;
import senai.sp.cotia.wms.model.Produto;
import senai.sp.cotia.wms.repository.ProdutoRepository;

@RestController
@CrossOrigin
@RequestMapping("api/produto")
public class ProdutoRestController {
	
	@Autowired
	private ProdutoRepository prodRepo;
	
	// MÉTODO PARA SALVAR
			@RequestMapping(value = "save")
			public ResponseEntity<Object> saveProduto(@RequestBody Produto produto, HttpServletRequest request,
					HttpServletResponse response, MultipartFile imagem){
				
				try {
				produto.setImagem(imagem.getBytes());
				prodRepo.save(produto);
				return ResponseEntity.ok(HttpStatus.CREATED);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ResponseEntity.ok().build();
			}
			
			//MÉTODO PARA LISTAR OS PRODUTOS
			@RequestMapping(value = "", method = RequestMethod.GET)
			public Iterable<Produto> listarPedidos(){
				return prodRepo.findAll();
			}
			
			// MÉTODO PARA BUSCAR PRODUTO NO BANCO
			@RequestMapping(value = "/{id}", method = RequestMethod.GET)
			public ResponseEntity<Produto> findProduto(@PathVariable("id") Long codProduto){
				// buscar pedido
				Optional<Produto> produto = prodRepo.findById(codProduto);
				
				// verificação de pedido
				if (produto.isPresent()) {
					return ResponseEntity.ok(produto.get());
				} else {
					return ResponseEntity.notFound().build();
				}
			}
			
			// MÉTODO PARA SALVAR ATUALIZAÇÃO
			@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
			public ResponseEntity<Void> atualizarProduto(@RequestBody Produto produto, 
					@PathVariable("id") Long codProduto){
				// validação de id
				if (codProduto != produto.getCodProduto()) {
					throw new RuntimeException("ID inválido");
				}
				// salvar atualizado atualizado
				prodRepo.save(produto);
				// criar novo cabeçalho HTTP
				HttpHeaders header = new HttpHeaders();
				header.setLocation(URI.create("/api/produto"));
				return new ResponseEntity<Void>(header, HttpStatus.OK);	
			}
			
			// MÉTODO PARA DELETAR PEDIDO
			@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
			public ResponseEntity<Void> excluirProduto(@PathVariable("id") Long codProduto){
				prodRepo.deleteById(codProduto);
				return ResponseEntity.noContent().build();
			}
			// metodo para procurar uma reserva à partir de qualquer atributo
			@RequestMapping(value = "/findbyall/{p}")
			public Iterable<Produto> findByAll(@PathVariable("p") String param) {
				return prodRepo.procurarTudo(param);
			}
			
		
			
			
			

}
