package senai.sp.cotia.wms.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import senai.sp.cotia.wms.type.Demanda;
@Data
@Entity
public class Produto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codProduto;
	@Column(unique = true)
	private String sku;
	private String nome;
	private Double valorUnitario;
	private String descricao;
	@OneToOne
	private UnidadeMedida medida;
	private boolean importado;
	@Enumerated(EnumType.STRING)
	private Demanda demanda;
	private Double ipi;
	private Double pis;
	private Double cofins;
	private Double icms;
	@ManyToOne
	private Fornecedor fornecedores;
	@OneToOne
	private Ncm ncm;
	private int quantidade;
}
