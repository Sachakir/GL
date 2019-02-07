package sacha.kir.bdd.approle;

import java.util.List;

public interface InterfaceAppRoleService {
	public List<AppRole> findAll();
	public void addAppRole();
	public AppRole findById(long id);
	public AppRole findByRole(String role);
}
