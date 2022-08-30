package senai.sp.cotia.wms.type;

public enum EnumPeriodo {
		MANHA("manha"),
		TARDE("tarde"),
		NOITE("noite");

		String periodo;
		
		private EnumPeriodo(String periodo) {
			this.periodo = periodo;
		}

		public String getPeriodo() {
			return periodo;
		}

		public void setPeriodo(String periodo) {
			this.periodo = periodo;
		}

		
		
}
