package senai.sp.cotia.wms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
@Data
@Entity
public class Professor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(unique = true)
	private String nif;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String senha;
	@OneToOne
	private Turma turma;
}
