package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.Fornecedor;

public interface FornecedorRepository extends PagingAndSortingRepository<Fornecedor, Long> {
	
	@Query("SELECT fornecedor FROM Fornecedor fornecedor WHERE fornecedor.nome LIKE %:p% OR fornecedor.cnpj LIKE %:p% "
			+ " OR fornecedor.cep LIKE %:p% OR fornecedor.logradouro LIKE %:p% OR fornecedor.localidade LIKE %:p%" 
			+" OR fornecedor.uf LIKE %:p% OR fornecedor.homologado LIKE %:p%" )
   public List<Fornecedor> procurarTudo(@Param("p") String param);
}
