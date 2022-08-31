package senai.sp.cotia.wms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
@Data
@Entity
public class ItemPedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int quantidade;
	@ManyToOne
	private Produto produto;
	@ManyToOne
	private Pedido pedido; 	
	@ManyToOne
	private Enderecamento enderecamento;
	
}
     