package sacha.kir.bdd.conges;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CongesRepository extends CrudRepository<Conges, Long>{
	
	@Query("SELECT p FROM Conges p WHERE p.conges_id = :congesid")
	public Conges findByCongesId(@Param("congesid") long congesid);
	
	@Transactional
	@Modifying
	@Query("UPDATE Conges SET validation_chef_service = :newstate WHERE conges_id = :congesid")
	public void updateChefState(@Param("congesid") long id,@Param("newstate") String newstate);
	
	@Transactional
	@Modifying
	@Query("UPDATE Conges SET validation_rh = :newstate WHERE conges_id = :congesid")
	public void updateRHState(@Param("congesid") long id,@Param("newstate") String newstate);
	
	@Query("SELECT MAX(conges_id) FROM Conges")
	public int getMaxId();
}
