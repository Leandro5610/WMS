package senai.sp.cotia.wms.rest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.StyleConstants.ColorConstants;

import org.springframework.web.bind.annotation.PathVariable;
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

@RestController
@RequestMapping("api/pdf")
public class BarcodePdf {
	@Privado
	@RequestMapping(value = "barcode/{id}", method = RequestMethod.GET)
	public String generatedPdf(@PathVariable("id") Long idProduto, HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException, JRException, DocumentException {

		try {

			Document document = new Document();

			response.setContentType("apllication/pdf");

			response.addHeader("Content-Disposition", "inline; filename=" + "codigo.pdf");

			PdfWriter arquivo = PdfWriter.getInstance(document, response.getOutputStream());
			document.open();

			Barcode128 barcode = new Barcode128();

			barcode.setCode("78983374" + idProduto.toString());

			Image img = barcode.createImageWithBarcode(arquivo.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);

			BarcodeQRCode qrCode = new BarcodeQRCode("Hello", 300, 300, null);

			img.scalePercent(200);
			document.add(img);
			document.close();

			return "funcionou";
		} catch (Exception e) {
			return "deu errado";
		}

	}
	
	@Privado
	@RequestMapping(value = "qrcode/{id}", method = RequestMethod.GET)
	public String generatedQrCode(@PathVariable("id") Long id, HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException, JRException, DocumentException {

		Document document = new Document(new Rectangle(PageSize.A4));
		try {
			response.setContentType("apllication/pdf");

			response.addHeader("Content-Disposition", "inline; filename=" + "qrCode.pdf");
			
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			
			document.open();

			// QR Code Barcode
			document.add(new Paragraph("QR Code Barcode"));

			BarcodeQRCode qrcode = new BarcodeQRCode(id+"", 200, 200, null);
			
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

}
