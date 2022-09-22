package senai.sp.cotia.wms.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.type.Demanda;
import senai.sp.cotia.wms.type.Periodo;
import senai.sp.cotia.wms.type.Tipo;
@CrossOrigin
@RestController
@RequestMapping("/api/enumeracoes")
public class EnumeracoesRestController {
	
	@RequestMapping(value = "demandas", method = RequestMethod.GET)
	public Demanda[] getDemanda() {
		Demanda[] demandas = Demanda.values();
		return demandas;
	}
	
	@RequestMapping(value = "periodos", method = RequestMethod.GET)
	public Periodo[] getPeriodos() {
		Periodo[] periodos = Periodo.values();
		return periodos;
	}
	
	@RequestMapping(value = "movimentacoes", method = RequestMethod.GET)
	public Tipo[] getTipos() {
		Tipo[] tipos = Tipo.values();
		return tipos;
	}
	
}
