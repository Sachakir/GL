package root.bdd.services;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceBddRepository extends CrudRepository<ServiceBdd, Long>{
	@Query("SELECT service_id FROM ServiceBdd ORDER BY service_id ASC")
	public List<Long> listAllServiceIdAsc();
	
	@Query("SELECT service_id FROM ServiceBdd ORDER BY service_id DESC")
	public List<Long> listAllServiceIdDesc();
	
	@Query("SELECT MAX(service_id) FROM ServiceBdd")
	public Long getMaxId();
	
}
