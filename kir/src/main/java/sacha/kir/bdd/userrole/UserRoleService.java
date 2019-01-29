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
	public void addUserRole() {
		UserRole u = new UserRole();
		u.setId((long) 5);
		u.setRole_id((long) 4);
		u.setUser_id((long) 2);
		
		repository.save(u);
	}

}
