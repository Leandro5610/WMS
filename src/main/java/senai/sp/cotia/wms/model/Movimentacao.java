package senai.sp.cotia.wms.model;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@ManyToOne
	private Pedido itens;
}
