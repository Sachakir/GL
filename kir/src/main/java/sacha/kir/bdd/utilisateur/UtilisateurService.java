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
	public void killBill()
	{
		Utilisateur n = new Utilisateur();
		n.setDateNaissance("1996-12-31");
		n.setJoursCongesRestants(42);
		n.setNom("Saad");
		n.setNumeroTel("06");
		n.setPrenom("Saad");
		
		repository.save(n);
		
		Utilisateur n2 = new Utilisateur();
		n2.setDateNaissance("1996-12-31");
		n2.setJoursCongesRestants(42);
		n2.setNom("Saad");
		n2.setNumeroTel("06");
		n2.setPrenom("Saad2");
		
		
		
		repository.save(n2);

	}
	
	@Override
	public String findbyname(String name) {
		
		return repository.find(name).get(0).getPrenom();
	}

}
