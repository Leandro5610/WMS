package senai.sp.cotia.wms.rest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.HistoricoQrCode;
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.model.Pedido;
import senai.sp.cotia.wms.model.UnidadeMedida;
import senai.sp.cotia.wms.repository.HistoricoRepository;

@RestController
@CrossOrigin
@RequestMapping("api/historico")
public class HistoricoRestController {

	@Autowired
	private HistoricoRepository histRepository;

	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object saveHistorico(@RequestBody HistoricoQrCode historico) {
		try {
			// salvar o usuário no banco de dados
			histRepository.save(historico);
			return ResponseEntity.ok(HttpStatus.CREATED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<HistoricoQrCode> historico() {
		return histRepository.findAll();
	}
	
	

}
