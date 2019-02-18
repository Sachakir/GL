package sacha.kir.bdd.note;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long>{
	
	@Query("SELECT n FROM Note n WHERE n.mois LIKE :mois AND n.uid = :uid")
	public Note findNoteByMonthAndUID(String mois, Long uid);
	
	@Query("SELECT MAX(note_id) FROM Note")
	public int findMaxId();
	
	@Query("SELECT n FROM Note n WHERE n.uid = :uid")
	public List<Note> findAllById(long uid);
}