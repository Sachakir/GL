package sacha.kir.bdd.utilisateur;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long>
{
	
	@Query("SELECT p FROM Utilisateur p WHERE LOWER(p.Nom) = LOWER(:lastName)")
    public List<Utilisateur> find(@Param("lastName") String lastName);
}	
