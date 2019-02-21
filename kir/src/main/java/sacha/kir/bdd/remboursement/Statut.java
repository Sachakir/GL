package sacha.kir.bdd.remboursement;

public enum Statut {
	
	enAttente("En attente"), valide("Validé"), refuse("Refusé");
	
	private String statut;
	
	private Statut(String statut) {
		this.statut = statut;
	}
	
	public String statut() {
		return statut;
	}
}
