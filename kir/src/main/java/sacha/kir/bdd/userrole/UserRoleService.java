package sacha.kir.bdd.userrole;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService implements InterfaceUserRoleService{
	
	@Autowired
	UserRoleRepository repository;
	
	@Override
	public List<UserRole> findAll() {
		List<UserRole> users = (List<UserRole>) repository.findAll();
		return users;
	}

	@Override
	public void addUserRole(UserRole r) {
		repository.save(r);
	}

	@Override
	public int getMaxId() {
		return repository.getMaxId();
	}

	@Override
	public UserRole findById(long id) {
		return repository.findById(id);
	}

	@Override
	public void updateRole(long id, long newroleid) {
		repository.updateRole(id, newroleid);
	}

	@Override
	public void deleteUserRole(long uid) {
		repository.deleteUserRole(uid);
	}
}
