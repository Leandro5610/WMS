package senai.sp.cotia.wms.rest;

import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.ItemNota;
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.model.NotaFiscal;
import senai.sp.cotia.wms.model.Produto;
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
