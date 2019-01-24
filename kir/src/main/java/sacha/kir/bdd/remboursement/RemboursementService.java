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
		r.setDate("2019-01-23");
		r.setDemande_id((long) 1);
		r.setJustificatifid(1);
		r.setMontant((float) 1.5);
		r.setMotif("Parce que, oui");
		r.setUid(1);
		r.setValidationchefservice("enAttente");
		r.setValidationrh("Valide");
		
		repository.save(r);
	}

}
