package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import senai.sp.cotia.wms.model.Membros;

public interface MembrosRepository extends PagingAndSortingRepository<Membros,Long>{
	
	@Query("SELECT turma.membros FROM Turma turma WHERE turma.id = :id")
    public List<Membros> pegarMembro(@Param("id") Long param);
}
