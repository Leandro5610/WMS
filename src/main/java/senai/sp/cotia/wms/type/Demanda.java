package senai.sp.cotia.wms.type;

public enum Demanda {
	BAIXA("baixa"),
	MEDIA("media"),
	ALTA("alta");
	
	String demanda;
	
	private Demanda(String demanda) {
		this.demanda = demanda;
	}

	public String getDemanda() {
		return demanda;
	}

	public void setDemanda(String demanda) {
		this.demanda = demanda;
	}
	
}
