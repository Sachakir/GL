package sacha.kir.bdd.membresmission;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembresMissionRepository extends CrudRepository<MembresMission, Long>{

}
