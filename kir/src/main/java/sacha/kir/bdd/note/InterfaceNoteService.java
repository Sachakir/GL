package sacha.kir.bdd.note;

import java.util.List;

public interface InterfaceNoteService {
	public List<Note> findAll();
	public void addNote();
	public Note addNote(String mois, long uid);
	public Note findNoteByMonthAndUID(String mois, long uid);
	public List<Note> findAllById(long uid);
	public Note findById(long note_id);
	public void deleteNoteUid(long uid);

}
