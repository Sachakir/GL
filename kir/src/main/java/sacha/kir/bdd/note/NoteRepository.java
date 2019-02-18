package sacha.kir.bdd.note;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long>{
	
	@Query("SELECT n FROM Note n WHERE n.mois LIKE :mois AND n.uid = :uid")
	public Note findNoteByMonthAndUID(String mois, Long uid);
	
	@Query("SELECT MAX(note_id) FROM Note")
	public int findMaxId();
	
	@Query("SELECT n FROM Note n WHERE n.uid = :uid")
	public List<Note> findAllById(long uid);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Note WHERE uid = :userid")
	public void deleteNoteUid(@Param("userid") long uid);
}