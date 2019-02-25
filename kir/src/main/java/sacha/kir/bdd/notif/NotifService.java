package sacha.kir.bdd.notif;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifService implements InterfaceNotifService {

	@Autowired
    private	NotifRepository repository;

	@Override
	public List<Notif> findAll() {
		List<Notif> notifs = (List<Notif>) repository.findAll();
		return notifs;
	}
	
	@Override
	public Notif findByNotifId(long notif_id) {
		return repository.findById(notif_id).orElse(null);
	}
	
	@Override
	public List<Notif> getAllByIdDesc(long uid) {
		return repository.getAllByIdDesc(uid);
	}

	@Override
	public void addNotif(long uid, String titre, String lien) {
		long notif_id = 1;
		if(repository.count() != 0) {
			notif_id += repository.getMaxId();
		}
		Notif n = new Notif(notif_id, uid, false, new Date(), titre, "https://google.fr");
		repository.save(n);
		
	}

}
