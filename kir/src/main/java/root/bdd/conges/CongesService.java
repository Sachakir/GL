package root.bdd.conges;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.bdd.membresservice.InterfaceMembresServiceBddService;
import root.bdd.membresservice.MembresServiceBdd;
import root.bdd.notif.InterfaceNotifService;
import root.bdd.remboursement.Statut;
import root.bdd.utilisateur.InterfaceUtilisateurService;
import root.bdd.utilisateur.Utilisateur;

@Service
public class CongesService implements InterfaceCongesService
{
	@Autowired
    private	CongesRepository repository;
	
	@Autowired
	InterfaceNotifService NotifService;
	
	@Autowired
	InterfaceMembresServiceBddService MembresServiceBddService;

	@Override
	public List<Conges> findAll() {
		List<Conges> users = (List<Conges>) repository.findAll();
		return users;
	}


	@Override
	public void addConges(String date_debut, String date_fin, long uID, boolean rtt) {
		
		Conges c = new Conges();
		c.setCongesid(repository.count()==0 ? 1 : (long) repository.getMaxId()+1);
		c.setDatedebut(date_debut);
		c.setDatefin(date_fin);
		c.setUid(uID);
		c.setValidationchefdeservice(Statut.enAttente.statut());
		c.setValidationrh(Statut.enAttente.statut());
		c.setRtt(rtt);
		System.out.println("rtt en entree: "+ rtt+ " rtt en sortie: " + c.isRtt());
		repository.save(c);
		String titre = "Demande de congés du " + date_debut.substring(0, 5) + " au " + date_fin.substring(0, 10);
		long service_id = MembresServiceBddService.findById(uID).getServiceId();
		// Envoi des notifications aux chefs du service du demandeur de congés
		List<MembresServiceBdd> interesses = MembresServiceBddService.getChefByServiceId(service_id);
		for (MembresServiceBdd membre : interesses)
			if (membre.getUid() != uID)
				NotifService.addNotif(membre.getUid(), titre, "/Calendrier");
	}

	@Override
	public Conges findByCongesId(long congesid) {
		return repository.findById(congesid).orElse(null);
	}
	
	@Override
	public List<Conges> findAllByIds(List<Long> uids){
		List<Conges> listeConges = new ArrayList<Conges>();
		for(long uid : uids) {
			listeConges.addAll(repository.getAllCongesByUid(uid));
		}
		
		return listeConges;
	}

	@Override
	public void updateChefState(long congesid, String newstate) {
		repository.updateChefState(congesid, newstate);
		Conges c = repository.getCongesbyId(congesid);
		String titre = "Validation service : congés du " + c.getDatedebut().substring(0, 5) + " au " + c.getDatefin().substring(0, 10);
		// Envoi de la notification au demandeur de congés
		NotifService.addNotif(c.getUid(), titre, "/GererConges");
		// Envoi des notifications aux chefs du service RH, sauf si le chef qui a validé est déjà chef RH
		titre = "Demande de congés du " + c.getDatedebut().substring(0, 5) + " au " + c.getDatefin().substring(0, 10);
		if (MembresServiceBddService.findById(c.getUid()).getServiceId() != 1) {
			List<MembresServiceBdd> interesses = MembresServiceBddService.getChefByServiceId(1);
			for (MembresServiceBdd membre : interesses)
				if (membre.getUid() != c.getUid())
					NotifService.addNotif(membre.getUid(), titre, "/Calendrier");
		}
	}

	@Override
	public void updateRHState(long congesid, String newstate) {
		repository.updateRHState(congesid, newstate);
		Conges c = repository.getCongesbyId(congesid);
		String titre = "Validation RH : congés du " + c.getDatedebut().substring(0, 5) + " au " + c.getDatefin().substring(0, 10);
		// Envoi de la notification au demandeur de congés
		NotifService.addNotif(c.getUid(), titre, "/GererConges");
	}

	@Override
	public void deleteConges(long uid) {
		repository.deleteConges(uid);
	}

	@Override
	public void updateConges(long congesid, String newdebut, String newfin) {
		repository.updateConges(congesid, newdebut, newfin);
		Conges c = repository.getCongesbyId(congesid);
		String titre = "Demande de congés du " + c.getDatedebut().substring(0, 5) + " au " + c.getDatefin().substring(0, 10);
		long service_id = MembresServiceBddService.findById(c.getUid()).getServiceId();
		// Envoi des notifications aux chefs du service du demandeur de congés
		List<MembresServiceBdd> interesses = MembresServiceBddService.getChefByServiceId(service_id);
		for (MembresServiceBdd membre : interesses)
			if (membre.getUid() != c.getUid())
				NotifService.addNotif(membre.getUid(), titre, "/Calendrier");
	}
	
	@Override
	public String getChefState(long congesid) {
		return repository.getChefState(congesid);
	}
	
	@Override
	public String getRHState(long congesid) {
		return repository.getRHState(congesid);
	}


	@Override
	public Conges getCongesbyId(long congesid) {
		return repository.getCongesbyId(congesid);
	}


	@Override
	public void deleteCongesbyCongesID(long congesId) {
		repository.deleteCongesbyCongesID(congesId);
	}

}
