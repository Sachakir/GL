package sacha.kir.bdd.missionnote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MissionsNoteService implements InterfaceMissionsNoteService{

	@Autowired
	MissionsNoteRepository repository;
	
	@Override
	public List<MissionsNote> findAll() {
		List<MissionsNote> users = (List<MissionsNote>) repository.findAll();
		return users;
	}

	@Override
	public void addNote() {
		MissionsNote mn = new MissionsNote();
		mn.setMission_id((long) 0);
		mn.setNote_id((long) 0);
		
		repository.save(mn);
	}

}
