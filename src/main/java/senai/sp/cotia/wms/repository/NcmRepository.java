package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import senai.sp.cotia.wms.model.Ncm;

public interface NcmRepository extends PagingAndSortingRepository<Ncm, Long> {
	
		public Ncm findByNcm(String ncm);
	
		//metodo para procurar itens no banco de dados por qualquer atributo
		@Query("SELECT ncm FROM Ncm ncm WHERE ncm.ncm LIKE %:p%")
		public List<Ncm> procurarTudo(@Param("p") String param);
	 
}
