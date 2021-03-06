package root.bdd.conges;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CongesRepository extends CrudRepository<Conges, Long>{
	
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
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Conges WHERE UID = :userid")
	public void deleteConges(@Param("userid") long uid);
	
	@Transactional
	@Modifying
	@Query("UPDATE Conges SET date_debut = :newdebut, date_fin = :newfin WHERE conges_id = :congesid")
	public void updateConges(@Param("congesid") long id, @Param("newdebut") String newdebut, @Param("newfin") String newfin);
	
	@Query("SELECT validation_chef_service FROM Conges WHERE conges_id = :congesid")
	public String getChefState(@Param("congesid") long id);
	
	@Query("SELECT validation_rh FROM Conges WHERE conges_id = :congesid")
	public String getRHState(@Param("congesid") long id);
	
	@Query("SELECT p FROM Conges p WHERE UID = :uid")
	public List<Conges> getAllCongesByUid(long uid);
	
	@Query("SELECT p FROM Conges p WHERE p.conges_id = :congesid")
	public Conges getCongesbyId(@Param("congesid") long congesid);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Conges WHERE CongesID = :userid")
	public void deleteCongesbyCongesID(@Param("userid") long congesId);
	
	@Transactional
	@Modifying
	@Query("UPDATE Conges SET motif_refus = :m WHERE conges_id = :congesid")
	public void updateMotifRefus(@Param("congesid") long id,@Param("m") String m);
	
}
