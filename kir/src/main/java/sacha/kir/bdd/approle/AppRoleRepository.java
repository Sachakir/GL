package sacha.kir.bdd.approle;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRoleRepository extends CrudRepository<AppRole, Long>{
	@Query("SELECT p FROM AppRole p WHERE p.role_id = :id")
	public AppRole findById(@Param("id") long id);
	
	@Query("SELECT p FROM AppRole p WHERE p.role_name = :rolename")
	public AppRole findByRole(@Param("rolename") String rolename);
}
