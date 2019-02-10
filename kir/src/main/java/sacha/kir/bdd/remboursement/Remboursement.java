package sacha.kir.bdd.remboursement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "remboursement")
public class Remboursement {
	 
	@Id
	@Column(name="DemandeID")
	private Long demande_id;
	
	@Column(name="Titre")
	private String titre;
	
	@Column(name="Montant")
	private float montant;
	
	@Column(name="Date")
	private String date;
	
	@Column(name="Motif")
	private String motif;
	
	@Column(name="validation_rh")
	private String validationrh;
	
	@Column(name="validation_chef_service")
	private String validationchefservice;
	
	@Column(name="JustificatifID")
	private long justificatifid;
	
	@Column(name="UID")
	private long uid;
	
	public Remboursement() {}

	public Remboursement(Long demande_id, String titre, float montant, String date, String motif, String validationrh,
			String validationchefservice, long justificatifid, long uid) {
		this.demande_id = demande_id;
		this.titre = titre;
		this.montant = montant;
		this.date = date;
		this.motif = motif;
		this.validationrh = validationrh;
		this.validationchefservice = validationchefservice;
		this.justificatifid = justificatifid;
		this.uid = uid;
	}

	public Long getDemande_id() {
		return demande_id;
	}

	public void setDemande_id(Long demande_id) {
		this.demande_id = demande_id;
	}
	
	public String getTitre() {
		return titre;
	}
	
	public void setTitre(String titre) {
		this.titre = titre;
	}

	public float getMontant() {
		return montant;
	}

	public void setMontant(float montant) {
		this.montant = montant;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getValidationrh() {
		return validationrh;
	}

	public void setValidationrh(String validationrh) {
		this.validationrh = validationrh;
	}

	public String getValidationchefservice() {
		return validationchefservice;
	}

	public void setValidationchefservice(String validationchefservice) {
		this.validationchefservice = validationchefservice;
	}

	public long getJustificatifid() {
		return justificatifid;
	}

	public void setJustificatifid(long justificatifid) {
		this.justificatifid = justificatifid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}
	
	@Override
    public String toString() {
        return motif + " " + montant;
    }	
	
	
	
	
}
