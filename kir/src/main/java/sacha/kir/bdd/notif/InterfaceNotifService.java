package sacha.kir.bdd.notif;

import java.util.List;

public interface InterfaceNotifService {
	public List<Notif> findAll();
	public Notif findByNotifId(long notif_id);
	public List<Notif> getAllByIdDesc(long uid);
	public void addNotif(long uid, String titre, String lien);
	public void updateNotif(long notifId,boolean estVue);

}
