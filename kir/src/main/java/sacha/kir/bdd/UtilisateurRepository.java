package sacha.kir.bdd;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
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
