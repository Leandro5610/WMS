package senai.sp.cotia.wms.rest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.HistoricoQrCode;
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.model.Pedido;
import senai.sp.cotia.wms.repository.HistoricoRepository;

@RestController
@CrossOrigin
@RequestMapping("api/historico")
public class HistoricoRestController {

	private HistoricoRepository repository;

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Object> saveHistorico(@RequestBody HistoricoQrCode historico, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			repository.save(historico);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<HistoricoQrCode> historico() {

		
		return repository.findAll();
	}
	
	

}
