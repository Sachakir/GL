package root.forms.service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ServiceForm {
	@NotNull
	@Size(min=1, message="Le nom du service ne peut pas être vide")
	@Size(max=42, message="Le nom du service ne peut pas faire plus de 42 caractères")
	private String nom;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
}
