package senai.sp.cotia.wms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Estoque {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private int saldo;
	private int capacidade;
	private int disponivel;
	@ManyToOne
	private Enderecamento enderecamento;
	
	/*public int setSaldo(int item) {
		int saldo = item;
        return this.saldo;
    }*/
	
	/*public int setDiponivel(int totalProdutos) {
		return this.disponivel = capacidade - totalProdutos;
	}
	*/


}
