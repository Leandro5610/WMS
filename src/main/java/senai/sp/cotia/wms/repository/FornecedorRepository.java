package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.Fornecedor;

public interface FornecedorRepository extends PagingAndSortingRepository<Fornecedor, Long> {
//	@Query("SELECT for FROM Fornecedor for WHERE for.nome LIKE %:p% OR for.cnpj LIKE %:p% "
//			+ "OR for.cep LIKE %:p% OR for.logradouro LIKE %:p% OR for.localidade LIKE %:p%" 
//			+"OR for.uf LIKE %:p% OR for.hologado LIKE %:p%" )
//    public List<Fornecedor> procurarTudo(@Param("p") String param);
}
