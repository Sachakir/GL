package sacha.kir.bdd.membresmission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
	public void addMembresMission(MembresMission membresMission) {		
		repository.save(membresMission);
	}
	
	public List<Long> findMissionsByUID (Long userId)
	{
		List<Long> missions = (List<Long>) repository.findMissionsByUID(userId);
		return missions;
	}

	@Override
	public void deleteMembresMission(long uid) {
		repository.deleteMembresMission(uid);
	}
}
