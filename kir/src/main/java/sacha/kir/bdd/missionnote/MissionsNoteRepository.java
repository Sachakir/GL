package sacha.kir.bdd.missionnote;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionsNoteRepository  extends CrudRepository<MissionsNote, Long>{

}
