package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.model.Membros;
import senai.sp.cotia.wms.repository.MembrosRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/membros")
public class MembrosRestController {
	@Autowired
	private MembrosRepository membrosR;
	
	/*@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveMembro(@RequestBody Membros membros) {
		try {
			
			membrosR.save(membros);
			return ResponseEntity.ok(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}*/
	
	@GetMapping(value = "teste/{id}")
	public List<Membros> pegarMembros(@PathVariable("id") Long idTurma){
		return membrosR.pegarMembro(idTurma);
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Membros> listarPedidos(){
		return membrosR.findAll();
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirMembro(@PathVariable("id") Long idMovimentacao) {
		membrosR.deleteById(idMovimentacao);
		return ResponseEntity.noContent().build();
	}

}
