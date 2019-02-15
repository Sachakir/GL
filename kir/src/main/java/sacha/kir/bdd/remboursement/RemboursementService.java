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
		r.setValidationrh("Valide");
		
		repository.save(r);
	}
	
	public void addNewRemboursement(Remboursement r) {
		long demande_id = 1;
		if(repository.count() != 0) {
			demande_id = repository.getMaxId();
		}
		
		r.setDemande_id(demande_id);
		repository.save(r);
	}
}
