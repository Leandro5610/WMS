package senai.sp.cotia.wms.rest;


import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import senai.sp.cotia.wms.model.ItemFornecedor;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.model.Produto;
import senai.sp.cotia.wms.repository.FornecedorRepository;
import senai.sp.cotia.wms.repository.ItemFornecedorRepository;
import senai.sp.cotia.wms.repository.MovimentacaoRepository;
import senai.sp.cotia.wms.repository.ProdutoRepository;
import senai.sp.cotia.wms.util.FireBaseUtil;

@RestController
@CrossOrigin
@RequestMapping("api/produto")
public class ProdutoRestController {

	@Autowired
	private ProdutoRepository prodRepo;

	@Autowired
	private FornecedorRepository fornRepo;

	@Autowired
	private FireBaseUtil fire;

	@Autowired
	private MovimentacaoRepository movirepo;

	@Autowired
	private ItemFornecedorRepository itemFornece;

	// MÉTODO PARA SALVAR
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveProduto(@RequestBody Produto produto, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		try {
			// percorrer os itens do fornecerdor e inserir o produto
			for (ItemFornecedor itens : produto.getFornecedores()) {
				itens.setProduto(produto);
				itens.setFornecedor(itens.getFornecedor());

			}

			// verificar se o produto tem imagem
			if (produto.getImagem() != null) {

				prodRepo.save(produto);

			} else {
				prodRepo.save(produto);
				return ResponseEntity.ok(HttpStatus.CREATED);
			}

		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok().build();
	}

	// MÉTODO PARA LISTAR OS PRODUTOS
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Produto> listarProdutos() {
		return prodRepo.findAll();
	}

	// MÉTODO PARA BUSCAR PRODUTO NO BANCO
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> findProduto(@PathVariable("id") Long codProduto) {
		String codString = codProduto.toString();
		
		int i =0;
        while (i < codString.length() && codString.charAt(i) == '0')
            i++;
        
        StringBuffer sb = new StringBuffer(codString);
        sb.replace(0, i, "");
		
		
		
		Long id = Long.parseLong(codString);		
		// buscar pedido
		Optional<Produto> produto = prodRepo.findById(id);

		// verificação de pedido
		if (produto.isPresent()) {
			return ResponseEntity.ok(produto.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// MÉTODO PARA SALVAR ATUALIZAÇÃO
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizarProduto(@RequestBody Produto produto, @PathVariable("id") Long codProduto) {
		// validação de id
		if (codProduto != produto.getCodProduto()) {
			throw new RuntimeException("ID inválido");
		}
		try {
			if (produto.getImagem() != null) {
				
				prodRepo.save(produto);
				return ResponseEntity.ok().build();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		// salvar atualizado atualizado
		prodRepo.save(produto);
		// criar novo cabeçalho HTTP
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/produto"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}

	// MÉTODO PARA DELETAR PRODUTO
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirProduto(@PathVariable("id") Long codProduto) {
		prodRepo.deleteById(codProduto);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "relatorio")
	public ResponseEntity<Object> relatorioEstoque() {
		List<Movimentacao> list = (List<Movimentacao>) movirepo.findAll();
		JRBeanCollectionDataSource dados = new JRBeanCollectionDataSource(list);

		try {
			JasperReport report = JasperCompileManager.compileReport("src/main/java/relatorio/Blank_A4.jrxml");

			Map<String, Object> map = new HashMap<>();

			JasperPrint print = JasperFillManager.fillReport(report, map, dados);

			JasperExportManager.exportReportToPdfFile(print, "C:\\Users\\TecDevTarde\\Desktop\\relatorio.pdf");
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok().build();

	}

	// metodo para procurar uma reserva à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}", method = RequestMethod.GET)
	public Iterable<Produto> findByAll(@PathVariable("p") String param) {
		return prodRepo.procurarTudo(param);
	}

}
