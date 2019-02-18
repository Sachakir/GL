package sacha.kir.form;

import java.util.Date;

public class RemboursementV2 {

	private Long demande_id;
	
	private String titre;

	private float montant;

	private String date;

	private String motif;

	private String validationfinances;

	private String validationchefservice;

	private Long justificatifid;

	private Long uid;

	private Long mission_id;
	private String prenomnom;
	private String etatFinal;
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
	public String getPrenomnom() {
		return prenomnom;
	}
	public void setPrenomnom(String prenomnom) {
		this.prenomnom = prenomnom;
	}
	public String getEtatFinal() {
		return etatFinal;
	}
	public void setEtatFinal(String etatFinal) {
		this.etatFinal = etatFinal;
	}
	
}
