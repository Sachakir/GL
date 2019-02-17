package sacha.kir.bdd.remboursement;

import java.util.List;

public interface InterfaceRemboursementService {
	public List<Remboursement> findAll();
	public void addRemboursement();
	public Remboursement addNewRemboursement(Remboursement r);
	public List<Remboursement> getAllByIdAsc(long uid);
	public List<Remboursement> getAllByIdDesc(long uid);
	public List<Remboursement> getAllByIdAsc(long uid, int limit);
	public List<Remboursement> getAllByIdDesc(long uid, int limit);
	public void updateRHState(long demandeid,String newstate);
	public void updateChefState(long demandeid,String newstate);

}
