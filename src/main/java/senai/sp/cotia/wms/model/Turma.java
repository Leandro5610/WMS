package senai.sp.cotia.wms.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import senai.sp.cotia.wms.type.Periodo;

@Entity
@Data
public class Turma {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Calendar dataInicio;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Calendar dataFinal;
	@Enumerated(EnumType.STRING)
	private Periodo	periodo;
	@ManyToOne
	private Aluno aluno;

}
