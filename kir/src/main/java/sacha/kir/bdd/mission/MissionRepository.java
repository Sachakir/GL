package sacha.kir.bdd.mission;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends CrudRepository<Mission, Long> {
	@Query("SELECT MAX(mission_id) FROM Mission")
	public int getMaxMissionId();
}
