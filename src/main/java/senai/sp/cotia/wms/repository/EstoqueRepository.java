package senai.sp.cotia.wms.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import senai.sp.cotia.wms.model.Estoque;

public interface EstoqueRepository extends PagingAndSortingRepository<Estoque, Long>{
	
	
	/*@Query("SELECT est FROM Estoque est WHERE est.nome LIKE %:p% OR est.capacidade LIKE %:p% "
			+ "OR est.disponivel LIKE %:p%")
    public List<Estoque> procurarTudo(@Param("p") String param);*/
}
