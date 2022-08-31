package senai.sp.cotia.wms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import senai.sp.cotia.wms.model.Aluno;

public interface AlunoRepository extends PagingAndSortingRepository<Aluno, Long> {
	
	
}
