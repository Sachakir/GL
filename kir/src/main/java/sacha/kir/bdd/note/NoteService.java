package sacha.kir.bdd.note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		n.setMois("01/2019");
		n.setNote_id((long) 4);
		n.setUid((long) 30);
		
		repository.save(n);
	}
	
	public Note addNote(String mois, Long uid) {
		DateFormat df = new SimpleDateFormat("MM/yyyy");
		try {
			df.parse(mois);
			System.out.println("Parsing succes");
			long note_id = 1;
			if(repository.count() != 0)
			{
				note_id += repository.findMaxId();
			}
			
			Note n = new Note(note_id, mois, uid);
			repository.save(n);
			
			return n;
		}
		catch(ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Note findNoteByMonthAndUID(String mois, Long uid) {
		return repository.findNoteByMonthAndUID(mois, uid);
	}

}
