package senai.sp.cotia.wms.model;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;

import senai.sp.cotia.wms.type.Tipo;

public class Movimentacao {
	private Long id;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Calendar data;
	private Tipo tipo;
	private Produto produto;
}
