package senai.sp.cotia.wms.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileUtils;
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
import senai.sp.cotia.wms.annotation.Publico;
import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.TokenWms;
import senai.sp.cotia.wms.model.Turma;
import senai.sp.cotia.wms.repository.AlunoRepository;
import senai.sp.cotia.wms.serevices.EmailService;
import senai.sp.cotia.wms.util.FireBaseUtil;

@CrossOrigin
@RestController
@RequestMapping("api/aluno")
public class AlunoRestController {
	@Autowired
	private AlunoRepository repository;

	@Autowired
	private FireBaseUtil fire;

	@Autowired
	private EmailService service = new EmailService();

	public static final String EMISSOR = "Sen@i";
	public static final String SECRET = "@msSenai";

	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveAluno(@RequestBody Aluno aluno, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {
		try {
			
			// verificar se o aluno tem uma imagem
			if (aluno.getImagem() != null) {
				// variavel para guardar a imagem codificada Base64 que está vindo do front
				String stringImagem = aluno.getImagem();
				
				// variaveis para extrair o que está entre a / e o ;
				int posicaoBarra = stringImagem.indexOf('/');
				int posicaoPontoVirgula = stringImagem.indexOf(';');

				// variavel para retirar a / e o ; para pegar a extensão da imagem
				String extensao = stringImagem.substring(posicaoBarra, posicaoPontoVirgula);

				// variavel para retirar a / da extensão
				String ex = extensao.replace("/", "");

				// variavel para retirar o texto data:imagem/enxtensão;base64, que está vindo do
				// base64 codificado do front-end
				String base64ImageString = stringImagem.replace("data:image/" + ex + ";base64,", "");

				// variavel para para decodificar o codigo base64 e converter em um vetor de
				// bytes
				byte[] decode = Base64.getDecoder().decode(base64ImageString);

				// variavel para converter o vetor de bytes em um texto
				String arquivoString = decode.toString();

				// variavel para retirar o texto "[B@" da variavel arquivoString
				String arquivo = arquivoString.replace("[B@", "");

				// variavel para gerar um nome aleatório para o arquivo e juntar com a extensão
				String nomeArquivo = UUID.randomUUID().toString() + arquivo + "." + ex;

				// variavel para guardar o nome do arquivo em um File
				File file = new File(nomeArquivo);

				// variavel para converter em arquivo e armazenar na pasta da raiz da aplicação
				FileOutputStream in = new FileOutputStream("temporaria/" + file);

				// variavel para escrever os bytes no arquivo
				in.write(decode);

				// pegar o arquivo que foi salvo na pasta temporaria
				Path pathFile = Paths.get("temporaria/" + nomeArquivo);

				// fazer o upload do arquivo no Fire Base (nuvem)
				fire.uploadFile(file, decode);

				in.close();
				// inserir nome da imagem no aluno que está vindo do Front
				aluno.setImagem(file.toString());

				// salva o aluno Banco de Dados
				repository.save(aluno);

				// excluir imagem da pasta temporaria depois de salvar o aluno
				Files.delete(pathFile);
				return new ResponseEntity<Object>(HttpStatus.CREATED);
			} else {
				//se o usuario não tiver foto ele salva mesmo assim
				repository.save(aluno);
				return new ResponseEntity<Object>(HttpStatus.CREATED);
			}
		} catch (Exception e) {
			// TODO: handle exceptionelse {
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Aluno> findAluno(@PathVariable("id") Long idAluno, HttpServletRequest request,
			HttpServletResponse response) {
		Optional<Aluno> aluno = repository.findById(idAluno);
		//verifica se o aluno existe
		if (aluno.isPresent()) {
			return ResponseEntity.ok(aluno.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateAluno(@RequestBody Aluno aluno, @PathVariable("id") Long id) {
		//verfica se o id da requição existe no banco de dados
		if (id != aluno.getId()) {
			throw new RuntimeException("Id Inválido");
		}
		//salva o aluno com as alteraçoes 
		repository.save(aluno);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/aluno"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteAluno(@PathVariable("id") Long idAluno) {
		repository.deleteById(idAluno);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Aluno> listAluno() {
		ArrayList<Aluno> list = new ArrayList<Aluno>();
		list = (ArrayList<Aluno>) repository.findAll();
		System.out.println(list);
		return repository.findAll();
	}

	// metodo para procurar um aluno à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}")
	public Iterable<Aluno> findByAll(@PathVariable("p") String param) {
		return repository.procurarTudo(param);
	}
	
	//METODO PARA PROCURAR O ALUNO PELA TURMA
	@RequestMapping(value = "/turma/{id}", method = RequestMethod.GET)
	public Iterable<Aluno> findByTurma(@PathVariable("id") Long id) {
		return repository.findByTurmaId(id);
	}
	/*
	 * @RequestMapping(value = "/confirma/{p}", method = RequestMethod.GET) public
	 * Aluno findByTurma(@PathVariable("p") String email) { return
	 * repository.findByEmail(email); }
	 */

	/*
	 * @RequestMapping(value = "/confirma/{p}", method = RequestMethod.GET) public
	 * Aluno confirmaEmail(@PathVariable("p") String email) { Aluno alnEmail =
	 * repository.findByEmail(email); String emailAln = alnEmail.getEmail(); if
	 * (emailAln.equals(email)) { return alnEmail; } throw new
	 * RuntimeException("Email não encontrado"); }
	 */

	/*@RequestMapping(value = "/sendEmail/{p}", method = RequestMethod.GET)
	public Aluno enviarEmail(@PathVariable("p") String email) {
		Aluno alunoEmail = repository.findByEmail(email);
		String emailAln = alunoEmail.getEmail();
		if (emailAln.equals(email)) {
			service.mandarEmail(alunoEmail, email);
			System.out.println(email);
			System.out.println(alunoEmail);
			return alunoEmail;
		}

		throw new RuntimeException("Email não encontrado");
	}*/
	
	//METODO PARA INSERIR A TURMA NO ALUNO
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updateTurmaAluno(@RequestBody Turma turma, @PathVariable("id") Long id) {
		Aluno aluno = repository.findAlunoById(id);
		aluno.setTurma(turma);
		repository.save(aluno);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/aluno"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}

	//METODO PARA RETIRAR O ALUNO DA TURMA
	@RequestMapping(value = "delete/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> deleteAlunoTurma(@PathVariable("id") Long id, @RequestBody Turma turma) {
		Aluno aluno = repository.findAlunoById(id);
		if (id != aluno.getId()) {
			throw new RuntimeException("Id Inválido");
		}
		aluno.setTurma(null);
		repository.save(aluno);
		return ResponseEntity.noContent().build();
	}
	
	//METODO PARA RECUPERAR A SENHA
	@RequestMapping(value = "recuperarSenha/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> recuperaSenha(@RequestBody Aluno aluno, @PathVariable("id") Long id) {
		aluno = repository.findAlunoById(id);
		if (id != aluno.getId()) {
			throw new RuntimeException("Id Inválido");
		}
		repository.save(aluno);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/aluno"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}

	@RequestMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public Object login(@RequestBody Aluno aluno) {
		aluno = repository.findByCodMatriculaAndSenha(aluno.getCodMatricula(), aluno.getSenha());
		//verifica se o aluno existe
		if (aluno != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			//guarda o código de matricula e id no payload
			map.put("aluno_codMatricula", aluno.getCodMatricula());
			map.put("aluno_id", aluno.getId());
			
			Calendar expiracao = Calendar.getInstance();

			//tempo de expiração do token 12 horas
			expiracao.add(Calendar.HOUR, 12);

			Algorithm algoritimo = Algorithm.HMAC256(SECRET);

			TokenWms token = new TokenWms();
			token.setToken(JWT.create().withPayload(map).withIssuer(EMISSOR).withExpiresAt(expiracao.getTime())
					.sign(algoritimo));
			return ResponseEntity.ok(token);
		} else {
			return new ResponseEntity<TokenWms>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	// decoda o token para pegar o id do aluno que está logado na sessão
	@RequestMapping(value = "sendId", method = RequestMethod.GET)
	public ResponseEntity<Long> decoda(HttpServletRequest request, HttpServletResponse response) {
		String token = null;
		// obtem o token da request
		token = request.getHeader("Authorization");
		// algoritimo para descriptografar
		Algorithm algoritimo = Algorithm.HMAC256(AlunoRestController.SECRET);
		// objeto para verificar o token
		JWTVerifier verifier = JWT.require(algoritimo).withIssuer(AlunoRestController.EMISSOR).build();
		// validar o token
		DecodedJWT decoded = verifier.verify(token);
		// extrair os dados do payload
		Map<String, Claim> payload = decoded.getClaims();
		String id = payload.get("aluno_id").toString();
		Long idl = Long.parseLong(id);
		return ResponseEntity.ok(idl);
	}

    @RequestMapping(value = "/login/{codMatricula}")
    public ResponseEntity<Aluno> findAlunoByCodMatricula(@PathVariable("codMatricula") String codMatricula, HttpServletRequest request,
            HttpServletResponse response) {
        Optional<Aluno> aluno = repository.findByCodMatricula(codMatricula);
        if (aluno.isPresent()) {
            return ResponseEntity.ok(aluno.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
