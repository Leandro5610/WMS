package senai.sp.cotia.wms.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import senai.sp.cotia.wms.annotation.Privado;
import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Professor;
import senai.sp.cotia.wms.model.TokenWms;
import senai.sp.cotia.wms.model.Turma;
import senai.sp.cotia.wms.repository.ProfessorRepository;
import senai.sp.cotia.wms.util.FireBaseUtil;

@RestController
@CrossOrigin
@RequestMapping("api/professor")
public class ProfessorRestController {

	@Autowired
	private ProfessorRepository repo;
	@Autowired
	private FireBaseUtil firebase;

	public static final String EMISSOR = "Sen@i";
	public static final String SECRET = "@msSenai";

	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object cadastrarProfessor(@RequestBody Professor professor) {
		try {
			if (professor.getImagem() != null) {
				// variavel para guardar a imagem codificada Base64 que está vindo do front
				String stringImagem = professor.getImagem();

				// variaveis para extrair o que está entre a / e o ;
				int posicaoBarra = stringImagem.indexOf('/');
				int posicaoPontoVirgula = stringImagem.indexOf(';');

				// variavel para retirar a / e o ; para pegar a extensão da imagem
				String extensao = stringImagem.substring(posicaoBarra, posicaoPontoVirgula);

				// variavel para retirar a / da extensão
				String extensaoOriginal = extensao.replace("/", "");

				// variavel para retirar o texto data:imagem/enxtensão;base64, que está vindo do
				// base64 codificado do front-end
				String base64ImagemString = stringImagem.replace("data:image/" + extensaoOriginal + ";base64,", "");

				// variavel para para decodificar o codigo base64 e converter em um vetor de
				// bytes
				byte[] decodificada = Base64.getDecoder().decode(base64ImagemString);

				// variavel para converter o vetor de bytes em um texto
				String arquivoString = decodificada.toString();

				// variavel para retirar o texto "[B@" da variavel arquivoString
				String arquivo = arquivoString.replace("[B@", "");

				// variavel para gerar um nome aleatório para o arquivo e juntar com a extensão
				String nomeArquivo = UUID.randomUUID().toString() + arquivo + "." + extensaoOriginal;

				// variavel para guardar o nome do arquivo em um File
				File file = new File(nomeArquivo);

				// variavel para converter em arquivo e armazenar no sistema do pc
				FileOutputStream fileInput = new FileOutputStream("temporaria/" + file);

				// variavel para escrever os bytes no arquivo
				fileInput.write(decodificada);

				// variavel para pegar o caminho da pasta com o arquivo da imagem
				Path pathFile = Paths.get("temporaria/" + nomeArquivo);
				
				//fazer o upload da imagem no fire base
				firebase.uploadFile(file, decodificada);
				fileInput.close();
				//inserir o nome da imagem no professor
				professor.setImagem(file.toString());
				repo.save(professor);
				//excluir imagem depois de salvar o professor 
				Files.delete(pathFile);
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
				// variavel para guardar a imagem codificada Base64 que está vindo do front
				String stringImagem = professor.getImagem();

				// variaveis para extrair o que está entre a / e o ;
				int posicaoBarra = stringImagem.indexOf('/');
				int posicaoPontoVirgula = stringImagem.indexOf(';');

				// variavel para retirar a / e o ; para pegar a extensão da imagem
				String extensao = stringImagem.substring(posicaoBarra, posicaoPontoVirgula);

				// variavel para retirar a / da extensão
				String extensaoOriginal = extensao.replace("/", "");

				// variavel para retirar o texto data:imagem/enxtensão;base64, que está vindo do
				// base64 codificado do front-end
				String base64ImagemString = stringImagem.replace("data:image/" + extensaoOriginal + ";base64,", "");

				// variavel para para decodificar o codigo base64 e converter em um vetor de
				// bytes
				byte[] decodificada = Base64.getDecoder().decode(base64ImagemString);

				// variavel para converter o vetor de bytes em um texto
				String arquivoString = decodificada.toString();

				// variavel para retirar o texto "[B@" da variavel arquivoString
				String arquivo = arquivoString.replace("[B@", "");

				// variavel para gerar um nome aleatório para o arquivo e juntar com a extensão
				String nomeArquivo = UUID.randomUUID().toString() + arquivo + "." + extensaoOriginal;

				// variavel para guardar o nome do arquivo em um File
				File file = new File(nomeArquivo);

				// variavel para converter em arquivo e armazenar no sistema do pc
				FileOutputStream fileInput = new FileOutputStream("temporaria/" + file);

				// variavel para escrever os bytes no arquivo
				fileInput.write(decodificada);

				// variavel para pegar o caminho da pasta com o arquivo da imagem
				Path pathFile = Paths.get("temporaria/" + nomeArquivo);
				
				//fazer o upload da imagem no fire base
				firebase.uploadFile(file, decodificada);
				fileInput.close();
				//inserir o nome da imagem no professor
				professor.setImagem(file.toString());
				repo.save(professor);
				//excluir imagem depois de salvar o professor 
				Files.delete(pathFile);
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

	// metodo para procurar uma reserva à partir de qualquer atributo
//	@RequestMapping(value = "/findbyall/{p}")
//	public Iterable<Professor> findByAll(@PathVariable("p") String param) {
//		return repo.procurarTudo(param);
//	}

	@RequestMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TokenWms> login(@RequestBody Professor professor) {
		List<Professor> prof = repo.findAll();
		
		for (Professor professor2 : prof) {
			if (professor.getNif().equals(professor2.getNif()) && professor.getSenha().equals(professor2.getSenha())) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("professor_id", professor.getId());
				map.put("professor_nif", professor.getNif());

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

}
