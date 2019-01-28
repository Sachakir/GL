package sacha.kir.bdd.remboursement;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RemboursementRepository extends CrudRepository<Remboursement, Long>{
	
}
