package sacha.kir.bdd.conges;
import java.util.List;

public interface InterfaceCongesService 
{
	public List<Conges> findAll();
	public void addConges(String date_debut, String date_fin, long uID);
	public Conges findByCongesId(long congesid);
	public void updateChefState(long congesid, String newstate);
	public void updateRHState(long congesid, String newstate);
	public void deleteConges(long uid);

}
