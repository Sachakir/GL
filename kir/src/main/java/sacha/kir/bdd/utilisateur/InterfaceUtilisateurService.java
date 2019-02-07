package sacha.kir.bdd.utilisateur;
import java.util.List;

public interface InterfaceUtilisateurService 
{
	public List<Utilisateur> findAll();
	public void addUser(Utilisateur u);
	public String findbyname(String name);
	public Utilisateur findPrenomNom(String nom,String Prenom);
	public int getMaxId();
	public Utilisateur findById(long id);
}
