package sacha.kir.bdd.remboursement;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
	private LocalDate date;
	
	@Column(name="Motif")
	private String motif;
	
	@Column(name="validation_finances")
	private String validationfinances;
	
	@Column(name="validation_chef_service")
	private String validationchefservice;
	
	@Column(name="JustificatifID")
	private Long justificatifid;
	
	@Column(name="UID")
	private Long uid;
	
	@Column(name="MissionID")
	private Long mission_id;
	
	@Column(name="Timestamp")
	private final LocalDateTime timestamp;
	
	public Remboursement() {
		this.timestamp = LocalDateTime.now();
	}

	public Remboursement(Long demande_id, String titre, float montant, LocalDate date, String motif, String validationfinances,
			String validationchefservice, Long justificatifid, Long uid, Long mission_id) {
		this();
		this.demande_id = demande_id;
		this.titre = titre;
		this.montant = montant;
		this.date = date;
		this.motif = motif;
		this.validationfinances = validationfinances;
		this.validationchefservice = validationchefservice;
		this.justificatifid = justificatifid;
		this.uid = uid;
		this.mission_id = mission_id;
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public String getValidationfinances() {
		return validationfinances;
	}

	public void setValidationfinances(String validationfinances) {
		this.validationfinances = validationfinances;
	}

	public String getValidationchefservice() {
		return validationchefservice;
	}

	public void setValidationchefservice(String validationchefservice) {
		this.validationchefservice = validationchefservice;
	}

	public Long getJustificatifid() {
		return justificatifid;
	}

	public void setJustificatifid(Long justificatifid) {
		this.justificatifid = justificatifid;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}
	
	public Long getMission_id() {
		return mission_id;
	}

	public void setMission_id(Long mission_id) {
		this.mission_id = mission_id;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	@Override
    public String toString() {
        return motif + " " + montant;
    }		
}
