package sacha.kir.bdd.note;

import java.util.List;

public interface InterfaceNoteService {
	public List<Note> findAll();
	public void addNote();
	public Note addNote(String mois, Long uid);
	public Note findNoteByMonthAndUID(String mois, Long uid);
}
