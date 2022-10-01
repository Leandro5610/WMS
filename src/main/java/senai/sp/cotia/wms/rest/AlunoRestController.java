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
import java.util.Optional;
import java.util.UUID;

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
import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.repository.AlunoRepository;
import senai.sp.cotia.wms.util.FireBaseUtil;

@CrossOrigin
@RestController
@RequestMapping("api/aluno")
public class AlunoRestController {
	@Autowired
	private AlunoRepository repository;
	@Autowired
	private FireBaseUtil fire;

	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveAluno(@RequestBody Aluno alunoString, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {
		try{
		if(alunoString.getImagem() != null) {
			// variavel para guardar a imagem codificada Base64 que está vindo do front
			String stringImagem = alunoString.getImagem();

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

			// variavel para converter em arquivo e armazenar no sistema do pc
			FileOutputStream in = new FileOutputStream("temporaria/" + file);

			// variavel para escrever os bytes no arquivo
			in.write(decode);
			
			Path pathFile = Paths.get("temporaria/" + nomeArquivo);
			
			fire.uploadFile(file, decode);
			in.close();
			alunoString.setImagem(file.toString());
			
			Files.delete(pathFile);
		}else {
			repository.save(alunoString);
		}
		} catch (Exception e) {
			// TODO: handle exceptionelse {
			e.printStackTrace();

			
		}
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Aluno> findAluno(@PathVariable("id") Long idAluno, HttpServletRequest request,
			HttpServletResponse response) {
		Optional<Aluno> aluno = repository.findById(idAluno);
		if (aluno.isPresent()) {
			return ResponseEntity.ok(aluno.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateAluno(@RequestBody Aluno aluno, @PathVariable("id") Long id) {
		if (id != aluno.getId()) {
			throw new RuntimeException("Id Inválido");
		}
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

	// metodo para procurar uma reserva à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}")
	public Iterable<Aluno> findByAll(@PathVariable("p") String param) {
		return repository.procurarTudo(param);
	}
	
	
}
