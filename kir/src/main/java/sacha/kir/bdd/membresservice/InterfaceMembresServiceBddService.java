package sacha.kir.bdd.membresservice;

import java.util.List;
import java.util.Map;

public interface InterfaceMembresServiceBddService {
	public List<MembresServiceBdd> findAll();
	public MembresServiceBdd findById(long uid);
	public MembresServiceBdd getChefByServiceId(long service_id);
	public void updateRoleById(long uid, long role_id);
	public void updateServiceById(long uid, long service_id);
	public void updateAdminStatusById(long uid, boolean isAdmin);
	public MembresServiceBdd addMembreService (MembresServiceBdd membre);
	List<Long> getAllUidByServiceId(long service_id);
}
