package sacha.kir.bdd.remboursementsnote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemboursementsNoteService implements InterfaceRemboursementsNoteService{

	@Autowired
	RemboursementsNoteRepository repository;
	
	@Override
	public List<RemboursementsNote> findAll() {
		List<RemboursementsNote> rembNotes = (List<RemboursementsNote>) repository.findAll();
		return rembNotes;
	}
	
	@Override
	public RemboursementsNote addRemboursementToNote(long note_id, long remboursement_id)
	{
		RemboursementsNote r = new RemboursementsNote(note_id, remboursement_id);
		repository.save(r);
		
		return r;
	}
	
	@Override
	public List<Long> findAllByNoteId(long note_id){
		return repository.findAllByNoteId(note_id);
	}

}
