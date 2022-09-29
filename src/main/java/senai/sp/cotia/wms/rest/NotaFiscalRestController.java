package senai.sp.cotia.wms.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.NotaFiscal;
import senai.sp.cotia.wms.repository.NotaFiscalRepository;

@RestController
@CrossOrigin
@RequestMapping("api/notafiscal")
public class NotaFiscalRestController {
	
	@Autowired
	private NotaFiscalRepository nfRepo;
	
	public ResponseEntity<Object> saveNotaFiscal(@RequestBody NotaFiscal nota, HttpServletRequest request,
			HttpServletResponse response){
		
		try {
		nfRepo.save(nota);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().build();
	}
	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<NotaFiscal> getNotasFiscais(){
		return nfRepo.findAll();
	}
	
	@RequestMapping(value = "/{id}")
	public ResponseEntity<Void> excluirNota(@PathVariable("id") Long idNota){
		nfRepo.deleteById(idNota);
		return ResponseEntity.noContent().build();
	}
}
