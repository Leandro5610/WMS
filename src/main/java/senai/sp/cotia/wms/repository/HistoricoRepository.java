package senai.sp.cotia.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import senai.sp.cotia.wms.model.HistoricoQrCode;

public interface HistoricoRepository extends PagingAndSortingRepository<HistoricoQrCode, Long> {
	
	
	public List<HistoricoQrCode>findByIdAluno(Long idAluno);
}
