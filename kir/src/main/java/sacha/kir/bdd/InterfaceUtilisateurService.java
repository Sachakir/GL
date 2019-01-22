package sacha.kir.bdd;
import java.util.List;

public interface InterfaceUtilisateurService 
{
	public List<Utilisateur> findAll();
	public void killBill();
	public String findbyname(String name);
}
