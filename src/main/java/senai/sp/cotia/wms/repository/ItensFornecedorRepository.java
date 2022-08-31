package senai.sp.cotia.wms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import senai.sp.cotia.wms.model.Enderecamento;
import senai.sp.cotia.wms.model.ItemFornecedor;

public interface ItensFornecedorRepository extends PagingAndSortingRepository<ItemFornecedor, Long>{

}
