package senai.sp.cotia.wms.model;


import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class NotaFiscal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigoNota;
	private Double valorTotal;
	private String dataEmissao;
	@OneToOne
	private Pedido pedido;
	@OneToMany(mappedBy = "notaFiscal", cascade = CascadeType.ALL)
	private List<ItemNota> itens;
}
