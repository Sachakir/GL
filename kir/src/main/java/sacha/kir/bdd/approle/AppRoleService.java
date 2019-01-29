package sacha.kir.bdd.approle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppRoleService implements InterfaceAppRoleService{
	@Autowired
	AppRoleRepository repository;
	
	@Override
	public List<AppRole> findAll() {
		List<AppRole> users = (List<AppRole>) repository.findAll();
		return users;
	}

	@Override
	public void addAppRole() {
		AppRole a = new AppRole();
		a.setRole_id((long) 4);
		a.setRole_name("ChefFinances");
		
		repository.save(a);
	}

}
