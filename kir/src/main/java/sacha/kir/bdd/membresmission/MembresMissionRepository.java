package sacha.kir.bdd.membresmission;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MembresMissionRepository extends CrudRepository<MembresMission, Long>{
	@Query("SELECT p.mission_id FROM MembresMission p WHERE p.membre_uid = :userId")
	public List<Long> findMissionsByUID(@Param("userId") long userId);
}
