package senai.sp.cotia.wms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.Aluno;

public interface AlunoRepository extends PagingAndSortingRepository<Aluno, Long> {
	
	@Query("SELECT alu FROM Aluno alu WHERE alu.nome LIKE %:p% OR alu.codMatricula LIKE %:p% OR alu.email LIKE %:p%")
    public List<Aluno> procurarTudo(@Param("p") String param);
	
	//public List<Aluno> findAll();
	
	//	public Optional<Aluno> searchEmail(@Param("email") String email);
	
	//	public Aluno findByCodMatriculaAndSenha(String matricula);
	
	@Query("SELECT aluno FROM Aluno aluno WHERE aluno.turma = idTurma ")
	public List<Aluno> pegarTurma(@Param("idTurma") Long id);
}
