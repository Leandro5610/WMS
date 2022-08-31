package senai.sp.cotia.wms.type;

public enum Periodo {
		MANHA("manha"),
		TARDE("tarde"),
		NOITE("noite");

		String periodo;
		
		private Periodo(String periodo) {
			this.periodo = periodo;
		}

		public String getPeriodo() {
			return periodo;
		}

		public void setPeriodo(String periodo) {
			this.periodo = periodo;
		}

		
		
}
