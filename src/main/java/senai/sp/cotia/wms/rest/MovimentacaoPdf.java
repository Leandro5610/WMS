package senai.sp.cotia.wms.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.repository.MovimentacaoRepository;

@RestController
@RequestMapping("api/movimentacao/")
public class MovimentacaoPdf {
	
	@Autowired
	private MovimentacaoRepository movimentacaoRepository;

	
	@RequestMapping(value = "pdf", method = RequestMethod.GET)
	public String generatedPdf() throws FileNotFoundException, JRException {

		List<Movimentacao> list = movimentacaoRepository.findAll();
		JRBeanCollectionDataSource bean = new JRBeanCollectionDataSource(list);
		
		JasperReport report =JasperCompileManager.compileReport(new FileInputStream("src/main/resources/teste_A4.jrxml"));
		HashMap<String, Object> map = new HashMap<>();
		
	    JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, bean);
		
		JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\bruno\\Downloads\\teste.pdf");
		return "uauauau";
		
	}
	
}
