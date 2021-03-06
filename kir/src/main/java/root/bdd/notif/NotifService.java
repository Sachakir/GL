package root.bdd.notif;

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
		Notif n = new Notif(notif_id, uid, false, new Date(), titre, lien);
		repository.save(n);
		
		// On supprime les notifs en trop quand il y en a plus que 10 pour le meme utilisateur
		List<Notif> notifs =  repository.getAllByIdDesc(uid);
		if (notifs.size() > 20)
			for (int i=20; i<notifs.size(); ++i)
				repository.delete(notifs.get(i));
	}

	@Override
	public void updateNotif(long notifId, boolean estVue) {
		repository.updateNotif(notifId, estVue);
	}

}
