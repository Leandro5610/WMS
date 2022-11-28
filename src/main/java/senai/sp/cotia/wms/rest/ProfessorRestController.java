package senai.sp.cotia.wms.rest;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import senai.sp.cotia.wms.annotation.Privado;
import senai.sp.cotia.wms.model.Professor;
import senai.sp.cotia.wms.model.TokenWms;
import senai.sp.cotia.wms.repository.ProfessorRepository;
import senai.sp.cotia.wms.services.EmailService;
import senai.sp.cotia.wms.util.FireBaseUtil;

@RestController
@CrossOrigin
@RequestMapping("api/professor")
public class ProfessorRestController {

	@Autowired
	private ProfessorRepository repo;
	@Autowired
	private FireBaseUtil firebase;
	@Autowired
	private EmailService service;

	public static final String EMISSOR = "Sen@i";
	public static final String SECRET = "@msSenai";

	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object cadastrarProfessor(@RequestBody Professor professor) {
		try {
			if (professor.getImagem() != null) {

				repo.save(professor);

			} else {
				repo.save(professor);
				return ResponseEntity.ok(HttpStatus.CREATED);

			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok().build();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Professor> getProfessores(@PathVariable("id") Long idUni) {
		
		Optional<Professor> prof = repo.findById(idUni);
		if (prof.isPresent()) {
			return ResponseEntity.ok(prof.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deletarProfessor(@PathVariable("id") Long id) {
		repo.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizarProfessor(@PathVariable("id") Long id, @RequestBody Professor professor) {
		
		if (id != professor.getId()) {
			throw new RuntimeException("id invalido");
		}
		try {
			if (professor.getImagem() != null) {

				repo.save(professor);

			}
		} catch (Exception e) {

		}

		repo.save(professor);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/professor"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);

	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Professor> listAluno() {
		return repo.findAll();
	}

	// metodo para procurar um professor à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}", method = RequestMethod.GET)
	public Iterable<Professor> findByAll(@PathVariable("p") String param) {
		return repo.procurarTudo(param);
	}

	@RequestMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<TokenWms> login(@RequestBody Professor professor) {
		List<Professor> prof = repo.findAll();

		for (Professor professor2 : prof) {
			if (professor.getNif().equals(professor2.getNif()) && professor.getSenha().equals(professor2.getSenha())) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("professor_id", professor2.getId());
				map.put("professor_nif", professor2.getNif());

				Calendar expiracao = Calendar.getInstance();
				expiracao.add(Calendar.HOUR, 12);

				Algorithm algoritimo = Algorithm.HMAC256(SECRET);

				TokenWms token = new TokenWms();
				token.setToken(JWT.create().withPayload(map).withIssuer(EMISSOR).withExpiresAt(expiracao.getTime())
						.sign(algoritimo));
				return ResponseEntity.ok(token);
			}
		}
		return new ResponseEntity<TokenWms>(HttpStatus.UNAUTHORIZED);

	}

	// decoda o token para pegar o id do usuário que está logado na sessão
	@Privado
	@RequestMapping(value = "sendId", method = RequestMethod.GET)
	public ResponseEntity<Long> decoda(HttpServletRequest request, HttpServletResponse response) {
		String token = null;
		// obtem o token da request
		token = request.getHeader("Authorization");
		// algoritimo para descriptografar
		Algorithm algoritimo = Algorithm.HMAC256(ProfessorRestController.SECRET);
		// objeto para verificar o token
		JWTVerifier verifier = JWT.require(algoritimo).withIssuer(ProfessorRestController.EMISSOR).build();
		// validar o token
		DecodedJWT decoded = verifier.verify(token);
		// extrair os dados do payload
		Map<String, Claim> payload = decoded.getClaims();
		String id = payload.get("professor_id").toString();
		Long idl = Long.parseLong(id);
		return ResponseEntity.ok(idl);
	}

	// METODO PARA RECUPERAR A SENHA
	@RequestMapping(value = "recuperarSenha/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> recuperaSenha(@RequestBody Professor professor, @PathVariable("id") Long id) {
		
		if (id != professor.getId()) {
			throw new RuntimeException("Id Inválido");
		}
		repo.save(professor);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/professor"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}

	@PostMapping(value = "/buscarEmail/{e}")
	public ResponseEntity<Professor> verifEmail(@RequestBody Professor professor, @PathVariable("e") String email) {

		Professor profBd = repo.findByEmail(professor.getEmail());

		if (professor.getEmail().equals(profBd.getEmail())) {

			Random random = new Random();
			profBd.setCodigo(random.nextInt(1000));
			repo.save(profBd);
			service.sendingEmailProf(email, profBd.getCodigo());
			return ResponseEntity.ok(profBd);

		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(value = "/verificarCod")
	public ResponseEntity<Professor> verifCodigo(@RequestBody Professor professor, String codigo) {

		Professor codigoVerificacao = repo.findByCodigoAndEmail(professor.getCodigo(), professor.getEmail());

		if (codigoVerificacao != null) {
			System.out.println("codigo certo");
			return ResponseEntity.ok(codigoVerificacao);
		} else {
			System.out.println("bruh");
			return ResponseEntity.notFound().build();
		}
	}
}
