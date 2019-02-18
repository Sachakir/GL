package sacha.kir.bdd.remboursementsnote;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RemboursementsNoteRepository  extends CrudRepository<RemboursementsNote, Long>{
	@Query("SELECT demande_id FROM RemboursementsNote WHERE note_id = :note_id")
	public List<Long> findAllByNoteId(long note_id);
}
