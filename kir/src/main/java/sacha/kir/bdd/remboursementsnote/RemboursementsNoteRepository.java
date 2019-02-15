package sacha.kir.bdd.remboursementsnote;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RemboursementsNoteRepository  extends CrudRepository<RemboursementsNote, Long>{

}
