package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import senai.sp.cotia.wms.model.Professor;

public interface ProfessorRepository extends PagingAndSortingRepository<Professor, Long> {
	
	@Query("SELECT prof FROM Professor prof WHERE prof.nome LIKE %:p% OR prof.email LIKE %:p%")
	public List<Professor> procurarTudo(@Param("p") String param);
	
	public Professor findByNifAndSenha(String nif, String senha);
	
	public Professor findByEmail(String email);
	
	public Professor findProfessorById(long id);
	
	@Query("SELECT professor FROM Professor professor WHERE professor.email = :e ")
	public Professor findByEmails(@Param("e") String email);
	
	public Professor findByCodigoAndEmail(int codigo, String email);
	
	public List<Professor> findAll();
}
