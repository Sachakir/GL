package sacha.kir.bdd.justificatif;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JustificatifRepository extends CrudRepository<Justificatif, Long> {
	
	@Query("SELECT MAX(justificatif_id) FROM Justificatif")
	public int getMaxId();
	
}
