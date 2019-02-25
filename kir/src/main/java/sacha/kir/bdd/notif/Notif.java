package sacha.kir.bdd.notif;

public class Notif {

	private String description;
	private String date;
	private String lien;
	
	public Notif (String description, String date, String lien) {
		this.description = description;
		this.date = date;
		this.lien = lien;
	}

	public String getDescription() {
		return description;
	}

	public String getDate() {
		return date;
	}

	public String getLien() {
		return lien;
	}
}
