package sacha.kir.bdd.membresservice;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembresServiceBddRepository extends CrudRepository<MembresServiceBdd, Long>{
	@Query("SELECT uid FROM MembresServiceBdd WHERE service_id = :service_id")
	public List<Long> getAllUidByServiceId(long service_id);
	
	//TODO Voir s'il y a un ou plusieurs chefs --> Plusieurs
	@Query("SELECT m FROM MembresServiceBdd m WHERE service_id = :service_id AND role_id = :role_id")
	public List<MembresServiceBdd> getByServiceIdAndRoleId(long service_id, long role_id);
	
	@Transactional
	@Modifying
	@Query("UPDATE MembresServiceBdd SET role_id = :role_id WHERE uid = :uid")
	public void updateRoleById(long uid, long role_id);
	
	@Transactional
	@Modifying
	@Query("UPDATE MembresServiceBdd SET service_id = :service_id WHERE uid = :uid")
	public void updateServiceById(long uid, long service_id);
	
	@Transactional
	@Modifying
	@Query("UPDATE MembresServiceBdd SET isAdmin = :isAdmin WHERE uid = :uid")
	public void updateAdminStatusById(long uid, boolean isAdmin);
}
