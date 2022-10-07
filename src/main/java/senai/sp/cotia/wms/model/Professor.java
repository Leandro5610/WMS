package senai.sp.cotia.wms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.jasypt.util.text.BasicTextEncryptor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
@Data
@Entity
public class Professor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(unique = true)
	private String nif;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String senha;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String imagem;
	
	
	public void setNome(String nome) {
		BasicTextEncryptor textEncriptor = new BasicTextEncryptor();
		textEncriptor.setPasswordCharArray("chaves".toCharArray());
		
		String nomeCrip = textEncriptor.encrypt(nome);
		 this.nome = nomeCrip;
	}
	
	public void setNif(String nif) {
		BasicTextEncryptor textEncriptor = new BasicTextEncryptor();
		textEncriptor.setPasswordCharArray("chaves".toCharArray());
		
		String nifCrip = textEncriptor.encrypt(nif);
		 this.nif = nifCrip;
	}
	
	public String getNif() {
		BasicTextEncryptor textEncriptor = new BasicTextEncryptor();
		textEncriptor.setPasswordCharArray("chaves".toCharArray());
		
		String nifDescrip = textEncriptor.decrypt(nif);
		return nifDescrip;
	}
	public String getNome() {
		BasicTextEncryptor textEncriptor = new BasicTextEncryptor();
		textEncriptor.setPasswordCharArray("chaves".toCharArray());
		
		String nomeDescrip = textEncriptor.decrypt(nome);
		return nomeDescrip;
	}
}
