package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.Professor;

public interface ProfessorRepository extends PagingAndSortingRepository<Professor, Long> {
	
	@Query("SELECT prof FROM Professor prof WHERE prof.nome LIKE %:p% OR prof.nif LIKE %:p% OR prof.email LIKE %:p%")
	public List<Professor> procurarTudo(@Param("p") String param);
	
	public Professor findByNifAndSenha(String nif, String senha);
	
	public List<Professor> findAll();
}
