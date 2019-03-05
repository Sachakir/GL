package root.bdd.remboursementsnote;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import root.bdd.remboursementsnote.CompositeKey;

@Entity
@IdClass(CompositeKey.class)
@Table(name = "remboursements_note_frais")
public class RemboursementsNote {
	 
	@Id
	@Column(name="NoteID")
	private Long note_id;
	
	@Id
	@Column(name="DemandeID")
	private Long demande_id;
	
	public RemboursementsNote() {}

	public RemboursementsNote(Long note_id, Long demande_id) {
		this.note_id = note_id;
		this.demande_id = demande_id;
	}

	public Long getNote_id() {
		return note_id;
	}

	public void setNote_id(Long note_id) {
		this.note_id = note_id;
	}

	public Long getDemande_id() {
		return demande_id;
	}

	public void setDemande_id(Long demande_id) {
		this.demande_id = demande_id;
	}	
}
