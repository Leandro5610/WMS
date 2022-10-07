package senai.sp.cotia.wms.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

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
import senai.sp.cotia.wms.model.Professor;
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

	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object cadastrarProfessor(@RequestBody Professor professor) {
		try {
			if(professor.getImagem() != null) {
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

				firebase.uploadFile(file, decodificada);
				fileInput.close();
				// salvar o usuário no banco de dados
				repo.save(professor);
				Files.delete(pathFile);
			}else {
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

}
