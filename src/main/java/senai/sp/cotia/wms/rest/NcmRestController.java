package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Ncm;
import senai.sp.cotia.wms.repository.NcmRepository;

@RestController
@RequestMapping("api/ncm")
public class NcmRestController {
	
	
	@Autowired
	private NcmRepository ncmRepo;
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Object> saveNcm(@RequestBody Ncm ncm, HttpServletRequest request, 
			HttpServletResponse response){
			try {
				ncmRepo.save(ncm);
			}catch (Exception e) {
				e.printStackTrace();
			}
		return ResponseEntity.ok().build();
	}
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Ncm> getNotasFiscais(){
		return ncmRepo.findAll();
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirNota(@PathVariable("id") Long idNota){
		ncmRepo.deleteById(idNota);
		return ResponseEntity.noContent().build();
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateNcm(@RequestBody Ncm ncm, @PathVariable("id" )Long id ){
		if(id != ncm.getId()) {
			throw new RuntimeException("Id Inválido");
		}
		ncmRepo.save(ncm);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/ncm"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Ncm> findAlunos(@PathVariable("id") Long idNcm, HttpServletRequest request, HttpServletResponse response){
		Optional<Ncm> ncm = ncmRepo.findById(idNcm);
		if(ncm.isPresent()) {
			return ResponseEntity.ok(ncm.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/*// metodo para procurar uma reserva à partir de qualquer atributo
		@RequestMapping(value = "/findbyall/{p}")
		public Iterable<Ncm> findByAll(@PathVariable("p") String param) {
			return ncmRepo.procurarTudo(param);
		}*/

}
