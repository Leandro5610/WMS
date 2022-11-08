package senai.sp.cotia.wms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import senai.sp.cotia.wms.repository.EmailRepository;


@Service
public class EmailService {

	@Autowired
	EmailRepository emailRepository;

	@Autowired
	private JavaMailSender emailSender;

	/*
	 * public EmailModel sendEmail(EmailModel emailModel) {
	 * emailModel.setSendDateEmail(LocalDateTime.now()); try{ SimpleMailMessage
	 * message = new SimpleMailMessage();
	 * message.setFrom(emailModel.getEmailFrom());
	 * message.setTo(emailModel.getEmailTo());
	 * message.setSubject(emailModel.getSubject());
	 * message.setText(emailModel.getText()); emailSender.send(message);
	 * 
	 * 
	 * 
	 * emailModel.setStatusEmail(StatusEmail.SENT); } catch (MailException e){
	 * e.printStackTrace(); emailModel.setStatusEmail(StatusEmail.ERROR); } return
	 * emailRepository.save(emailModel); }
	 */

	public void senEmail() {
	
	}

}