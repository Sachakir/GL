package sacha.kir.bdd.userrole;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long>{
	@Query("SELECT MAX(id) FROM UserRole")
	public int getMaxId();
	
	@Query("SELECT p FROM UserRole p WHERE p.user_id = :id")
	public UserRole findById(@Param("id") long id);

	@Transactional
	@Modifying
	@Query("UPDATE UserRole SET role_id = :newroleid WHERE user_id = :userid")
	public void updateRole(@Param("userid") long id,@Param("newroleid") long newroleid);
}
