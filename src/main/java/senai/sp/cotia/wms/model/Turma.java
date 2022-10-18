package senai.sp.cotia.wms.model;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
	private int numParticipantes;
	@OneToOne
	private Professor prof;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String imagem;
	@OneToMany
	private List<Aluno> alunos;
	
	}
