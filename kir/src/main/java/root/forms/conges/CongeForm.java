package root.forms.conges;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CongeForm {
	
	private String username;
	@NotNull
	@Size(min=1, max=42)
	private String dateDebut;
	@NotNull
	@Size(min=1, max=42)
	private String dateFin;
	
	private long congesid;
	
	@NotNull
	@Size(min=1)
	private String motifRefus;
	
	@NotNull
	private String type;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}
	public String getDateFin() {
		return dateFin;
	}
	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}
	public long getCongesid() {
		return congesid;
	}
	public void setCongesid(long c) {
		this.congesid = c;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMotifRefus() {
		return motifRefus;
	}
	public void setMotifRefus(String motifRefus) {
		this.motifRefus = motifRefus;
	}
	
}
