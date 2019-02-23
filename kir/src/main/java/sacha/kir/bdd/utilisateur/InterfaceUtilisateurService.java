package sacha.kir.bdd.utilisateur;
import java.util.List;

public interface InterfaceUtilisateurService 
{
	public List<Utilisateur> findAll();
	public void addUser(Utilisateur u);
	public String findbyname(String name);
	public Utilisateur findPrenomNom(String nom,String Prenom);
	public Long getMaxId();
	public Utilisateur findById(long id);
	public void deleteUser(long uid);
	public void updateJoursConges(long uid, long joursRestantsConges);
	public void updateJoursRtt(long uid, long joursRestantsRtt);

}
