package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.Fornecedor;
import senai.sp.cotia.wms.model.Produto;



public interface ProdutoRepository extends PagingAndSortingRepository<Produto, Long>{
	@Query("SELECT pro FROM Produto pro WHERE pro.sku LIKE %:p% OR pro.nome LIKE %:p% "
			+ " OR pro.valorUnitario LIKE %:p% OR pro.descricao LIKE %:p% OR pro.medida LIKE %:p%" 
			+" OR pro.importado LIKE %:p% OR pro.demanda LIKE %:p% OR pro.ipi LIKE %:p%"
			+" OR pro.pis LIKE %:p% OR pro.cofins LIKE %:p% OR pro.icms LIKE %:p%"
			+" OR pro.fornecedores LIKE %:p% OR pro.ncm LIKE %:p% OR pro.pontoPedido LIKE %:p%" )
   public List<Produto> procurarTudo(@Param("p") String param);

}
