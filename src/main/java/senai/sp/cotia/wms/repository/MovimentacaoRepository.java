package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.ItemNota;
import senai.sp.cotia.wms.model.Movimentacao;

public interface MovimentacaoRepository extends PagingAndSortingRepository<Movimentacao, Long> {
	
	//public Movimentacao findByData(String data);
	
	//procura uma movimentação no banco de dados por qualquer atributo
	@Query("SELECT m FROM Movimentacao m WHERE m.data LIKE %:p% "
				+ "OR m.tipo LIKE %:p%")
	public List<Movimentacao> procurarMovimentacao(@Param("p") String param);
	
	//metodo para procurar itens no banco de dados por qualquer atributo
	@Query("SELECT mov FROM Movimentacao mov WHERE mov.data LIKE %:p% OR mov.tipo LIKE %:p% " )
	public List<Movimentacao> procurarTudo(@Param("p") String param);
	
	public List<Movimentacao> findAll();
	
	@Query("SELECT data from Movimentacao mov WHERE mov.data = :p")
	public List<Movimentacao> procurarDatas(@Param("p") String param); 
 
}
