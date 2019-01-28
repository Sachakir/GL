package sacha.kir.bdd.conges;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "conges")
public class Conges {
	
	 @Id
	 @Column(name="CongesID")
	 private Long conges_id;
	 
	 @Column(name="ValidationRH")
	 private String validation_rh;
	 
	 @Column(name="validation_chef_service")
	 private String validation_chef_service;
	 
	 @Column(name="UID")
	 private long UID;
	 
	 @Column(name="date_fin")
	 private String date_fin;
	 
	 @Column(name="date_debut")
	 private String date_debut;
	 
	 
	 
	 public Conges() {}

	public Conges(Long congesid, String datedebut, String datefin, String validationrh, String validationchefdeservice,
			long uid)
	{
		this.conges_id = congesid;
		this.date_debut = datedebut;
		this.date_fin = datefin;
		this.validation_rh = validationrh;
		this.validation_chef_service = validationchefdeservice;
		this.UID = uid;
	}

	public Long getCongesid() {
		return conges_id;
	}

	public void setCongesid(Long congesid) {
		this.conges_id = congesid;
	}

	public String getDatedebut() {
		return date_debut;
	}

	public void setDatedebut(String datedebut) {
		this.date_debut = datedebut;
	}

	public String getDatefin() {
		return date_fin;
	}

	public void setDatefin(String datefin) {
		this.date_fin = datefin;
	}

	public String getValidationrh() {
		return validation_rh;
	}

	public void setValidationrh(String validationrh) {
		this.validation_rh = validationrh;
	}

	public String getValidationchefdeservice() {
		return validation_chef_service;
	}

	public void setValidationchefdeservice(String validationchefdeservice) {
		this.validation_chef_service = validationchefdeservice;
	}

	public long getUid() {
		return UID;
	}

	public void setUid(long uid) {
		this.UID = uid;
	}
	 
	@Override
    public String toString() {
        return date_debut + " " + date_fin + " " + UID;
    }
}
