package sacha.kir.bdd.membresmission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class MembresMissionService implements InterfaceMembresMissionService{

	@Autowired
    private	MembresMissionRepository repository;
	
	@Override
	public List<MembresMission> findAll() {
		List<MembresMission> users = (List<MembresMission>) repository.findAll();
		return users;
	}

	@Override
	public void addMembresMission() {
		MembresMission m = new MembresMission();
		m.setMembre_uid((long) 1);
		m.setMission_id((long) 0);
		
		repository.save(m);
	}

}
