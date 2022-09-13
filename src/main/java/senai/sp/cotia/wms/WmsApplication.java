package senai.sp.cotia.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class WmsApplication{

	public static void main(String[] args) {
		SpringApplication.run(WmsApplication.class, args);
	}

}
