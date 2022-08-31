package senai.sp.cotia.wms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import senai.sp.cotia.wms.model.Pedido;

public interface PedidoRepository extends PagingAndSortingRepository<Pedido, Long>{

}
