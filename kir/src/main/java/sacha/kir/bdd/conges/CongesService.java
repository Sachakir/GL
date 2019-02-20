package sacha.kir.bdd.conges;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CongesService implements InterfaceCongesService
{
	@Autowired
    private	CongesRepository repository;
	
	@Override
	public List<Conges> findAll() {
		List<Conges> users = (List<Conges>) repository.findAll();
		return users;
	}


	@Override
	public void addConges(String date_debut, String date_fin, long uID) {
		
		Conges c = new Conges();
		c.setCongesid(repository.count()==0 ? 1 : (long) repository.getMaxId()+1);
		c.setDatedebut(date_debut);
		c.setDatefin(date_fin);
		c.setUid(uID);
		c.setValidationchefdeservice("EnAttente");
		c.setValidationrh("EnAttente");
		repository.save(c);
	}

	@Override
	public Conges findByCongesId(long congesid) {
		return repository.findById(congesid).orElse(null);
	}
	
	@Override
	public List<Conges> findAllByIds(List<Long> uids){
		List<Conges> listeConges = new ArrayList<Conges>();
		for(long uid : uids) {
			listeConges.addAll(repository.getAllCongesByUid(uid));
		}
		
		return listeConges;
	}

	@Override
	public void updateChefState(long congesid, String newstate) {
		repository.updateChefState(congesid, newstate);
	}

	@Override
	public void updateRHState(long congesid, String newstate) {
		repository.updateRHState(congesid, newstate);
	}

	@Override
	public void deleteConges(long uid) {
		repository.deleteConges(uid);
	}

	@Override
	public void updateConges(long congesid, String newdebut, String newfin) {
		repository.updateConges(congesid, newdebut, newfin);
	}
	
	@Override
	public String getChefState(long congesid) {
		return repository.getChefState(congesid);
	}

}
