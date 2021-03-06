package root.bdd.note;

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
	public Note addNote(String mois, long uid) {
		DateFormat df = new SimpleDateFormat("MM-yyyy");
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
	
	@Override
	public Note findNoteByMonthAndUID(String mois, long uid) {
		return repository.findNoteByMonthAndUID(mois, uid);
	}
	
	@Override
	public List<Note> findAllById(long uid){
		return repository.findAllById(uid);
	}
	
	@Override
	public Note findById(long note_id) {
		return repository.findById(note_id).orElse(null);
	}

	@Override
	public void deleteNoteUid(long uid) {
		repository.deleteNoteUid(uid);
	}

}
