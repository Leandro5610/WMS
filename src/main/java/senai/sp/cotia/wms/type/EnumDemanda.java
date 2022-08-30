package senai.sp.cotia.wms.type;

public enum EnumDemanda {
	BAIXA("baixa"),
	MEDIA("media"),
	ALTA("alta");
	
	String demanda;
	
	private EnumDemanda(String demanda) {
		this.demanda = demanda;
	}

	public String getDemanda() {
		return demanda;
	}

	public void setDemanda(String demanda) {
		this.demanda = demanda;
	}
	
}
