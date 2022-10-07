package senai.sp.cotia.wms.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.Membros;
import senai.sp.cotia.wms.repository.MembrosRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/Membros")
public class MembrosRestController {
	@Autowired
	private MembrosRepository membros;
	
	@GetMapping( value = "teste/{id}")
	public List<Membros> pegarMembros(@PathVariable("id") Long idTurma){
		return membros.pegarMembro(idTurma);
	}
	
	
}
