package sacha.kir.bdd.membresmission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(CompositeKey.class)
@Table(name = "membres_mission")
public class MembresMission 
{
	@Id
	@Column(name="MissionID")
	private Long mission_id;
	@Id
	@Column(name="MembreUID")
	private Long membre_uid;
	
	public MembresMission() {}

	public MembresMission(Long mission_id, Long membre_uid) 
	{
		this.mission_id = mission_id;
		this.membre_uid = membre_uid;
	}

	public Long getMission_id() {
		return mission_id;
	}

	public void setMission_id(Long mission_id) {
		this.mission_id = mission_id;
	}

	public Long getMembre_uid() {
		return membre_uid;
	}

	public void setMembre_uid(Long membre_uid) {
		this.membre_uid = membre_uid;
	}
	
	@Override
    public String toString() {
        return membre_uid + " " + mission_id;
    }
}


