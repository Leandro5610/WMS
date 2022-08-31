package senai.sp.cotia.wms.model;

import javax.persistence.OneToMany;

public class Estoque {
	private Long id;
	private String nome;
	private int capacidade;
	private int disponivel;
	@OneToMany
	private Enderecamento enderecamento;
	@OneToMany
	private Movimentacao movimentacao;

}
