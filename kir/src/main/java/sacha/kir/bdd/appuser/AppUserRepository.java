package sacha.kir.bdd.appuser;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long>{
	@Transactional
	@Modifying
	@Query("DELETE FROM AppUser WHERE user_id = :userid")
	public void deleteAppUser(@Param("userid") long uid);
}
