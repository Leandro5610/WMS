package senai.sp.cotia.wms.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.type.Tipo;

public interface MovimentacaoRepository extends PagingAndSortingRepository<Movimentacao, Long> {
	
	//public Movimentacao findByData(String data);
	
	//procura uma movimentação no banco de dados por qualquer atributo
	@Query("SELECT m FROM Movimentacao m WHERE m.data LIKE %:p% OR m.tipo LIKE %:p% OR m.produto.nome LIKE %:p% OR m.quantidade LIKE %:p%")
	public List<Movimentacao> procurarMovimentacao(@Param("p") String param);
	
	//metodo para procurar itens no banco de dados por qualquer atributo
	/*@Query("SELECT mov FROM Movimentacao mov WHERE mov.data LIKE %:p% OR mov.tipo LIKE %:p% OR mov.produto.nome LIKE %:p%")
	public List<Movimentacao> procurarTudo(@Param("p") String param);*/
	@Query("SELECT m FROM Movimentacao m WHERE tipo = :p")
	public List<Movimentacao>procurarPorTipo(@Param("p") Tipo param);
	
	@Query("SELECT m FROM Movimentacao m WHERE m.data = :d")
	public List<Movimentacao>procurarPorData(@Param("d") Date param);
	
	public List<Movimentacao> findAll();
	
	@Query("SELECT m FROM Movimentacao m WHERE m.produto.sku = :a AND m.data BETWEEN :c AND :e")
	public List<Movimentacao> dataProduto(@Param("a") String produto,@Param("c") Date dateStart,@Param("e") Date dateEnd);

	@Query("SELECT m FROM Movimentacao m WHERE m.produto = :p")
	public List<Movimentacao> saldo(@Param("p") Long produto);
	
	@Query("SELECT m FROM Movimentacao m WHERE m.data BETWEEN :s AND :e")
	public List<Movimentacao> buscarMovimentacoesPorData(@Param("s")Date dataStart, @Param("e")Date dataEnd);
	
	
}
