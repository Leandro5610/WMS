package senai.sp.cotia.wms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import senai.sp.cotia.wms.model.Professor;

public interface ProfessorRepository extends PagingAndSortingRepository<Professor, Long> {

}
