package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.ItemNota;
import senai.sp.cotia.wms.model.NotaFiscal;


public interface ItemNotaRepository extends PagingAndSortingRepository<ItemNota, Long> {
	
	public ItemNota findByPedidoAndNotaFiscal(String pedido, String notaFiscal);
	
	//procura um item da nota no banco de dados por qualquer atributo
	@Query("SELECT i FROM ItemNota i WHERE i.pedido LIKE %:p% OR i.notaFiscal LIKE %:p% ")
	public List<ItemNota> procurarItemNota(@Param("p") String param);
	
	//metodo para procurar itens no banco de dados por qualquer atributo
	@Query("SELECT itens FROM ItemNota itens WHERE itens.pedido LIKE %:p% OR itens.notaFiscal LIKE %:p% " + " OR itens.quantidade LIKE %:p% ")
	public List<ItemNota> procurarTudo(@Param("p") String param);
	
	@Query("SELECT nota.itens FROM NotaFiscal nota WHERE nota.codigoNota = 'codigo' ")
    public List<ItemNota> pegarNota(@Param("codigo") Long param);
   

}
