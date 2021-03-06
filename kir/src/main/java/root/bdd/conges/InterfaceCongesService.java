package root.bdd.conges;
import java.util.List;

import org.springframework.data.repository.query.Param;

public interface InterfaceCongesService 
{
	public List<Conges> findAll();
	public void addConges(String date_debut, String date_fin, long uID, boolean rtt);
	public Conges findByCongesId(long congesid);
	public List<Conges> findAllByIds(List<Long> uids);
	public void updateChefState(long congesid, String newstate);
	public void updateRHState(long congesid, String newstate);
	public void deleteConges(long uid);
	public void updateConges(long congesid, String newdebut, String newfin);
	public String getChefState(long congesid);
	public String getRHState(long congesid);
	public Conges getCongesbyId(long congesid);
	public void deleteCongesbyCongesID(long congesId);
	public void updateMotifRefus(long id, String m);

}
