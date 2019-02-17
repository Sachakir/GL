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
	
	@Query("SELECT ap FROM AppUser ap WHERE ap.user_id = :userid")
	public AppUser findByUid(@Param("userid") long uid);
	
	@Transactional
	@Modifying
	@Query("UPDATE AppUser SET encrypted_password = :newstate WHERE user_id = :congesid")
	public void updatePassword(@Param("congesid") long uid,@Param("newstate") String encrPassword);
}
