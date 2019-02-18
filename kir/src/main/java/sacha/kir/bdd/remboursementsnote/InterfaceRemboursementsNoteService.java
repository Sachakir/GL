package sacha.kir.bdd.remboursementsnote;

import java.util.List;

public interface InterfaceRemboursementsNoteService {
	public List<RemboursementsNote> findAll();
	public RemboursementsNote addRemboursementToNote(long note_id, long remboursement_id);
	public List<Long> findAllByNoteId(long note_id);
}
