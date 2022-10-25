package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import lombok.experimental.PackagePrivate;
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
	
	@Query("SELECT m FROM Movimentacao m WHERE m.produto.sku = :a AND m.data BETWEEN :c AND :e")
	public List<Movimentacao> dataProduto(@Param("a") String produto,@Param("c") String dateStart,@Param("e") String dateEnd);
 
	@Query("SELECT m FROM Movimentacao m WHERE m.produto = :p")
	public List<Movimentacao> findByTipoEntrada(@Param("p") Long produto);
	
	@Query("SELECT m FROM Movimentacao m WHERE m.tipo = 'SAIDA' AND m.produto = :p")
	public List<Movimentacao> findByTipoSaida(@Param("p") Long produto);
	
	
}
