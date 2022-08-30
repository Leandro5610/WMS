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
@Data
@Entity
public class NotaFiscal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigoNota;
	private Double valorTotal;
	@OneToOne
	private Pedido pedido;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Calendar dataEmissao;	
	@OneToMany
	private List<ItemNota> itens;
}
