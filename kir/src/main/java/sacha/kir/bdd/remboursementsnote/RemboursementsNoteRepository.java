package sacha.kir.bdd.remboursementsnote;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RemboursementsNoteRepository  extends CrudRepository<RemboursementsNote, Long>{
	@Query("SELECT demande_id FROM RemboursementsNote WHERE note_id = :note_id")
	public List<Long> findAllByNoteId(long note_id);
	
	@Query("SELECT note_id FROM RemboursementsNote WHERE demande_id = :demande_id")
	public Long findNoteIdByDemandeId(long demande_id);
	
	@Query("SELECT r from RemboursementsNote r WHERE note_id = :note_id AND demande_id = :demande_id")
	public RemboursementsNote findByNoteIdAndDemandeId(long note_id, long demande_id);
}
