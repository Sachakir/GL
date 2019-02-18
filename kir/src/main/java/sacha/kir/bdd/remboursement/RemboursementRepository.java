package sacha.kir.bdd.remboursement;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RemboursementRepository extends CrudRepository<Remboursement, Long>{
	
	@Query("SELECT MAX(demande_id) FROM Remboursement")
	public int getMaxId();
	
	@Query("SELECT r FROM Remboursement r WHERE uid = :uid ORDER BY timestamp ASC")
	public List<Remboursement> getAllByIdAsc(long uid);
	
	@Query("SELECT r FROM Remboursement r WHERE uid = :uid ORDER BY timestamp DESC")
	public List<Remboursement> getAllByIdDesc(long uid);
	
	@Query("SELECT r FROM Remboursement r WHERE demande_id IN :demande_ids ORDER BY timestamp ASC")
	public List<Remboursement> getAllByIdListAsc(List<Long> demande_ids);
	
	@Query("SELECT r FROM Remboursement r WHERE demande_id IN :demande_ids ORDER BY timestamp DESC")
	public List<Remboursement> getAllByIdListDesc(List<Long> demande_ids);
	
	@Transactional
	@Modifying
	@Query("UPDATE Remboursement SET validationfinances = :newstate WHERE demande_id = :demandeid")
	public void updateRHState(@Param("demandeid") long demandeid,@Param("newstate") String newstate);
	
	@Transactional
	@Modifying
	@Query("UPDATE Remboursement SET validationchefservice = :newstate WHERE demande_id = :demandeid")
	public void updateChefState(@Param("demandeid") long demandeid,@Param("newstate") String newstate);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Remboursement WHERE uid = :userid")
	public void deleteRembUid(@Param("userid") long uid);
}
