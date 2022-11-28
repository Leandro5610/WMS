package senai.sp.cotia.wms.rest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
import org.springframework.web.context.ServletContextAware;

import com.google.common.io.Files;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import senai.sp.cotia.wms.annotation.Privado;
import senai.sp.cotia.wms.annotation.Publico;
import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Enderecamento;
import senai.sp.cotia.wms.model.Pedido;
import senai.sp.cotia.wms.repository.EnderecamentoRepository;
import senai.sp.cotia.wms.repository.PedidoRepository;

@CrossOrigin
@RestController
@RequestMapping("api/enderecamento")
public class EnderecamentoRestController implements ServletContextAware {
	ServletContext contexto;
	
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.contexto = servletContext;
		
	}
	
	
	@Autowired
	private EnderecamentoRepository repository;
	@Autowired
	private PedidoRepository pedidoRep;

	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveEnderecameto(@RequestBody Enderecamento enderecamento, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		try {
			repository.save(enderecamento);
			return ResponseEntity.ok(HttpStatus.CREATED);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Enderecamento> listEnderecamento() {
		return repository.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteEnderecamento(@PathVariable("id") Long idEnderecamento) {
		repository.deleteById(idEnderecamento);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateEnderecamento(@RequestBody Enderecamento enderecamento,
			@PathVariable("id") Long id) {
		if (id != enderecamento.getId()) {
			throw new RuntimeException("Id Inválido");
		}
		repository.save(enderecamento);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/enderecamento"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Enderecamento> findEnderecamento(@PathVariable("id") Long idEnderecamento,
			HttpServletRequest request, HttpServletResponse response) {
		
		Optional<Enderecamento> enderecemento = repository.findById(idEnderecamento);
		if (enderecemento.isPresent()) {
			return ResponseEntity.ok(enderecemento.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/findbyall/{p}", method = RequestMethod.GET)
	public Iterable<Enderecamento> findByAll(@PathVariable("p") String param) {
		return repository.procurarTudo(param);
	}

	// METODO PARA GERAR RELATÓRIO DO ESTOQUE GERAL
	@GetMapping(value = "relatorio")
	public ResponseEntity<Object> relatorioEstoque(HttpServletRequest request, HttpServletResponse response) {
		
		// lista de todos os endereçamentos
		List<Enderecamento> list = (List<Enderecamento>) repository.findAll();
		// passando a lista de endereçamentos para collection
		JRBeanCollectionDataSource dados = new JRBeanCollectionDataSource(list);
		Calendar calendar = Calendar.getInstance();
		// pegar o ano atual para mostrar no relatório
		int yearInt = calendar.get(Calendar.YEAR);

		try {
			// compilando o arquivo de layout do relatório
			JasperReport report = JasperCompileManager.compileReport("src/main/java/relatorios/Invoice.jrxml");
			// convertendo o ano para string
			String year = yearInt + "";

			Map<String, Object> map = new HashMap<>();
			// passando a collection de dados para o paremeter
			// criado no arquivo de layout do jasper reports
			map.put("CollectionData", dados);
			// passando o ano para o parameter
			// criado no arquivo de layout do jasper reports
			map.put("year", year);

			String name = "relatorio.pdf";

			// preenchendo o arquivo de layout com as informações
			// da lista de endereçamento
			JasperPrint print = JasperFillManager.fillReport(report, map, new JREmptyDataSource());
			// exportando o relatório como um arquivo PDF
			JasperExportManager.exportReportToPdfFile(print, name);
			// criando um arquvio como o nome do relatório
			File arquivo = new File(name);
			// pegando o caminho da requsição
			OutputStream output = response.getOutputStream();
			// mandando o relatório para o front-end
			// para ser feito o download
			Files.copy(arquivo, output);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}

	}

	/*
	  @GetMapping(value = "relatoriooo") public ResponseEntity<Object>
	  relatorioEstoqueAbc(HttpServletRequest request, HttpServletResponse reponse)
	  { List<Enderecamento> list = (List<Enderecamento>) repository.findAll();
	  
	  JRBeanCollectionDataSource dados = new JRBeanCollectionDataSource(list);
	  Calendar calendar = Calendar.getInstance();
	  
	  int yearInt = calendar.get(Calendar.YEAR);
	 
	  try { JasperReport report =
	  JasperCompileManager.compileReport("src/main/java/relatorios/ABC.jrxml");
	  
	  String year = yearInt + "";
	  
	  Map<String, Object> map = new HashMap<>(); map.put("CollectionData", dados);
	  //variavel para mandar o ano atual para o relatório map.put("year", year); //
	  String name = "relatorio.pdf";
	  
	  String nameXml = "relatorio.xml";
	  
	  JasperPrint print = JasperFillManager.fillReport(report, map, dados);
	  
	  JasperExportManager.exportReportToPdfFile(print, name);
	  
	  JasperExportManager.exportReportToXmlFile(print, name, true);
	  
	 } catch (JRException e) { // TODO Auto-generated catch block
	  e.printStackTrace(); }
	  
	  return ResponseEntity.ok().build();
	  
	  }
	 */

	@GetMapping(value = "relatorioABC")
	public ResponseEntity<Object> relatorioABC(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		List<Enderecamento> list = (List<Enderecamento>) repository.findAll();

		JRBeanCollectionDataSource dados = new JRBeanCollectionDataSource(list);
		Calendar calendar = Calendar.getInstance();

		int yearInt = calendar.get(Calendar.YEAR);

		try {
			JasperReport report = JasperCompileManager.compileReport("src/main/java/relatorios/TesteABC.jrxml");

			String year = yearInt + "";
			// response.setHeader("Content-Type","application/xml");
			Map<String, Object> map = new HashMap<>();
			map.put("CollectionData", dados);

			map.put("year", year);

			// String name = "relatorio.pdf";
			//response.setContentType("application/xls");
			
			
			Resource resource = new ClassPathResource("static");
			

			System.out.println(contexto.getRealPath("static"));
			
			String nomeAleatorio =  UUID.randomUUID().toString()+".xls";
			
			String nameXml = new File("src\\main\\resources\\static\\relatorios").getAbsolutePath() +"\\"+nomeAleatorio;
			
			ClassLoader loader = getClass().getClassLoader();

			JasperPrint print = JasperFillManager.fillReport(report, map, new JREmptyDataSource());

			//JasperExportManager.exportReportToPdfFile(print, nameXml);

			JRXlsxExporter exporter = new JRXlsxExporter();

			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, nameXml);
			exporter.exportReport();

			/* criando um arquvio como o nome do relatório
			File arquivo = new File(resource.getURI()+"/relatorio.xls");
			
			 pegando o caminho da requsição
			OutputStream output = response.getOutputStream();
			 mandando o relatório para o front-end
			 para ser feito o download
			
			Files.copy(arquivo, output);*/
			
			return ResponseEntity.ok(nomeAleatorio);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.badRequest().build();
		}

		
	}

	@RequestMapping(value = "armazenar/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> guardaItens(@RequestBody Enderecamento enderecamento,
			@PathVariable("id") Long idEnderecamento) {
		// verefica se existe o enderecamento
		repository.save(enderecamento);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/enderecamento"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}

}
