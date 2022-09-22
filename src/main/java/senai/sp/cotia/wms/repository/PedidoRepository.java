package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.Pedido;

public interface PedidoRepository extends PagingAndSortingRepository<Pedido, Long>{
	
	@Query("SELECT ped FROM Pedido ped WHERE ped.valor LIKE %:p% OR ped.dataPedido LIKE %:p% "
			+ " OR ped.aluno LIKE %:p% ")
   public List<Pedido> procurarTudo(@Param("p") String param);
	
	@Query("SELECT pu FROM Pedido pu WHERE pu.aluno = idToken")
	public List<Pedido> procurarPedidoPeloUsuario(@Param("pu") String pedidoUser, @Param("idToken") Long id);
}
