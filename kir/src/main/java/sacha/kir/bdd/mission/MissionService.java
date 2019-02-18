package sacha.kir.bdd.mission;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MissionService implements InterfaceMissionService{

	@Autowired
    private	MissionRepository repository;
	
	@Override
	public List<Mission> findAll() {
		List<Mission> users = (List<Mission>) repository.findAll();
		return users;
	}

	@Override
	public void addMission(Mission mission) 
	{
		repository.save(mission);
	}
	
	public Mission findMissionById(Long missionId)
	{
		return repository.findById(missionId).orElse(null);
	}
	
	public List<Mission> findMissionsById(List<Long> missionIds)
	{
		List<Mission> missions = new ArrayList<Mission>();
		for(Long missionId : missionIds)
		{
			Mission m = findMissionById(missionId);
			if(m != null) {
				missions.add(m);
			}
		}
		return missions;
	}

	@Override
	public int getMaxMissionId() {
		if (repository.count() == 0)
		{
			return 0;
		}
		else
		{
			return repository.getMaxMissionId();
		}
	}

}
