package sacha.kir.bdd.userrole;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long>{
	@Query("SELECT MAX(id) FROM UserRole")
	public int getMaxId();
}
