package senai.sp.cotia.wms.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.repository.AlunoRepository;

@RestController
@RequestMapping(value = "api/relatorio")
public class testeController {
	@Autowired
	private AlunoRepository repo;
	
	@RequestMapping(value = "pdf", method = RequestMethod.GET)
	public String generatedPdf() throws FileNotFoundException, JRException {
		
		List<Aluno> list = repo.findAll();
		

		JRBeanCollectionDataSource bean =new JRBeanCollectionDataSource(list);
		JasperReport report =JasperCompileManager.compileReport(new FileInputStream("src/main/resources/teste_A4.jrxml"));
		
		HashMap<String, Object> map = new HashMap<>();
		JasperPrint print = JasperFillManager.fillReport(report, map, bean);
		
		JasperExportManager.exportReportToPdfFile(print, "teste.pdf");
		
		return "gerado";
	}
}
