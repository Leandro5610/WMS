package senai.sp.cotia.wms.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.aspectj.weaver.patterns.IfPointcut.IfFalsePointcut;
import org.jasypt.util.text.BasicTextEncryptor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import senai.sp.cotia.wms.type.Demanda;
@Entity
@Data
public class Enderecamento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String corredor;
	private String edificio;
	private String andar;
	private String modulo;
	@Enumerated(EnumType.STRING)
	private Demanda demanda;
	 
}
