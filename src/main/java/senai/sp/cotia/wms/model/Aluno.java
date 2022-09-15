package senai.sp.cotia.wms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.jasypt.util.text.BasicTextEncryptor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
//import senai.sp.cotia.wms.util.HashUtil;


@Entity
@Data
public class Aluno {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(unique = true)
	private String codMatricula;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String senha;
	@ManyToOne
	private Turma turma;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String imagem;
	
	public void setNome(String nome) {
		BasicTextEncryptor textEncriptor = new BasicTextEncryptor();
		textEncriptor.setPasswordCharArray("chaves".toCharArray());
		String nomeCrip = textEncriptor.encrypt(nome);
		 this.nome = nomeCrip;
	}
	
	public void setCodMatricula(String codMatricula) {
		BasicTextEncryptor textEncriptor = new BasicTextEncryptor();
		textEncriptor.setPasswordCharArray("chaves".toCharArray());
		
		String codMatriculaCrip = textEncriptor.encrypt(codMatricula);
		this.codMatricula = codMatriculaCrip ;
	}
	
	public String getNome() {
		BasicTextEncryptor textDecriptor = new BasicTextEncryptor();
		textDecriptor.setPasswordCharArray("chaves".toCharArray());
		
		String nomeDecrip = textDecriptor.decrypt(nome);
		return nomeDecrip;
	}
	public String getCodMatricula() {
		BasicTextEncryptor textDecriptor = new BasicTextEncryptor();
		textDecriptor.setPasswordCharArray("chaves".toCharArray());
		
		String codMatriculaDecrip = textDecriptor.decrypt(codMatricula);
		return codMatriculaDecrip;
	}
	
//	public void setSenha(String senha) {
//	    this.senha = HashUtil.hash256(senha);
//	 }
//		
//		public void setSenhaComHash(String hash) {
//	        // seta o hash na senha
//	        this.senha = hash;
//		}
	
	
	
	
	
	
	
	
	
}
