package root.bdd.membresmission;

import java.util.List;

public interface InterfaceMembresMissionService {
	public List<MembresMission> findAll();
	public void addMembresMission(MembresMission membreMission);
	public List<Long> findMissionsByUID (Long userId);
	public void deleteMembresMission(long uid);
	public void deleteMembresByMissionId(long mission_id);
	public List<Long> findMembersByMissionId(long mission_id);
}
