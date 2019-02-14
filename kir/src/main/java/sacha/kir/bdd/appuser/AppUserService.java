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
	public void addAppUser(AppUser a) {
		repository.save(a);
	}

	@Override
	public void deleteAppUser(long uid) {
		repository.deleteAppUser(uid);
	}

}
