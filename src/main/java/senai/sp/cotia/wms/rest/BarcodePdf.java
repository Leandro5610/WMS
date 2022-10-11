package senai.sp.cotia.wms.rest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("api/barcode")
public class BarcodePdf {

	
	@RequestMapping(value = "pdf", method = RequestMethod.GET)
	public String generatedPdf() throws FileNotFoundException, JRException, DocumentException {
		
		try{
			
		Document document = new Document();
		
		PdfWriter arquivo = PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\bruno\\Downloads\\teste.pdf"));
		document.open();
		
		Barcode128 barcode = new Barcode128();
		
		barcode.setCode("789");
		
		Image img = barcode.createImageWithBarcode(arquivo.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);

		
		img.scalePercent(200);
		document.add(img);
		document.close();
		
		return "funcionou";
		}catch (Exception e) {
			return "deu errado";
		}
		
		
	}
	
}
