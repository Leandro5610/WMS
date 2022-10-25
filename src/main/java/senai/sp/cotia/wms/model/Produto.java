package senai.sp.cotia.wms.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
	private Double valorImportacao;
	@Enumerated(EnumType.STRING)
	private Demanda demanda;
	private Double ipi;
	private Double pis;
	private Double cofins;
	private Double icms;
	@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
	private List<ItemFornecedor> fornecedores;
	@OneToOne
	private Ncm ncm;
	private int pontoPedido;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String imagem;
	@JsonProperty(access = Access.WRITE_ONLY)
	@OneToMany
	private List<Movimentacao> movimentacoes;
	
	
}

