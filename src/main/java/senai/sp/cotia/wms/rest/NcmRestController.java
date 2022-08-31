package senai.sp.cotia.wms.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.Ncm;

@RestController
@RequestMapping("api/estoque")
public class NcmRestController {
//	
//	
//	@Autowired
//	private NcmRepository ncmRepo;
//	
//	@RequestMapping(value = "save", method = RequestMethod.POST)
//	public ResponseEntity<Object> saveNcm(@RequestBody Ncm ncm, HttpServletRequest request, 
//			HttpServletResponse response){
//		
//		return ResponseEntity.ok().build();
//	}
}
