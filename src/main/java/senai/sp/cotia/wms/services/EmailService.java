package senai.sp.cotia.wms.services;

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
import senai.sp.cotia.wms.services.EmailService;

@RestController
@RequestMapping("api/email")
public class EmailService {

	
	@Autowired
	private AlunoRepository repository;
	
	//METODO PARA ENVIAR O EMAIL PARA RECUPERAR SENHA
	@RequestMapping(value = "enviar-email", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> sendingEmail(String email) {
		
		Aluno aluno = repository.findByEmails(email);
		//Verifica se o aluno existe a partir do email
		if (aluno.getEmail().equals(email)) {
			Properties pro = new Properties();
			//configuração para host do gmail
			pro.put("mail.smtp.host", "smtp.gmail.com");
			//configuração da autenticação do email
			pro.put("mail.smtp.auth", "true");
			//configuração da porta onde vai ser enviado o email
			pro.put("mail.smtp.port", "587");
			//configuração para 
			pro.put("mail.smtp.starttls.enable", "true");
			//configuração para protocolo ssl
			pro.put("mail.smtp.ssl.protocols=TLSv1.2", "true");
			//configuração para liberar todos 
			pro.put("mail.smtp.ssl.trust", "*");
			
			//cria uma sessão para autenticar o email com o email e senha do remetente
			Session session = Session.getInstance(pro, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("sistemawms568@gmail.com", "viqsabpfmfajoldq");
				}
			});
			session.setDebug(true);

			try {
				//mensagem que vai ser enviada para
				String htmlMessage = "<html>"
                        + "Olá,<br><br>"
                        + "Para alterar sua senha por favor acesse este link,<br>"
                        + "http://newtours.demoaut.com.<br><br>"
                        + "Obrigado por escolher o sistem WMS" 
                        + "</html>";
				Message message = new MimeMessage(session);
				//passando o remetente  do email
				message.setFrom(new InternetAddress("sistemawms568@gmail.com")); // Remetente
				//passando o destinatário do email
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // Destinatário(s)
				//passando o assunto do email
				message.setSubject("Alteração de senha ");// Assunto
				//passando o corpo do email
				message.setText("Olá, use esse código para redefinir sua senha: ");
				//passando o link para a recuperação de senha
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
