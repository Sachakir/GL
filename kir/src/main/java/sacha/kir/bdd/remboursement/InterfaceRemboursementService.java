package sacha.kir.bdd.remboursement;

import java.util.List;

public interface InterfaceRemboursementService {
	public List<Remboursement> findAll();
	public void addRemboursement();
}
