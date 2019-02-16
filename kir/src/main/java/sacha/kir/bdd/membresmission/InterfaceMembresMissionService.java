package sacha.kir.bdd.membresmission;

import java.util.List;

public interface InterfaceMembresMissionService {
	public List<MembresMission> findAll();
	public void addMembresMission(MembresMission membreMission);
	public List<Long> findMissionsByUID (Long userId);
	public void deleteMembresMission(long uid);
}
