package sacha.kir.bdd;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long>
{
	/*
	@Query("SELECT UID FROM users u WHERE UID = 9")
	Utilisateur findAllActiveUsers();*/
}	
