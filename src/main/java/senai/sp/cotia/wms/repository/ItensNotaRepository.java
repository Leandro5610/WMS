package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.ItemNota;


public interface ItensNotaRepository extends PagingAndSortingRepository<ItemNota, Long> {
	
	public ItemNota findByPedidoAndNotaFiscal(String pedido, String notaFiscal);
	
	//procura um item da nota no banco de dados por qualquer atributo
	@Query("SELECT i FROM ItemNota i WHERE i.pedido LIKE %:p% OR i.notaFiscal LIKE %:p% ")
	public List<ItemNota> procurarItemNota(@Param("p") String param);
	
	//metodo para procurar itens no banco de dados por qualquer atributo
	@Query("SELECT itens FROM ItensNota itens WHERE itens.pedido LIKE %:p% OR itens.notaFiscal LIKE %:p% " + "OR itens.quantidade LIKE %:p% ")
	public List<ItemNota> procurarTudo(@Param("p") String param);

}
