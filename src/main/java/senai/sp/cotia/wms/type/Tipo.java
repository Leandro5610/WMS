package senai.sp.cotia.wms.type;

public enum Tipo {
	ENTRADA("entrada"),
	SAIDA("saida");
	
	String tipo;
	
	private Tipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	
}
