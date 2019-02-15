package sacha.kir.bdd.remboursement;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RemboursementRepository extends CrudRepository<Remboursement, Long>{
	
	@Query("SELECT MAX(demande_id) FROM Remboursement")
	public int getMaxId();
}
