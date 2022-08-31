package senai.sp.cotia.wms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
@Data
@Entity
public class ItensPedido {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	private int quantidade;
	@OneToMany
	private Produto produto;
	@OneToOne
	private Pedido pedido; 	
}
     