package root.bdd.membresmission;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MembresMissionRepository extends CrudRepository<MembresMission, Long>{
	@Query("SELECT p.mission_id FROM MembresMission p WHERE p.membre_uid = :userId")
	public List<Long> findMissionsByUID(@Param("userId") long userId);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM MembresMission WHERE membre_uid = :userid")
	public void deleteMembresMission(@Param("userid") long uid);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM MembresMission WHERE mission_id = :mission_id")
	public void deleteMembresByMissionId(@Param("mission_id") long mission_id);
}
