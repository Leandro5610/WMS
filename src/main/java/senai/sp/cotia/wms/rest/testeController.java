package senai.sp.cotia.wms.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
import senai.sp.cotia.wms.model.ItemNota;
import senai.sp.cotia.wms.model.NotaFiscal;
import senai.sp.cotia.wms.repository.AlunoRepository;
import senai.sp.cotia.wms.repository.ItemNotaRepository;
import senai.sp.cotia.wms.repository.NotaFiscalRepository;
import senai.sp.cotia.wms.repository.ProdutoRepository;

@RestController
@RequestMapping(value = "api/relatorio")
public class testeController {
	@Autowired
	private AlunoRepository repo;
	@Autowired
	private NotaFiscalRepository repo1;
	@Autowired
	private ItemNotaRepository itemRep;
	@Autowired
	private ProdutoRepository produtoRep;

	@RequestMapping(value = "pdf/{id}", method = RequestMethod.GET)
	public String generatedPdf(@PathVariable("id") Long id) throws FileNotFoundException, JRException {
		
		
		List<ItemNota> list = itemRep.pegarNota(id);
		Optional<NotaFiscal> nota = repo1.findById(id);
	
		JRBeanCollectionDataSource bean = (JRBeanCollectionDataSource) list;
		
		JasperReport report =JasperCompileManager.compileReport(new FileInputStream("src/main/resources/Nota_Fiscal.jrxml"));
		HashMap<String, Object> map = new HashMap<>();
		
		
		
	    JasperPrint jasperPrint = JasperFillManager.fillReport(report, map,bean);
		
		JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\TecDevTarde\\Downloads\\teste.pdf");
		return "Deu certo";
		
		
	}
}
