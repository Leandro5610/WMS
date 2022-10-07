package senai.sp.cotia.wms.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.repository.MembrosRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/Membros")
public class MembrosRestController {
	@Autowired
	private MembrosRepository membros;
	
	
}
