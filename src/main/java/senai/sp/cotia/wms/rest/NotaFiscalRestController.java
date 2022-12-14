package senai.sp.cotia.wms.rest;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import senai.sp.cotia.wms.annotation.Privado;
import senai.sp.cotia.wms.model.NotaFiscal;
import senai.sp.cotia.wms.repository.ItemNotaRepository;
import senai.sp.cotia.wms.repository.NotaFiscalRepository;

@RestController
@CrossOrigin
@RequestMapping("api/notaFiscal")
public class NotaFiscalRestController {

	@Autowired
	private NotaFiscalRepository nfRepo;

	private ItemNotaRepository itemRepo;

	public ResponseEntity<Object> saveNotaFiscal(@RequestBody NotaFiscal nota, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			nfRepo.save(nota);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().build();
	}
	
	@Privado
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<NotaFiscal> getNotasFiscais() {
		return nfRepo.findAll();
	}
	
	@Privado
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirNota(@PathVariable("id") Long idNota) {
		nfRepo.deleteById(idNota);
		return ResponseEntity.noContent().build();
	}
	
	@Privado
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<NotaFiscal> findNota(@PathVariable("id") Long idNota) {
		// buscar nota
		Optional<NotaFiscal> nota = nfRepo.findById(idNota);

		// verificação de nota
		if (nota.isPresent()) {
			return ResponseEntity.ok(nota.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@RequestMapping(value = "pega/{cod}", method = RequestMethod.GET)
	public NotaFiscal vamove(@PathVariable("cod") String param) {
		Long id = Long.parseLong(param);
		return nfRepo.pegaPedido(id);
	}

	

	


}
