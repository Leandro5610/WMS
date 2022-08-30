package senai.sp.cotia.wms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
@Data
@Entity
public class ItensPedido {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long idItem;
	private Double valorItem;
	private int quantidade;
	@OneToMany
	private Produto produto;
	private Pedido pedido; 	
}
     