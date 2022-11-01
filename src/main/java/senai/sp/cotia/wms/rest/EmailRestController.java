package senai.sp.cotia.wms.rest;

import java.util.Optional;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.EmailModel;
import senai.sp.cotia.wms.repository.AlunoRepository;
import senai.sp.cotia.wms.serevices.EmailService;

@RestController
@RequestMapping("api/email")
public class EmailRestController {

	@Autowired
	EmailService emailService;

	@Autowired
	private AlunoRepository repository;

	@RequestMapping(value = "sending-email", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> sendingEmail(String email) {
		Aluno aluno = repository.findByEmail(email);
		System.out.println(aluno.getNome());
		if (aluno.getEmail().equals(email)) {
			Properties pro = new Properties();
			pro.put("mail.smtp.host", "smtp.gmail.com");
			pro.put("mail.smtp.auth", "true");
			pro.put("mail.smtp.port", "587");
			pro.put("mail.smtp.starttls.enable", "true");
			pro.put("mail.smtp.ssl.protocols=TLSv1.2", "true");
			pro.put("mail.smtp.ssl.trust", "*");

			Session session = Session.getInstance(pro, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("sistemawms568@gmail.com", "viqsabpfmfajoldq");
				}
			});

			session.setDebug(true);

			try {
				String htmlMessage = "<html>"
                        + "Hello,<br><br>"
                        + "This one test for send email with image,<br>"
                        + "thanks for cooperate.<br><br>"
                        + "http://newtours.demoaut.com" 
                        + "</html>";
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("sistemawms568@gmail.com")); // Remetente
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // Destinatário(s)
				message.setSubject("Alteração de senha ");// Assunto
				message.setText("Olá, use esse código para redefinir sua senha: ");
				message.setContent(htmlMessage, "text/html; charset=utf-8"	);

				/** Método para enviar a mensagem criada */
				Transport.send(message);

				System.out.println("Feito!!!");
				return ResponseEntity.ok().build();
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		} else {
			return ResponseEntity.badRequest().build();
		}

	}

}
