package sacha.kir.bdd.conges;
import java.util.List;

public interface InterfaceCongesService 
{
	public List<Conges> findAll();
	public void addConges(String date_debut, String date_fin, long uID);
	public Conges findByCongesId(long congesid);
	public List<Conges> findAllByIds(List<Long> uids);
	public void updateChefState(long congesid, String newstate);
	public void updateRHState(long congesid, String newstate);
	public void deleteConges(long uid);
	public void updateConges(long congesid, String newdebut, String newfin);
	public String getChefState(long congesid);
}
