package senai.sp.cotia.wms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import org.jasypt.util.text.BasicTextEncryptor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;
import senai.sp.cotia.wms.util.HashUtil;

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
	@NotEmpty
	private String senha;
	@Lob
	@Column(columnDefinition = "LONGTEXT")
	private String imagem;
	@Column(unique = true)
	private String email;
	private int codigo;

	/*
	 * public void setNome(String nome) { BasicTextEncryptor textEncriptor = new
	 * BasicTextEncryptor();
	 * textEncriptor.setPasswordCharArray("chaves".toCharArray()); String nomeCrip =
	 * textEncriptor.encrypt(nome); this.nome = nomeCrip; }
	 */

	/*public void setNif(String nif) {
		BasicTextEncryptor textEncriptor = new BasicTextEncryptor();
		textEncriptor.setPasswordCharArray("chaves".toCharArray());
		String nifCrip = textEncriptor.encrypt(nif);
		this.nif = nifCrip;
	}*/

	/*public String getNif() {
		BasicTextEncryptor textEncriptor = new BasicTextEncryptor();
		textEncriptor.setPasswordCharArray("chaves".toCharArray());
		String nifDescrip = textEncriptor.decrypt(nif);
		return nifDescrip;
	}*/

	/*
	 * public String getNome() { BasicTextEncryptor textEncriptor = new
	 * BasicTextEncryptor();
	 * textEncriptor.setPasswordCharArray("chaves".toCharArray()); String
	 * nomeDescrip = textEncriptor.decrypt(nome); return nomeDescrip; }
	 */

	public void setSenha(String senha) {
		this.senha = HashUtil.hash256(senha);
	}

	public void setSenhaComHash(String hash) {
		// seta o hash na senha
		this.senha = hash;
	}

}
