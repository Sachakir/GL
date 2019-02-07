package sacha.kir.bdd.userrole;

import java.util.List;

public interface InterfaceUserRoleService {
	public List<UserRole> findAll();
	public void addUserRole(UserRole r);
	public int getMaxId();
	public UserRole findById(long id);
}
