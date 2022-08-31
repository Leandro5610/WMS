package senai.sp.cotia.wms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import senai.sp.cotia.wms.model.Produto;

public interface ProdutoRepository extends PagingAndSortingRepository<Produto, Long>{

}
