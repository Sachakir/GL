package sacha.kir.bdd.appuser;

import java.util.List;

public interface InterfaceAppUserService {
	public List<AppUser> findAll();
	public void addAppUser();
}
