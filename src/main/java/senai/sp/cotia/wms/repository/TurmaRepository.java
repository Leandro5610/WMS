package senai.sp.cotia.wms.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Fornecedor;
import senai.sp.cotia.wms.model.Professor;
import senai.sp.cotia.wms.model.Turma;

public interface TurmaRepository extends PagingAndSortingRepository<Turma, Long> {
	
	@Query("SELECT turma FROM Turma turma WHERE turma.nome LIKE %:p% OR turma.dataInicio LIKE %:p% "
			+ " OR turma.dataFinal LIKE %:p% OR turma.periodo LIKE %:p%")
	public List<Turma> procurarTudo(@Param("p") String param);

	
	/*@Query("SELECT turma from Turma turma WHERE :t BETWEEN turma.dataInicio and turma.dataFinal")
	public Turma between(@Param("t") Calendar dataInicio);*/
	
	@Query("SELECT t FROM Turma t WHERE t.prof = :p")
	public List<Turma> procurarTurmaPeloProf(@Param("p") Professor idProf);
}
