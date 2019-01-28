package sacha.kir.bdd.missionnote;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(CompositeKey.class)
@Table(name = "missions_note_frais")
public class MissionsNote {
	@Id
	@Column(name="NoteID")
	private Long note_id;
	
	@Id
	@Column(name="MissionID")
	private Long mission_id;
	
	public MissionsNote() {}

	public MissionsNote(Long note_id, Long mission_id) {
		super();
		this.note_id = note_id;
		this.mission_id = mission_id;
	}

	public Long getNote_id() {
		return note_id;
	}

	public void setNote_id(Long note_id) {
		this.note_id = note_id;
	}

	public Long getMission_id() {
		return mission_id;
	}

	public void setMission_id(Long mission_id) {
		this.mission_id = mission_id;
	}
	
	@Override
    public String toString() {
        return mission_id + " " + note_id;
    }
}
