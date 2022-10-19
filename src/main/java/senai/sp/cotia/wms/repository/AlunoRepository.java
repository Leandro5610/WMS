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
	
	public Aluno findByEmail(@Param("email") String email);
	
	public Aluno findByCodMatriculaAndSenha(String matricula, String senha);
	
	public Aluno findAlunoById(long id);
	
	public List<Aluno> findAll();
	
	
<<<<<<< HEAD
	public List<Aluno> findByTurmaId();
=======
	//public List<Aluno> findByTurma();
>>>>>>> eb9d08ae542db56f9304b6c17e2726bd014ad270
	
}
