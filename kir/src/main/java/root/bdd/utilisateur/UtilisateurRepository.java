package root.bdd.utilisateur;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long>
{
	
	@Query("SELECT p FROM Utilisateur p WHERE LOWER(p.Nom) = LOWER(:lastName)")
    public List<Utilisateur> find(@Param("lastName") String lastName);
	
	@Query("SELECT p FROM Utilisateur p WHERE LOWER(p.Nom) = LOWER(:nom) AND LOWER(p.Prenom) = LOWER(:prenom)")
	public Utilisateur trouverParPrenomNom(@Param("nom") String nom,@Param("prenom") String prenom);
	
	@Query("SELECT MAX(UID) FROM Utilisateur")
	public Long getMaxId();
	
	@Query("SELECT p FROM Utilisateur p WHERE p.UID = :id")
	public Utilisateur findById(@Param("id") long id);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Utilisateur WHERE UID = :userid")
	public void deleteUser(@Param("userid") long uid);
	
	@Transactional
	@Modifying
	@Query("UPDATE Utilisateur SET Jourscongesrestants = :newstate WHERE UID = :congesid")
	public void updateJoursConges(@Param("congesid") long uid,@Param("newstate") float joursRestantsConges);
	
	@Transactional
	@Modifying
	@Query("UPDATE Utilisateur SET rtt = :newstate WHERE UID = :congesid")
	public void updateJoursRtt(@Param("congesid") long uid,@Param("newstate") float joursRestantsRtt);
}	
