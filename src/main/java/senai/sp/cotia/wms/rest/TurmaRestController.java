package senai.sp.cotia.wms.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
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

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Membros;
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
	public Object cadastrarMedida(@RequestBody Turma turma, HttpServletRequest request,
			HttpServletResponse response) {
		
		try {
			if(turma.getImagem() != null) {
				// variavel para guardar a imagem codificada Base64 que está vindo do front
				String stringImagem = turma.getImagem();

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
				firebase.uploadFile(file, decodificada);
				repo.save(turma);
				fileInput.close();
				
				for (Membros membros : turma.getMembros()) {
					membros.setTurma(turma);
					membros.setAluno(membros.getAluno());
					membros.setProfessor(turma.getProf());
					
				}
				
			}else {
				// salvar o usuário no banco de dados
				repo.save(turma);
				return ResponseEntity.ok(HttpStatus.CREATED);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;

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
	public ResponseEntity<Void> deletaUnidades(@PathVariable("id") Long idTurma) {
		repo.deleteById(idTurma);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizaTurma(@PathVariable("id") Long id, @RequestBody Turma turma) {
		if (id != turma.getId()) {
			throw new RuntimeException("id invalido");
		}
		repo.save(turma);
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/turma"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);

	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Turma> listAluno() {
		return repo.findAll();
	}

	// metodo para procurar uma reserva à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}")
	public Iterable<Turma> findByAll(@PathVariable("p") String param) {
		return repo.procurarTudo(param);
	}

}
