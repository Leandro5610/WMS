package senai.sp.cotia.wms.model;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long numPedido;
	private Double valor;
	@JsonFormat(pattern = "yyyy-MM-dd 'T'HH:mm")
	private Calendar dataPedido;
	@OneToOne
	private Aluno aluno;
	@OneToMany(mappedBy = "pedido")
	private List<ItemPedido> itens;
	
}
