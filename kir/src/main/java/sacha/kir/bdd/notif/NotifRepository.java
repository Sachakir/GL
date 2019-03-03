package sacha.kir.bdd.notif;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifRepository extends CrudRepository<Notif, Long>{

	@Query("SELECT MAX(notif_id) FROM Notif")
	public int getMaxId();

	@Query("SELECT n FROM Notif n WHERE uid = :uid ORDER BY date DESC")
	public List<Notif> getAllByIdDesc(long uid);
	
	
}
