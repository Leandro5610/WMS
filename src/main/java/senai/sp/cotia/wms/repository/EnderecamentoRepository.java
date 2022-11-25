package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.Enderecamento;

public interface EnderecamentoRepository extends PagingAndSortingRepository<Enderecamento, Long>{
	
	@Query("SELECT endereco FROM Enderecamento endereco WHERE endereco.corredor LIKE %:e% OR endereco.edificio LIKE %:e%"
			+" OR endereco.andar LIKE %:e% OR endereco.modulo LIKE %:e% OR endereco.demanda LIKE %:e%"
			+" OR endereco.itens.nome LIKE %:e%")
    public List<Enderecamento> procurarTudo(@Param("e") String param);
}
