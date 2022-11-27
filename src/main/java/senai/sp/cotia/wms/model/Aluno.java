package senai.sp.cotia.wms.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import org.jasypt.util.text.BasicTextEncryptor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import senai.sp.cotia.wms.util.HashUtil;

@Entity
@Data
public class Aluno {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String nome;
	@Column(unique = true)
	private String codMatricula;
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotEmpty
	private String senha;
	@ManyToOne(cascade = CascadeType.ALL)
	private Turma turma;
	@Column(columnDefinition = "LONGTEXT")
	private String imagem;
	@Column(unique = true)
	private String email;
<<<<<<< HEAD

	/*
	 * public void setNome(String nome) { BasicTextEncryptor textEncriptor = new
	 * BasicTextEncryptor();
	 * textEncriptor.setPasswordCharArray("chaves".toCharArray()); String nomeCrip =
	 * textEncriptor.encrypt(nome); this.nome = nomeCrip; }
	 */

	        public void setCodMatricula(String codMatricula) {
		BasicTextEncryptor textEncriptor = new BasicTextEncryptor();
		textEncriptor.setPasswordCharArray("chaves".toCharArray());
		String codMatriculaCrip = textEncriptor.encrypt(codMatricula);
		this.codMatricula = codMatriculaCrip;
	}

	/*
	 * public String getNome() { BasicTextEncryptor textDecriptor = new
	 * BasicTextEncryptor();
	 * textDecriptor.setPasswordCharArray("chaves".toCharArray()); String nomeDecrip
	 * = textDecriptor.decrypt(nome); return nomeDecrip; }
	 */

	public String getCodMatricula() {
		BasicTextEncryptor textDecriptor = new BasicTextEncryptor();
		textDecriptor.setPasswordCharArray("chaves".toCharArray());
		String codMatriculaDecrip = textDecriptor.decrypt(codMatricula);
		return codMatriculaDecrip;
	}
=======
	private int codigo;
	
	 /* public void setNome(String nome) { BasicTextEncryptor textEncriptor = new
	  BasicTextEncryptor();
	  textEncriptor.setPasswordCharArray("chaves".toCharArray()); String nomeCrip =
	  textEncriptor.encrypt(nome); this.nome = nomeCrip; }*/
	  
	  public void setCodMatricula(String codMatricula) { BasicTextEncryptor
	  textEncriptor = new BasicTextEncryptor();
	  textEncriptor.setPasswordCharArray("chaves".toCharArray());
	  String codMatriculaCrip = textEncriptor.encrypt(codMatricula);
	  this.codMatricula = codMatriculaCrip; }
	  
	 /* public String getNome() { BasicTextEncryptor textDecriptor = new
	  BasicTextEncryptor();
	  textDecriptor.setPasswordCharArray("chaves".toCharArray());
	  String nomeDecrip = textDecriptor.decrypt(nome); return nomeDecrip; }*/
	  
	  public String getCodMatricula() { BasicTextEncryptor textDecriptor = new
	  BasicTextEncryptor();
	  textDecriptor.setPasswordCharArray("chaves".toCharArray());
	  String codMatriculaDecrip = textDecriptor.decrypt(codMatricula); return
	  codMatriculaDecrip; }
	 
>>>>>>> 9458992ac7d41879f284f014372b9948e2c0dee4

	public void setSenha(String senha) {
		this.senha = HashUtil.hash256(senha);
	}

	public void setSenhaComHash(String hash) {
		// seta o hash na senha
		this.senha = hash;
	}

}
