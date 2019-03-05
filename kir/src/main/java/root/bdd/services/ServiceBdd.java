package root.bdd.services;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "services")
public class ServiceBdd {
	 
	@Id
	@Column(name="ServiceID")
	private Long service_id;
	
	@Column(name="Nom")
	private String nom;
	
	public ServiceBdd() {
		
	}

	public ServiceBdd(long service_id, String nom) {
		this();
		this.service_id = service_id;
		this.nom = nom;
	}

	// Getters
	public Long getService_id() {
		return service_id;
	}
	
	public String getNom() {
		return nom;
	}
	
	// Setters
	public void setService_id(Long service_id) {
		this.service_id = service_id;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	
}
