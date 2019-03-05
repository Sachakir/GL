package root.bdd.remboursement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.bdd.justificatif.JustificatifRepository;
import root.bdd.membresservice.InterfaceMembresServiceBddService;
import root.bdd.membresservice.MembresServiceBdd;
import root.bdd.note.InterfaceNoteService;
import root.bdd.notif.InterfaceNotifService;
import root.bdd.remboursementsnote.InterfaceRemboursementsNoteService;

@Service
public class RemboursementService implements InterfaceRemboursementService {
	
	@Autowired
	InterfaceNotifService NotifService;
    @Autowired
    InterfaceNoteService NoteService;
    @Autowired
    InterfaceRemboursementsNoteService RemboursementsNoteService;
	
	@Autowired
	InterfaceMembresServiceBddService MembresServiceBddService;
    
	@Autowired
	RemboursementRepository repository;
	
	@Autowired
	JustificatifRepository justificatifRepository;
	
	@Override
	public List<Remboursement> findAll() {
		List<Remboursement> users = (List<Remboursement>) repository.findAll();
		return users;
	}
	
	@Override
	public Remboursement findById(long demande_id) {
		return repository.findById(demande_id).orElse(null);
	}
	
	@Override
	public Remboursement addNewRemboursement(Remboursement r) {
		long demande_id = 1;
		if(repository.count() != 0) {
			demande_id += repository.getMaxId();
		}
		
		r.setDemande_id(demande_id);
		repository.save(r);
		String titre = "Demande : " + r.getTitre();
		long service_id = MembresServiceBddService.findById(r.getUid()).getServiceId();
		// Envoi des notifications aux chefs du service du demandeur de remboursement
		List<MembresServiceBdd> interesses = MembresServiceBddService.getChefByServiceId(service_id);
		for (MembresServiceBdd membre : interesses)
			if (membre.getUid() != r.getUid())
				NotifService.addNotif(membre.getUid(), titre, "/validationNDF");
		
		return r;
	}
	
	@Override
	public List<Remboursement> getAllByIdAsc(long uid) {
		return repository.getAllByIdAsc(uid);
	}
	
	@Override
	public List<Remboursement> getAllByIdDesc(long uid) {
		return repository.getAllByIdDesc(uid);
	}
	
	@Override
	public List<Remboursement> getAllByIdAsc(long uid, int limit) {
		List<Remboursement> l = repository.getAllByIdAsc(uid);
		if(limit > l.size())
			return l;
		else
			return l.subList(0, limit);
	}
	
	@Override
	public List<Remboursement> getAllByIdDesc(long uid, int limit) {
		List<Remboursement> l = repository.getAllByIdDesc(uid);
		if(limit > l.size())
			return l;
		else
			return l.subList(0, limit);
	}
	
	@Override
	public List<Remboursement> getAllByIdListAsc(List<Long> demande_ids) {
		return repository.getAllByIdListAsc(demande_ids);
	}
	
	@Override
	public List<Remboursement> getAllByIdListDesc(List<Long> demande_ids) {
		return repository.getAllByIdListDesc(demande_ids);
	}
 
	@Override
	public void updateFinancesState(long demandeid, String newstate) {
		repository.updateFinancesState(demandeid, newstate);
		
		Remboursement r = repository.findById(demandeid).orElse(null);
		String titre = (newstate.equals(Statut.valide.statut()) ? "Validation" : "Refus") + " finances : " + r.getTitre();
		NotifService.addNotif(r.getUid(), titre, r.genererLien());
	}

	@Override
	public void updateChefState(long demandeid, String newstate) {
		repository.updateChefState(demandeid, newstate);

		Remboursement r = repository.findById(demandeid).orElse(null);
		String titre = (newstate.equals(Statut.valide.statut()) ? "Validation" : "Refus") + " service : " + r.getTitre();
		// Envoie de la notification au demandeur de remboursement
		NotifService.addNotif(r.getUid(), titre, r.genererLien());
		
		// Envoi des notifications aux chefs du service finances , sauf si le chef qui a validé est déjà chef finances
		titre = "Demande : " + r.getTitre();
		if (MembresServiceBddService.findById(r.getUid()).getServiceId() != 2) {
			List<MembresServiceBdd> interesses = MembresServiceBddService.getChefByServiceId(2);
			for (MembresServiceBdd membre : interesses)
				if (membre.getUid() != r.getUid())
					NotifService.addNotif(membre.getUid(), titre, "/validationNDF");
		}
	}

	@Override
	public String getFinancesState(long demandeid) {
		return repository.getFinancesState(demandeid);
	}

	@Override
	public String getChefState(long demandeid) {
		return repository.getChefState(demandeid);		
	}

	@Override
	public void deleteRembUid(long uid) {
		repository.deleteRembUid(uid);
	}
	
	@Override
	public void deleteById(long demande_id) {
		Remboursement r = repository.findById(demande_id).orElse(null);
		if(r != null) {
			if(r.getJustificatifid() != null) {
				long justificatifId = r.getJustificatifid();
				justificatifRepository.deleteById(justificatifId);
			}
			repository.deleteById(demande_id);
		}
	}
	
	@Override
	public void update(Remboursement remb) {
		Remboursement r = repository.findById(remb.getDemande_id()).orElse(null);
		if(r != null) {
			repository.save(remb);
		}
		String titre = "Demande : " + r.getTitre();
		long service_id = MembresServiceBddService.findById(r.getUid()).getServiceId();
		// Envoi des notifications aux chefs du service du demandeur de remboursement
		List<MembresServiceBdd> interesses = MembresServiceBddService.getChefByServiceId(service_id);
		for (MembresServiceBdd membre : interesses)
			if (membre.getUid() != r.getUid())
				NotifService.addNotif(membre.getUid(), titre, "/validationNDF");
	}
}
