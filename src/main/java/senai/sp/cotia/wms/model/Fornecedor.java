package senai.sp.cotia.wms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



import lombok.Data;
@Data
@Entity
public class Fornecedor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(unique = true)
	private String cnpj;
	private String cep;
	private String logradouro;
	private String localidade;
	private String uf;	
	private boolean homologado;
	
	
	/*public void setNome(String nome) {
		BasicTextEncryptor textEncriptor = new BasicTextEncryptor();
		textEncriptor.setPasswordCharArray("chaves".toCharArray());
		String nomeCrip = textEncriptor.encrypt(nome);
		 this.nome = nomeCrip;
	}
	
	/*public void setCnpj(String cnpj) {
		BasicTextEncryptor textEncriptor = new BasicTextEncryptor();
		textEncriptor.setPasswordCharArray("chaves".toCharArray());
		
		String cnpjCrip = textEncriptor.encrypt(cnpj);
		 this.cnpj = cnpjCrip;
	}

	/*public String getCnpj() {
		BasicTextEncryptor textDecriptor = new BasicTextEncryptor();
		textDecriptor.setPasswordCharArray("chaves".toCharArray());
		
		String CnpjDecrip = textDecriptor.decrypt(cnpj);
		return CnpjDecrip;
	}
	
	public String getNome() {
		BasicTextEncryptor textDecriptor = new BasicTextEncryptor();
		textDecriptor.setPasswordCharArray("chaves".toCharArray());
		
		String nomeDecrip = textDecriptor.decrypt(nome);
		return nomeDecrip;
	}*/
	
	
	
	
}
