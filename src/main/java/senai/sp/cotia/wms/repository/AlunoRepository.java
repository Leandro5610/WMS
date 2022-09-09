package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.Aluno;

public interface AlunoRepository extends PagingAndSortingRepository<Aluno, Long> {
	
	@Query("SELECT alu FROM Aluno alu WHERE alu.nome LIKE %:p% OR alu.codMatricula LIKE %:p%")
    public List<Aluno> procurarTudo(@Param("p") String param);
	
	public List<Aluno> findAll();
	
}
