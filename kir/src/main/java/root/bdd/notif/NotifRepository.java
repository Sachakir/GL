package root.bdd.notif;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifRepository extends CrudRepository<Notif, Long>{

	@Query("SELECT MAX(notif_id) FROM Notif")
	public int getMaxId();

	@Query("SELECT n FROM Notif n WHERE uid = :uid ORDER BY date DESC")
	public List<Notif> getAllByIdDesc(long uid);
	
	@Transactional
	@Modifying
	@Query("UPDATE Notif SET vue = :newroleid WHERE notif_id = :userid")
	public void updateNotif(@Param("userid") long notifId,@Param("newroleid") boolean estVue);
}
