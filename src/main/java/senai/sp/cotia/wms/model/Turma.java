package senai.sp.cotia.wms.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import senai.sp.cotia.wms.type.EnumPeriodo;

@Entity
@Data
public class Turma {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nomeTurma;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Calendar dataInicio;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Calendar dataFim;
	@Enumerated(EnumType.STRING)
	private EnumPeriodo	periodo;
	@OneToMany
	private Aluno aluno;
}
