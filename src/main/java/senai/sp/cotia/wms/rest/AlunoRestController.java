package senai.sp.cotia.wms.rest;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.repository.AlunoRepository;

@RestController
@RequestMapping("api/aluno")
public class AlunoRestController {
	@Autowired
	private AlunoRepository repository;
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Object> saveAluno(@RequestBody Aluno aluno, HttpServletRequest request,
			HttpServletResponse response, HttpSession session){
				return null;
		
	}
}
