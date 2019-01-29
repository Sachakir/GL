package sacha.kir.bdd.appuser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sacha.kir.EncrytedPasswordUtils;

@Service
public class AppUserService implements InterfaceAppUserService {
	@Autowired AppUserRepository repository;
	
	@Override
	public List<AppUser> findAll() {
		List<AppUser> users = (List<AppUser>) repository.findAll();
		return users;
	}

	@Override
	public void addAppUser() {
		AppUser a = new AppUser();
		EncrytedPasswordUtils ep = new EncrytedPasswordUtils();
		
		a.setEncrypted_password(ep.encryptePassword("toto"));
		a.setUser_id((long) 4);
		a.setUser_name("Arthur");
		
		repository.save(a);
	}

}
