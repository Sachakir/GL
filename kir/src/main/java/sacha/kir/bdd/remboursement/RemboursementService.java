package sacha.kir.bdd.remboursement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sacha.kir.bdd.justificatif.JustificatifRepository;
import sacha.kir.bdd.note.InterfaceNoteService;
import sacha.kir.bdd.note.NoteService;
import sacha.kir.bdd.notif.InterfaceNotifService;
import sacha.kir.bdd.remboursementsnote.InterfaceRemboursementsNoteService;
import sacha.kir.bdd.remboursementsnote.RemboursementsNoteService;

@Service
public class RemboursementService implements InterfaceRemboursementService {
	
	@Autowired
	InterfaceNotifService NotifService;
    @Autowired
    InterfaceNoteService NoteService;
    @Autowired
    InterfaceRemboursementsNoteService RemboursementsNoteService;

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
		
		/*TODO findById() prend l'id de la Note et non pas l'id de la mission c'est pour ca que ca plante
		String mois = "02-2019";//NoteService.findById(r.getMission_id()).getMois();
		mois = mois.substring(0, 2) + "-" + mois.substring(3);
		NotifService.addNotif(30, "Demande : " + r.getTitre(),
				"/remboursements/note=" + mois + "/remboursement_id=" + demande_id);
		*/
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
	}

	@Override
	public void updateChefState(long demandeid, String newstate) {
		repository.updateChefState(demandeid, newstate);
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
}
