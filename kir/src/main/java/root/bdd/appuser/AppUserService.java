package root.bdd.appuser;

import java.util.List;

import root.EncrytedPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Override
	public AppUser findByUid(long uid) {
		return repository.findByUid(uid);
	}

	@Override
	public void updatePassword(long uid, String encrPassword) {
		repository.updatePassword(uid, encrPassword);
	}

}
