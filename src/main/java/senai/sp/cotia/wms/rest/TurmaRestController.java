package senai.sp.cotia.wms.rest;


import java.net.URI;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.Erro;
import senai.sp.cotia.wms.model.Professor;
import senai.sp.cotia.wms.model.Turma;

import senai.sp.cotia.wms.repository.TurmaRepository;
import senai.sp.cotia.wms.util.FireBaseUtil;

@RestController
@CrossOrigin
@RequestMapping("api/turma")
public class TurmaRestController {
	@Autowired
	private TurmaRepository repo;
	@Autowired
	private FireBaseUtil firebase;

	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object cadastrarMedida(@RequestBody Turma turma, HttpServletRequest request, HttpServletResponse response) {

		Erro erro = new Erro();
		Calendar dataAtual = Calendar.getInstance();

		try {
			// verificar se a data final é antes da data de inicio
			if (turma.getDataFinal().before(turma.getDataInicio())) {
				return new ResponseEntity<Object>(erro, HttpStatus.NOT_ACCEPTABLE);
			}
			// verificar se a data de inicio é no domingo
			else if (turma.getDataInicio().get(Calendar.DAY_OF_WEEK) == 1) {
				return new ResponseEntity<Object>(erro, HttpStatus.NOT_ACCEPTABLE);
			}
			// verificar se a data de inicio é antes da data atual
			else if (turma.getDataInicio().before(dataAtual)) {
				return ResponseEntity.badRequest().build();

			} else if (turma.getDataFinal().get(Calendar.DAY_OF_WEEK) == 1) {
				return new ResponseEntity<Object>(erro, HttpStatus.NOT_ACCEPTABLE);

			} else if (turma.getImagem() != null) {

				repo.save(turma);
				return ResponseEntity.ok(HttpStatus.CREATED);

			} else {
				// salvar o usuário no banco de dados
				repo.save(turma);
				return ResponseEntity.ok(HttpStatus.CREATED);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Turma> getTurma(@PathVariable("id") Long idTurma) {
		
		Optional<Turma> turma = repo.findById(idTurma);
		if (turma.isPresent()) {
			return ResponseEntity.ok(turma.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deletaTurma(@PathVariable("id") Long idTurma) {
		repo.deleteById(idTurma);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Turma> listTurma() {
		return repo.findAll();
	}

	// metodo para procurar uma reserva à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}", method = RequestMethod.GET)
	public Iterable<Turma> findByAll(@PathVariable("p") String param) {
		return repo.procurarTudo(param);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> colocarMembros(@RequestBody Turma turma, @PathVariable("id") Long id) {
		if (id != turma.getId()) {
			throw new RuntimeException("ID Inválido");
		}
		try {
			if (turma.getImagem() != null) {
				repo.save(turma);
				return ResponseEntity.ok().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		repo.save(turma);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/turma"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}

	@RequestMapping(value = "turmaByProf/{id}")
	public List<Turma> procurarTurmaPorProf(@PathVariable("id") Professor idProf) {
		return repo.procurarTurmaPeloProf(idProf);
	}
}
