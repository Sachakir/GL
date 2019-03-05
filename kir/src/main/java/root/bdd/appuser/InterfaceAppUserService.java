package root.bdd.appuser;

import java.util.List;

public interface InterfaceAppUserService {
	public List<AppUser> findAll();
	public void addAppUser(AppUser a);
	public void deleteAppUser(long uid);
	public AppUser findByUid(long uid);
	public void updatePassword(long uid, String encrPassword);

}
