package sacha.kir.bdd.notif;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notif")
public class Notif {

	@Id
	@Column(name="NotifID")
	private Long notif_id;

	@Column(name="UID")
	private Long uid;

	@Column(name="Vue")
	private Boolean vue;
	
	@Column(name="Date")
	private Date date;

	@Column(name="Titre")
	private String titre;

	@Column(name="Lien")
	private String lien;

	public Notif(Long notif_id, Long uid, Boolean vue, Date date, String titre, String lien) {
		super();
		this.notif_id = notif_id;
		this.uid = uid;
		this.vue = vue;
		this.date = date;
		this.titre = titre;
		this.lien = lien;
	}

	public Long getNotif_id() {
		return notif_id;
	}

	public void setNotif_id(Long notif_id) {
		this.notif_id = notif_id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Boolean getVue() {
		return vue;
	}

	public void setVue(Boolean vue) {
		this.vue = vue;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getLien() {
		return lien;
	}

	public void setLien(String lien) {
		this.lien = lien;
	}
	
	
}
