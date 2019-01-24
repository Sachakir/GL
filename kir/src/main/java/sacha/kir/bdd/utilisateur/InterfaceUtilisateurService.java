package sacha.kir.bdd.utilisateur;
import java.util.List;

public interface InterfaceUtilisateurService 
{
	public List<Utilisateur> findAll();
	public void killBill();
	public String findbyname(String name);
}
