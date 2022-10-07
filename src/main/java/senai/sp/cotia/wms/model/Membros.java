package senai.sp.cotia.wms.model;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
@Data
@Entity
public class Membros {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	
	private Aluno aluno;
	@ManyToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private Turma turma;
	@OneToOne
	private Professor professor;
	
}
