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

}
