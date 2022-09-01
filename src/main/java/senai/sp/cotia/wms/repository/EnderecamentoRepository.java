package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Enderecamento;

public interface EnderecamentoRepository extends PagingAndSortingRepository<Enderecamento, Long>{
	
	@Query("SELECT end FROM Enderecamento end WHERE end.corredor LIKE %:p% OR end.edificio LIKE %:p% OR end.andar LIKE %:p% OR end.modulo LIKE %:p%"
+"OR end.demanda LIKE %:p%")
    public List<Enderecamento> procurarTudo(@Param("p") String param);
}
