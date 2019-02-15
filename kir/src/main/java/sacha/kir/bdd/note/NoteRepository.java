package sacha.kir.bdd.note;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long>{
	
	@Query("SELECT n FROM Note n WHERE n.mois = :mois AND n.uid = :uid")
	public Note findNoteByMonthAndUID(String mois, Long uid);
	
}