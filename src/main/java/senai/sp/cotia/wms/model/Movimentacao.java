package senai.sp.cotia.wms.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToOne;


import lombok.Data;
import senai.sp.cotia.wms.type.Tipo;
@Data
@Entity
public class Movimentacao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String data;
	@Enumerated(EnumType.STRING)
	private Tipo tipo;
	@OneToOne
	private Produto produto;
	
}
