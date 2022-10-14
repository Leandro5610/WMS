package senai.sp.cotia.wms.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import senai.sp.cotia.wms.model.HistoricoQrCode;

public interface HistoricoRepository extends PagingAndSortingRepository<HistoricoQrCode, Long> {

}
