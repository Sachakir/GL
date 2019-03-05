package root.bdd.note;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "note_frais")
public class Note {
	@Id
	@Column(name="NoteID")
	private Long note_id;
	
	@Column(name="Mois")
	private String mois;
	
	@Column(name="UID")
	private Long uid;

	public Note() {}
	
	public Note(Long note_id, String mois, Long uid) {
		this.note_id = note_id;
		this.mois = mois;
		this.uid = uid;
	}

	public Long getNote_id() {
		return note_id;
	}

	public void setNote_id(Long note_id) {
		this.note_id = note_id;
	}

	public String getMois() {
		return mois;
	}

	public void setMois(String mois) {
		this.mois = mois;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}
	
	@Override
    public String toString() {
        return mois + " " + note_id + " " + uid;
    }
}
