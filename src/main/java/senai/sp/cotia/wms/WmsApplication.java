package senai.sp.cotia.wms;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableScheduling
public class WmsApplication{
	

	public static void main(String[] args) {
		SpringApplication.run(WmsApplication.class, args);
	}
	@Scheduled(cron = "0 13 * * 3 *")
	public void task() throws Exception{
		String nameXml = new File( "src\\main\\resources\\static\\relatorios").getAbsolutePath();
		File file = new File (nameXml);
		for (File f : file.listFiles()) {
			f.delete();
		} 
	}
	
}
