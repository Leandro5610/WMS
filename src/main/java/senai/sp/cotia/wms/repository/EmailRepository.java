package senai.sp.cotia.wms.repository;

 
import org.springframework.data.repository.PagingAndSortingRepository;

 

import senai.sp.cotia.wms.model.EmailModel;


public interface EmailRepository extends PagingAndSortingRepository<EmailModel, Long> {
	
}
 