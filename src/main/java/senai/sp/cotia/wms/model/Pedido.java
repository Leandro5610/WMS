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

@Entity
@Data
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long numPedido;
	private Double valor;
	private String dataPedido;
	@OneToOne
	private Aluno aluno;
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private List<ItemPedido> itens;
	private int totalItens;
	
	public Double totalPedido(Pedido pedido) {
		double total = 0;
		for (ItemPedido itens : pedido.getItens()) {
			total = itens.getQuantidade();
			System.out.println("Total:"+total);
		}
		
		return total;
	}
	
}
