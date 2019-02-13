package sacha.kir.bdd.mission;

import java.util.List;

public interface InterfaceMissionService {
	public List<Mission> findAll();
	public void addMission();
	public Mission findMissionById(Long missionId);
	public List<Mission> findMissionsById(List<Long> missionIds);
}
