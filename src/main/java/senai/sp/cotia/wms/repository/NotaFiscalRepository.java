package senai.sp.cotia.wms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import senai.sp.cotia.wms.model.NotaFiscal;

public interface NotaFiscalRepository extends PagingAndSortingRepository<NotaFiscal, Long>{
	
	/*@Query("SELECT nota FROM NotaFiscal nota WHERE nota.valorTotal LIKE %:p% OR nota.pedido LIKE %:p% "
			+ " OR nota.dataEmissao LIKE %:p%" )
   public List<NotaFiscal> procurarTudo(@Param("p") String param);*/
	
	
	

}	
