package senai.sp.cotia.wms.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import senai.sp.cotia.wms.type.EnumDemanda;
@Data
@Entity
public class Produto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codProduto;
	@Column(unique = true)
	private String sku;
	private String nome;
	private Double valor;
	private String descricao;
	@OneToOne
	private UniMedida medida;
	private boolean produtoImportado;
	@Enumerated(EnumType.STRING)
	private EnumDemanda demanda;
	private Double ipi;
	@OneToMany
	private List<Fornecedor> fornecedores;	
}
