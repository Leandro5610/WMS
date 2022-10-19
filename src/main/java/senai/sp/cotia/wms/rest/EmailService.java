package senai.sp.cotia.wms.rest;


import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Pedido;
import senai.sp.cotia.wms.repository.AlunoRepository;


@Service
public class EmailService {
	
	@Autowired
	private AlunoRepository repositoryAluno;

		public static void mandarEmail(Aluno aluno) {
			Properties props = new Properties();
			/** Parâmetros de conexão com servidor Yahoo */
			/** Parâmetros de conexão com servidor Yahoo */
			props.put("mail.smtp.host", "smtp.mail.yahoo.com");
			// props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.ssl.protocols=TLSv1.2", "true");
			props.put("mail.smtp.ssl.trust", "*");

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {

					return new PasswordAuthentication("joao.silva1764321@yahoo.com", "wxjvsytyjezrwomj");
				}
			});

			session.getProperties().put("mail.smtp.starttls.enable", "true");
			/** Ativa Debug para sessão */
			session.setDebug(true);
			
			try {
				System.out.println("ENTREI NO TRY");
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("joao.silva1764321@yahoo.com")); // Remetente
				System.out.println("AQUIII");
				message.setSubject("JOAOOOOO");// Assunto
				message.setText("OIIII EMAAAAIIILL");

				/*List<Aluno> listUser = repositoryUser.findAll();

				System.out.println("LISTA DE USUARIOS:" + listUser);

				InternetAddress[] emails = new InternetAddress[listUser.size()];

				for (int i = 0; i < listUser.size(); i++) {

					emails[i] = new InternetAddress();

					emails[i].setAddress(listUser.get(i).getEmail());

				}

				message.setRecipients(Message.RecipientType.TO, emails); // Destinatário(s)
				
				/** Método para enviar a mensagem criada */
				Transport.send(message);


			} catch (MessagingException e) {
				System.out.println("ENTREI NO CATCH");
				throw new RuntimeException(e);

			}
		}
}    