package sacha.kir.bdd.utilisateur;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService implements InterfaceUtilisateurService 
{
	@Autowired
    private UtilisateurRepository repository;
	
	@Override
	public List<Utilisateur> findAll() 
	{
		List<Utilisateur> users = (List<Utilisateur>) repository.findAll();
		return users;
	}

	@Override
	public void addUser(Utilisateur u)
	{
		repository.save(u);
	}
	
	@Override
	public String findbyname(String name) {
		
		return repository.find(name).get(0).getPrenom();
	}

	@Override
	public Utilisateur findPrenomNom(String nom, String Prenom) {
		return repository.trouverParPrenomNom(nom, Prenom);
	}
	
	@Override
	public int getMaxId()
	{
		return repository.getMaxId();
	}
}
