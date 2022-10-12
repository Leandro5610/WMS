package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
public class EnderecamentoRestController {
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
		header.setLocation(URI.create("/api/aluno"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Enderecamento> findAluno(@PathVariable("id") Long idEnderecamento, HttpServletRequest request,
			HttpServletResponse response) {
		Optional<Enderecamento> enderecemento = repository.findById(idEnderecamento);
		if (enderecemento.isPresent()) {
			return ResponseEntity.ok(enderecemento.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "/findbyall/{p}")
	public Iterable<Enderecamento> findByAll(@PathVariable("p") String param) {
		return repository.procurarTudo(param);
	}
	
	@GetMapping(value = "relatorio")
	public ResponseEntity<Object> relatorioEstoque(HttpServletRequest request, HttpServletResponse reponse) {
		List<Enderecamento> list = (List<Enderecamento>) repository.findAll();

		JRBeanCollectionDataSource dados = new JRBeanCollectionDataSource(list);
		Calendar calendar = Calendar.getInstance();

		int yearInt = calendar.get(Calendar.YEAR);

		try {
			JasperReport report = JasperCompileManager.compileReport("src/main/resources/Invoice.jrxml");

			String year = yearInt + "";

			Map<String, Object> map = new HashMap<>();
			map.put("CollectionData", dados);

			map.put("year", year);
			
			String name = "C:\\Users\\Pichau\\Downloads\\relatorio.pdf";
			
			JasperPrint print = JasperFillManager.fillReport(report, map, new JREmptyDataSource());

			JasperExportManager.exportReportToPdfFile(print, name);

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok().build();

	}
}
