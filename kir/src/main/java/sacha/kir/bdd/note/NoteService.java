package sacha.kir.bdd.note;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService implements InterfaceNoteService {
	
	@Autowired
	NoteRepository repository;
	
	@Override
	public List<Note> findAll() {
		List<Note> users = (List<Note>) repository.findAll();
		return users;
	}

	@Override
	public void addNote()
	{
		Note n = new Note();
		n.setMois("Janvier");
		n.setNote_id((long) 4);
		n.setUid((long) 30);
		
		repository.save(n);
	}
	
	public Note findNoteByMonthAndUID(String mois, Long uid)
	{
		return repository.findNoteByMonthAndUID(mois, uid);
	}

}
