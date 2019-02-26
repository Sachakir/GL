package sacha.kir.bdd.membresmission;

import java.util.ArrayList;
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
	
	@Override
	public void deleteMembresByMissionId(long mission_id) {
		repository.deleteMembresByMissionId(mission_id);
	}
	
	@Override
	public List<Long> findMembersByMissionId(long mission_id) {
		List<MembresMission> users = findAll();
		List<Long> mission_members = new ArrayList<Long>();
		for(MembresMission m : users) {
			if(m.getMission_id() == mission_id) {
				mission_members.add(m.getMembre_uid());
			}
		}
		
		return mission_members;
	}
}
