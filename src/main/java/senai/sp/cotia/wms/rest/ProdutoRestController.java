package senai.sp.cotia.wms.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Fornecedor;
import senai.sp.cotia.wms.model.ItemFornecedor;
import senai.sp.cotia.wms.model.ItemNota;
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.model.Pedido;
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
				// variavel para guardar a imagem codificada Base64 que está vindo do front
				String stringImagem = produto.getImagem();

				// variaveis para extrair o que está entre a / e o ;
				int posicaoBarra = stringImagem.indexOf('/');
				int posicaoPontoVirgula = stringImagem.indexOf(';');

				// variavel para retirar a / e o ; para pegar a extensão da imagem
				String extensao = stringImagem.substring(posicaoBarra, posicaoPontoVirgula);

				// variavel para retirar a / da extensão
				String extensaoOriginal = extensao.replace("/", "");

				// variavel para retirar o texto data:imagem/enxtensão;base64, que está vindo do
				// base64 codificado do front-end
				String base64ImagemString = stringImagem.replace("data:image/" + extensaoOriginal + ";base64,", "");

				// variavel para para decodificar o codigo base64 e converter em um vetor de
				// bytes
				byte[] decodificada = Base64.getDecoder().decode(base64ImagemString);

				// variavel para converter o vetor de bytes em um texto
				String arquivoString = decodificada.toString();

				// variavel para retirar o texto "[B@" da variavel arquivoString
				String arquivo = arquivoString.replace("[B@", "");

				// variavel para gerar um nome aleatório para o arquivo e juntar com a extensão
				String nomeArquivo = UUID.randomUUID().toString() + arquivo + "." + extensaoOriginal;

				// variavel para guardar o nome do arquivo em um File
				File file = new File(nomeArquivo);

				// variavel para converter em arquivo e armazenar no sistema do pc
				FileOutputStream fileInput = new FileOutputStream("temporaria/" + file);

				// variavel para escrever os bytes no arquivo
				fileInput.write(decodificada);

				// variavel para pegar o caminho da pasta com o arquivo da imagem
				Path pathFile = Paths.get("temporaria/" + nomeArquivo);

				// fazer o upload da imagem para fire base
				fire.uploadFile(file, decodificada);
				fileInput.close();
				// inserir imagem nome da imagem no produto
				produto.setImagem(file.toString());
				prodRepo.save(produto);

				// excluir imagem da pasta temporaria
				Files.delete(pathFile);
			} else {
				prodRepo.save(produto);
				return ResponseEntity.ok(HttpStatus.CREATED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().build();
	}

	// MÉTODO PARA LISTAR OS PRODUTOS
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Produto> listarPedidos() {
		return prodRepo.findAll();
	}

	// MÉTODO PARA BUSCAR PRODUTO NO BANCO
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> findProduto(@PathVariable("id") Long codProduto) {
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
	public ResponseEntity<Void> atualizarProduto(@RequestBody Produto produto, @PathVariable("id") Long codProduto) {
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
	@RequestMapping(value = "/findbyall/{p}")
	public Iterable<Produto> findByAll(@PathVariable("p") String param) {
		return prodRepo.procurarTudo(param);
	}

}
