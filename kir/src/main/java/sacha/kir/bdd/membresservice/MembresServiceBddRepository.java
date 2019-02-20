package sacha.kir.bdd.membresservice;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembresServiceBddRepository extends CrudRepository<MembresServiceBdd, Long>{
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
