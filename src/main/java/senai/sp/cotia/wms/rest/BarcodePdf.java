package senai.sp.cotia.wms.rest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.itextpdf.text.pdf.PdfWriter;
import net.sf.jasperreports.engine.JRException;
import senai.sp.cotia.wms.model.Produto;
import senai.sp.cotia.wms.repository.ProdutoRepository;

@CrossOrigin
@RestController
@RequestMapping("api/pdf")
public class BarcodePdf {

	@Autowired
	private ProdutoRepository productRepository;

	// METODO PARA GERAR UM CÓDIGO DE BARRA PARA UM PRODUTO
	@RequestMapping(value = "barcode/{id}", method = RequestMethod.GET)
	public String generateBarCode(@PathVariable("id") Long idProduto, HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException, JRException, DocumentException {

		try {

			// procura o produto no banco
			Optional<Produto> produto = productRepository.findById(idProduto);

			String nomeProduto = produto.get().getNome();

			// gera um novo doucmento
			Document document = new Document();

			// cria um arquivo pdf passando o documento e o lugar que vai ser salvo
			PdfWriter arquivo = PdfWriter.getInstance(document, response.getOutputStream());
			document.open();

			// biblioteca para gerar codigo de barras
			Barcode128 barcode = new Barcode128();

			Random geradorNumero = new Random();

			// gera um numero aleatório de 0 até 10
			double number = geradorNumero.nextDouble();

			// formata o numero com 9 casas decimais
			String numeroFormatado = "";
			/*
			 * if (idProduto < 10) { numeroFormatado= String.format("%.6f", number); }else
			 * if(idProduto >=1000 && idProduto <=999) { numeroFormatado =
			 * String.format("%.5f", number); }else if(idProduto >=1000 && idProduto ==
			 * 9999) { numeroFormatado = String.format("%.3f", number); }else {
			 * numeroFormatado = String.format("%.4f", number); }
			 */

			numeroFormatado = String.format("%.5f", number);

			// retira a vircula do número
			String mascaraCodigo = numeroFormatado.replace(",", "");

			// três primeiros digitos do codigo de barras brasileiro GTIN-13
			String padraoBr = "789";

			String mascaraId = "";
			if (idProduto >= 10) {
				mascaraId = "00";
			} else if (idProduto >= 100) {
				mascaraId = "0";
			} else if (idProduto < 10) {
				mascaraId = "000";
			}

			Paragraph paragrafo = new Paragraph();

			paragrafo.add("Codigo de Barras: " + nomeProduto);

			// inseri as inforções do codigo de barras com a mascara e o identificador do
			// produto
			barcode.setCode(padraoBr + mascaraCodigo + mascaraId + idProduto.toString());

			// montando o a imagem do codigo de barras
			Image img = barcode.createImageWithBarcode(arquivo.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);

			// BarcodeQRCode qrCode = new BarcodeQRCode("Código de Barras do " +
			// nomeProduto, 300, 300, null);

			// tamanho da imagem do codigo de barras
			img.scalePercent(200);
			document.add(paragrafo);
			// adiciona no arquivo o codigo de barra e gera o arquivo
			document.add(img);
			document.close();

			return "funcionou";

		} catch (Exception e) {
			return "deu errado";
		}

	}

	// METODO PARA GERAR O QR CODE DO PRODUTO
	@RequestMapping(value = "qrCode/{id}", method = RequestMethod.GET)
	public String generatedQrCode(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, JRException, DocumentException {
		// procura o produto no banco de dados
		Optional<Produto> pro = productRepository.findById(id);

		// cria um documento no tamanho A4
		Document document = new Document(new Rectangle(PageSize.A4));
		try {
			/*
			 * CASO PRECISE // coloca na reposta o formato do arquivo
			 * response.setContentType("apllication/pdf"); // cria o arquivo com o nome
			 * qrCode response.addHeader("Content-Disposition", "inline; filename=" +
			 * "qrCode.pdf");
			 */

			// cria um arquivo pdf passando o documento e o lugar que vai ser salvo
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

			document.open();

			// QR Code Barcode
			document.add(new Paragraph("QR Code do Produto: " + pro.get().getNome()));

			// cria um qrcode com o id do produto
			BarcodeQRCode qrcode = new BarcodeQRCode(id + "", 200, 200, null);

			Image image = qrcode.getImage();

			// adiciona no arquivo e gera o arquivo
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

	@PostMapping(value = "barCodes")
	public String generatedPdfA(@RequestBody Produto[] produtos, HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException, JRException, DocumentException {
		/*
		 * if (produto.getCodProduto() < 10) { numeroFormatado= String.format("%.8f",
		 * number); System.out.println("passou 1"); }else if( >=100 && id <=199) {
		 * numeroFormatado = String.format("%.6f", number);
		 * System.out.println("passou 2"); }else if(id >=1000 && id == 9999) {
		 * numeroFormatado = String.format("%.5f", number);
		 * System.out.println("passou 3"); }else { numeroFormatado =
		 * String.format("%.7f", number); System.out.println("passou 4"); }
		 */
		try {
			Document document = new Document();
			// cria um arquivo pdf passando o documento e o lugar que vai ser salvo
			PdfWriter arquivo = PdfWriter.getInstance(document, response.getOutputStream());
			document.open();

			// biblioteca para gerar codigo de barras
			Barcode128 barcode = new Barcode128();

			// percorre o array de ids para gerar codigos de barras
			// de acordo com o tamanho do array
			for (Produto produto : produtos) {
				Optional<Produto> p = productRepository.findById(produto.getCodProduto());

				Random geradorNumero = new Random();

				System.out.println("Nome do produto: " + p.get().getNome());
				// gera um numero aleatório até 10
				double number = geradorNumero.nextDouble();

				String numeroFormatado = "";
				numeroFormatado = String.format("%.5f", number);

				String mascaraId = "";
				if (p.get().getCodProduto() >= 10) {
					mascaraId = "00";
				} else if (p.get().getCodProduto() >= 100) {
					mascaraId = "0";
				} else if (p.get().getCodProduto() < 10) {
					mascaraId = "000";
				}

				// formata o numero com 9 casas decimais

				// retira a vircula do número
				String mascaraCodigo = numeroFormatado.replace(",", "");

				// três primeiros digitos do codigo de barras brasileiro GTIN-13
				String padraoBr = "789";

				Paragraph paragrafo = new Paragraph();

				paragrafo.add("Codigo de Barras: " + p.get().getNome());
				// gerar o codigo de barras com a mascara e o identificador do produto
				 barcode.setCode(padraoBr + mascaraCodigo + mascaraId + p.get().getCodProduto());

				Image img = barcode.createImageWithBarcode(arquivo.getDirectContent(), BaseColor.BLACK,
						BaseColor.BLACK);
				img.scalePercent(200);
				document.add(paragrafo);
				document.add(img);

				// BarcodeQRCode qrCode = new BarcodeQRCode("Código de Barras do " , 300, 300,
				// null);
			}

			document.close();

			return "funcionou";
		} catch (Exception e) {
			return e.toString();
		}

	}

	// METODO PARA GERAR VARIOS QR CODES
	@RequestMapping(value = "qrCodes", method = RequestMethod.POST)
	public String generatedQrCodes(@RequestBody Produto[] produtos, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, JRException, DocumentException {

		Document document = new Document(new Rectangle(PageSize.A4));
		try {
			// cria um arquivo pdf passando o documento e o lugar que vai ser salvo
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

			document.open();

			// percorre o array de ids para gerar codigos de barras
			// de acordo com o tamanho do array
			for (Produto qrCodes : produtos) {
				Optional<Produto> p = productRepository.findById(qrCodes.getCodProduto());

				Paragraph paragrafo = new Paragraph();

				paragrafo.add("QR CODE: " + p.get().getNome());

				// adicionar o id no qrcode
				document.add(paragrafo);

				BarcodeQRCode qrcode = new BarcodeQRCode(qrCodes + "", 200, 200, null);

				Image image = qrcode.getImage();

				// gerar o pdf com vários qr codes
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
