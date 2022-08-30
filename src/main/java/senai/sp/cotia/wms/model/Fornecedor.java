
package senai.sp.cotia.wms.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
	@OneToMany
	private List<ItemFornecedor> listItem;
	
}
