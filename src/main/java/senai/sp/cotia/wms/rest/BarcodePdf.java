package senai.sp.cotia.wms.rest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.StyleConstants.ColorConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfFormXObject;
import com.itextpdf.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRException;
import senai.sp.cotia.wms.annotation.Privado;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.model.Produto;
import senai.sp.cotia.wms.repository.ProdutoRepository;

@CrossOrigin
@RestController
@RequestMapping("api/pdf")
public class BarcodePdf {

	@Autowired
	private ProdutoRepository productRepository;

	@Privado
	@RequestMapping(value = "barcode/{id}", method = RequestMethod.GET)
	public String generatedPdf(@PathVariable("id") Long idProduto, HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException, JRException, DocumentException {

		try {

			Optional<Produto> produto = productRepository.findById(idProduto);

			String nomeProduto = produto.get().getNome();

			Document document = new Document();

			response.setContentType("apllication/pdf");

			response.addHeader("Content-Disposition", "inline; filename=" + "codigo.pdf");

			PdfWriter arquivo = PdfWriter.getInstance(document, response.getOutputStream());
			document.open();

			Barcode128 barcode = new Barcode128();

			Random geradorNumero = new Random();
			// gera um numero aleatório até 10
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 87ceed7c06f111a30ac97a14f7277f0777f94ae8
			//double numeroAletorio = geradorNumero.nextDouble(10);
			// formata o numero com 9 casas decimais
			//String numeroFormatado = String.format("%.9f", numeroAletorio);
			// retira a vircula do número
			//String mascaraCodigo = numeroFormatado.replace(",", "");
<<<<<<< HEAD
=======
=======
			// double numeroAletorio arquivo= geradorNumero.nextDouble(10);
			// formata o numero com 9 casas decimais
			// String numeroFormatado = String.format("%.9f", numeroAletorio);
			// retira a vircula do número
			// String mascaraCodigo = numeroFormatado.replace(",", "");
>>>>>>> f0e73fc357a1aa99407349514ab3685552b297a1
>>>>>>> 87ceed7c06f111a30ac97a14f7277f0777f94ae8
			// três primeiros digitos do codigo de barras brasileiro GTIN-13
			String padraoBr = "789";

			Paragraph paragrafo = new Paragraph();

			paragrafo.add("Codigo de Barras do " + nomeProduto);
			// gerar o codigo de barras com a mascara e o identificador do produto
<<<<<<< HEAD
			//barcode.setCode(padraoBr + mascaraCodigo + idProduto.toString());
=======
<<<<<<< HEAD
		//	barcode.setCode(padraoBr + mascaraCodigo + idProduto.toString());
=======
			// barcode.setCode(padraoBr + mascaraCodigo + idProduto.toString());
>>>>>>> f0e73fc357a1aa99407349514ab3685552b297a1
>>>>>>> 87ceed7c06f111a30ac97a14f7277f0777f94ae8
			Image img = barcode.createImageWithBarcode(arquivo.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);

			// BarcodeQRCode qrCode = new BarcodeQRCode("Código de Barras do " +
			// nomeProduto, 300, 300, null);

			img.scalePercent(200);
			document.add(paragrafo);
			document.add(img);
			document.close();

			return "funcionou";
		} catch (Exception e) {
			return "deu errado";
		}

	}

	@Privado
	@RequestMapping(value = "qrcode/{id}", method = RequestMethod.GET)
	public String generatedQrCode(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, JRException, DocumentException {
		Optional<Produto> pro = productRepository.findById(id);
		Document document = new Document(new Rectangle(PageSize.A4));
		try {
			// coloca na reposta o formato do arquivo
			response.setContentType("apllication/pdf");
			// cria o arquivo com o nome qrCode
			response.addHeader("Content-Disposition", "inline; filename=" + "qrCode.pdf");

			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

			document.open();

			// QR Code Barcode
			document.add(new Paragraph("QR Code do Produto" + pro.get().getNome()));

			BarcodeQRCode qrcode = new BarcodeQRCode(id + "", 200, 200, null);

			Image image = qrcode.getImage();

			// Add Barcode to PDF document
			document.add(image);

			document.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "certo";

	}

	@RequestMapping(value = "/teste", method = RequestMethod.GET)
<<<<<<< HEAD
	public String generatedPdfA(String[]idProduto, HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException, JRException, DocumentException {
=======
	public String generatedPdfA(String[] idProduto, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, JRException, DocumentException {

>>>>>>> f0e73fc357a1aa99407349514ab3685552b297a1
		try {

			Document document = new Document();

			// response.setContentType("apllication/pdf");

			// response.addHeader("Content-Disposition", "inline; filename=" +
			// "codigo.pdf");

			PdfWriter arquivo = PdfWriter.getInstance(document,
					new FileOutputStream("C:\\Users\\Pichau\\Desktop\\teste.pdf"));
			document.open();

			Barcode128 barcode = new Barcode128();

<<<<<<< HEAD
			for (String id: idProduto) {
=======
			for (String id : idProduto) {
>>>>>>> f0e73fc357a1aa99407349514ab3685552b297a1
				Random geradorNumero = new Random();

				// gera um numero aleatório até 10
<<<<<<< HEAD
				//double numeroAletorio = geradorNumero.nextDouble(10);
				
				// formata o numero com 9 casas decimais
				//String numeroFormatado = String.format("%.9f", numeroAletorio);
=======
<<<<<<< HEAD
			//	double numeroAletorio = geradorNumero.nextDouble(10);
				
				// formata o numero com 9 casas decimais
			//	String numeroFormatado = String.format("%.9f", numeroAletorio);
>>>>>>> 87ceed7c06f111a30ac97a14f7277f0777f94ae8
				
				// retira a vircula do número
				//String mascaraCodigo = numeroFormatado.replace(",", "");
				
=======
				// double numeroAletorio = geradorNumero.nextDouble(10);

				// formata o numero com 9 casas decimais
				// String numeroFormatado = String.format("%.9f", numeroAletorio);

				// retira a vircula do número
				// String mascaraCodigo = numeroFormatado.replace(",", "");

>>>>>>> f0e73fc357a1aa99407349514ab3685552b297a1
				// três primeiros digitos do codigo de barras brasileiro GTIN-13
				String padraoBr = "789";

				Paragraph paragrafo = new Paragraph();

				paragrafo.add("Codigo de Barras do ");
				// gerar o codigo de barras com a mascara e o identificador do produto
<<<<<<< HEAD
				//barcode.setCode(padraoBr + mascaraCodigo+id);
=======
<<<<<<< HEAD
				//barcode.setCode(padraoBr + mascaraCodigo+id);
=======
				// barcode.setCode(padraoBr + mascaraCodigo+id);
>>>>>>> f0e73fc357a1aa99407349514ab3685552b297a1
>>>>>>> 87ceed7c06f111a30ac97a14f7277f0777f94ae8
				Image img = barcode.createImageWithBarcode(arquivo.getDirectContent(), BaseColor.BLACK,
						BaseColor.BLACK);
				img.scalePercent(200);
				document.add(paragrafo);
				document.add(img);
<<<<<<< HEAD
				
				 //BarcodeQRCode qrCode = new BarcodeQRCode("Código de Barras do " , 300, 300, null);
=======

				// BarcodeQRCode qrCode = new BarcodeQRCode("Código de Barras do " , 300, 300,
				// null);
>>>>>>> f0e73fc357a1aa99407349514ab3685552b297a1

			}
			document.close();

			return "funcionou";
		} catch (Exception e) {
			return e.toString();
		}

	}

	@RequestMapping(value = "qrCodes", method = RequestMethod.GET)
	public String generatedQrCodes(String[] ids, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, JRException, DocumentException {

		Document document = new Document(new Rectangle(PageSize.A4));
		try {
			// coloca na reposta o formato do arquivo
			response.setContentType("apllication/pdf");
			// cria o arquivo com o nome qrCode
			response.addHeader("Content-Disposition", "inline; filename=" + "qrCode.pdf");

			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

			document.open();

			// QR Code Barcode
			for (String string : ids) {
				BarcodeQRCode qrcode = new BarcodeQRCode(ids + "", 200, 200, null);

				Image image = qrcode.getImage();

				// Add Barcode to PDF document
				document.add(image);

			}

			document.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "certo";

	}

}
