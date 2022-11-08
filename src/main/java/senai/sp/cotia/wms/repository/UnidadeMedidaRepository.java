package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.UnidadeMedida;

public interface UnidadeMedidaRepository extends PagingAndSortingRepository<UnidadeMedida, Long>{
	

	@Query("SELECT uni FROM UnidadeMedida uni WHERE uni.nome LIKE %:p% OR uni.sigla LIKE %:p%")
   public List<UnidadeMedida> procurarTudo(@Param("p") String param);
}	
