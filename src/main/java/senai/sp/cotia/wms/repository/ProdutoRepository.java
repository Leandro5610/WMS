package senai.sp.cotia.wms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import senai.sp.cotia.wms.model.Produto;



public interface ProdutoRepository extends PagingAndSortingRepository<Produto, Long>{
	@Query("SELECT pro FROM Produto pro WHERE pro.sku LIKE %:p% OR pro.nome LIKE %:p% "
	+" OR pro.descricao LIKE %:p% "
	+" OR pro.medida.nome LIKE %:p%"
	+" OR pro.ncm.ncm LIKE %:p%"
	+" OR pro.demanda LIKE %:p% ")
	public List<Produto> procurarTudo(@Param("p") String param);
	
	/*@Query("SELECT pro FROM Produto pro WHERE pro.nome")
	public Produto pegarNome(@Param("p")Long id);*/
	
	
}
