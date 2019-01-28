package sacha.kir.bdd.missionnote;

import java.util.List;

public interface InterfaceMissionsNoteService {
	public List<MissionsNote> findAll();
	public void addNote();
}
