package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.ItemPedido;


public interface ItemPedidoRepository extends PagingAndSortingRepository<ItemPedido, Long> {
	
	public ItemPedido findByProdutoAndPedido(String produto, String pedido);
	
	//procura um item do pedido no banco de dados por qualquer atributo
	@Query("SELECT i FROM ItemPedido i WHERE i.produto LIKE %:p% OR i.pedido LIKE %:p% ")
	public List<ItemPedido> procurarItemPedido(@Param("p") String param);
	
	//metodo para procurar itens no banco de dados por qualquer atributo
	@Query("SELECT itens FROM ItemPedido itens WHERE itens.pedido LIKE %:p% OR itens.produto LIKE %:p% " + "OR itens.quantidade LIKE %:p% ")
	public List<ItemPedido> procurarTudo(@Param("p") String param);


}
