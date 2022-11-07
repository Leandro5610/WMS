package senai.sp.cotia.wms.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.Files;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.repository.MovimentacaoRepository;
@CrossOrigin
@RestController
@RequestMapping("api/movimentacao/")
public class MovimentacaoPdf {

	@Autowired
	private MovimentacaoRepository movimentacaoRepository;

	// METODO PARA GERAR RELATORIO DE TODAS AS MOVIMENTAÇÕES
	@RequestMapping(value = "relatorioMovimentacoes", method = RequestMethod.GET)
	public String generatedPdf(HttpServletRequest request, HttpServletResponse response)
			throws JRException, IOException {
		// lista de todas as movimentações
		List<Movimentacao> list = movimentacaoRepository.findAll();

		// Instanciando uma coleção de dados a partir do lista de movimentações
		JRBeanCollectionDataSource bean = new JRBeanCollectionDataSource(list);

		// compilando o arquivo de layout do relatório
		JasperReport report = JasperCompileManager
				.compileReport(new FileInputStream("src/main/java/relatorios/movimentacao.jrxml"));

		HashMap<String, Object> map = new HashMap<>();

		// passando a coleção de dados para o parameter CollectionBeanParm do jasper
		// report
		map.put("CollectionBeanParam", bean);
		// nome do arquivo
		String name = "relatorio.pdf";

		// preenchendo o relatório com as informações das movimentações
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, new JREmptyDataSource());

		JasperExportManager.exportReportToPdfFile(jasperPrint, name);

		File arquivo = new File(name);

		OutputStream output = response.getOutputStream();
		Files.copy(arquivo, output);
		return "eee";

	}

	// METODO PARA GERAR RELATÓRIO DAS MOVIMENTAÇÕES
	// A PATIR DE UMA DATA E UM PRODUTO ESPECIFICO
	@RequestMapping(value = "pdf/{a}&{c}&{e}", method = RequestMethod.GET)
	public String generatedPdfDatasProdutos(@PathVariable("c") String dateStart, @PathVariable("e") String dateEnd,
			@PathVariable("a") String produto, 
			HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, JRException {
		// lista de movimentações de acordo com a data e o produto especificado
		List<Movimentacao> list = movimentacaoRepository.dataProduto(produto, dateStart, dateEnd);
		try {
			// Instanciando uma coleção de dados a partir do lista de movimentações
			JRBeanCollectionDataSource bean = new JRBeanCollectionDataSource(list);

			// compilando o arquivo de layout do relatório
			JasperReport report = JasperCompileManager
					.compileReport(new FileInputStream("src/main/java/relatorios/Invoice.jrxml"));
			HashMap<String, Object> map = new HashMap<>();
			// passando a coleção de dados para o parameter CollectionBeanParm do jasper
			// report
			map.put("CollectionBeanParam", bean);

			// nome do arquivo
			String name = "relatorio.pdf";

			// preenchendo o relatório com as informações das movimentações
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, new JREmptyDataSource());

			JasperExportManager.exportReportToPdfFile(jasperPrint, name);

			// NOTA SE ESSE METODO FOR APROVADO PELO CHILE IMPLEMENTAR
			
			  File arquivo = new File(name); 
			  OutputStream output =response.getOutputStream();
			  Files.copy(arquivo, output);
			  return "uauauau2";
		} catch (Exception e) {
			e.printStackTrace();
			return "erro";
		}

	}

	// METODO PARA GERAR RELATÓRIO DE MOVIMENTAÇÕES
	// A PATIR DE UMA DATA ESPECIFICA
	@RequestMapping(value = "pdf/data/{s}&{e}", method = RequestMethod.GET)
	public ResponseEntity<Movimentacao> generatedPdfDatas(@PathVariable("s") String dateStart, @PathVariable("e") String dateEnd,
			HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, JRException {

		List<Movimentacao> list = movimentacaoRepository.buscarMovimentacoesPorData(dateStart, dateEnd);

		JRBeanCollectionDataSource bean = new JRBeanCollectionDataSource(list);
		try {
			JasperReport report = JasperCompileManager
					.compileReport(new FileInputStream("src/main/java/relatorios/moviDatas.jrxml"));
			HashMap<String, Object> map = new HashMap<>();
			map.put("CollectionBeanParam", bean);

			String name = "relatorio.pdf";

			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, new JREmptyDataSource());

			JasperExportManager.exportReportToPdfFile(jasperPrint, name);

			 File arquivo = new File(name);
			 OutputStream output = response.getOutputStream();
			 Files.copy(arquivo, output);
			 return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		
		

	}

}
