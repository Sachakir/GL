package sacha.kir.bdd.remboursement;

import java.util.List;

public interface InterfaceRemboursementService {
	public List<Remboursement> findAll();
	public Remboursement findById(long demande_id);
	public Remboursement addNewRemboursement(Remboursement r);
	public List<Remboursement> getAllByIdAsc(long uid);
	public List<Remboursement> getAllByIdDesc(long uid);
	public List<Remboursement> getAllByIdAsc(long uid, int limit);
	public List<Remboursement> getAllByIdDesc(long uid, int limit);
	public List<Remboursement> getAllByIdListAsc(List<Long> demande_ids);
	public List<Remboursement> getAllByIdListDesc(List<Long> demande_ids);
	public void updateFinancesState(long demandeid, String newstate);
	public void updateChefState(long demandeid,String newstate);
	public String getFinancesState(long demandeid);
	public String getChefState(long demandeid);
	public void deleteRembUid(long uid);
	public void deleteById(long demande_id);

}
