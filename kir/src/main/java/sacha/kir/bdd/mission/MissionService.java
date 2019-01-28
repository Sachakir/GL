package sacha.kir.bdd.mission;

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
	public void addMission() 
	{
		Mission m = new Mission();
		m.setDate_debut("2019-01-23");
		m.setDate_fin("2019-01-24");
		m.setDescription("Une mission en France.");
		m.setMission_id((long)0);
		m.setResponsable_id((long)1);
		
		repository.save(m);
	}

}
