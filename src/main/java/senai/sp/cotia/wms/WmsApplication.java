package senai.sp.cotia.wms;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import senai.sp.cotia.wms.model.Professor;
import senai.sp.cotia.wms.repository.ProdutoRepository;
import senai.sp.cotia.wms.repository.ProfessorRepository;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableScheduling
public class WmsApplication {
	@Autowired
	private ProfessorRepository repository;

	public static void main(String[] args) {		
		SpringApplication.run(WmsApplication.class, args);
		
	}


	// Definindo o tempo para deletar os arquivos da pasta relatórios
	// Toda a quarta-feira as 13:00 da tarde
	// Definindo o tempo para deletar os arquivos da pasta relatórios
	// Toda a quarta-feira as 13:00 da tarde
	@Scheduled(cron = "0 0 13 ? * TUE")
	public void task() throws Exception {
		String nameXml = new File("src\\main\\resources\\static\\relatorios").getAbsolutePath();
		File file = new File(nameXml);

		for (File f : file.listFiles()) {
			f.delete();
		}

	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void saveProf() {
		List<Professor> prof = repository.findAll();
		Professor profPadrao = new Professor();
		
		if (prof.isEmpty()) {
			profPadrao.setEmail("admin@gmail.com");
			profPadrao.setNif("1234");
			profPadrao.setNome("Admin");
			profPadrao.setSenha("admin");
			repository.save(profPadrao);
		}else {
			System.out.println("Não é necessário fazer cadastro");
		}
	}
}
	
