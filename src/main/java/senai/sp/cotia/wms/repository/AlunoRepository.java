package senai.sp.cotia.wms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.Aluno;

public interface AlunoRepository extends PagingAndSortingRepository<Aluno, Long> {
	
	@Query("SELECT alu FROM Aluno alu WHERE alu.nome LIKE %:p% OR alu.email LIKE %:p%")
    public List<Aluno> procurarTudo(@Param("p") String param);
	
	//public List<Aluno> findAll();
	
	public List<Aluno> findByEmail(String email);
	
	public Aluno findByCodMatriculaAndSenha(String matricula, String senha);
	
	public Aluno findAlunoById(long id);
	
	public List<Aluno> findAll();
	
	public List<Aluno> findByTurmaId(long id);

	public Optional<Aluno> findByCodMatricula(String matricula);
	
	@Query("SELECT aluno FROM Aluno aluno WHERE aluno.email = :e ")
	public Aluno findByEmails(@Param("e") String email);
	
	public List<Aluno> findByCodigo(int codigo);
	
	public Aluno findByCodigoAndEmail(int codigo, String email);
}