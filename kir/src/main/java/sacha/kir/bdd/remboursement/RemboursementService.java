package sacha.kir.bdd.remboursement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemboursementService implements InterfaceRemboursementService {

	@Autowired
	RemboursementRepository repository;
	
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
	public void addRemboursement() {
		Remboursement r = new Remboursement();
		r.setDate("01/23/2019");
		r.setDemande_id((long) 1);
		r.setTitre("Demande type");
		r.setJustificatifid((long)1);
		r.setMontant((float) 1.5);
		r.setMotif("Parce que, oui");
		r.setUid((long)1);
		r.setValidationchefservice("enAttente");
		r.setValidationfinances("Valide");
		
		repository.save(r);
	}
	
	@Override
	public Remboursement addNewRemboursement(Remboursement r) {
		long demande_id = 1;
		if(repository.count() != 0) {
			demande_id += repository.getMaxId();
		}
		
		r.setDemande_id(demande_id);
		repository.save(r);
		
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
	public void updateRHState(long demandeid, String newstate) {
		repository.updateRHState(demandeid, newstate);
	}

	@Override
	public void updateChefState(long demandeid, String newstate) {
		repository.updateChefState(demandeid, newstate);
	}

	@Override
	public void deleteRembUid(long uid) {
		repository.deleteRembUid(uid);
	}
}
