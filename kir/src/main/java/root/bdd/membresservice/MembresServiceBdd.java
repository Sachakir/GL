package root.bdd.membresservice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "membres_service")
public class MembresServiceBdd {
	 
	@Id
	@Column(name="UID")
	private Long uid;
	
	@Column(name="RoleID")
	private Long role_id;
	
	@Column(name="ServiceID")
	private Long service_id;
	
	@Column(name="is_admin")
	private boolean isAdmin;
	
	public MembresServiceBdd() {
		this.isAdmin = false;
	}

	public MembresServiceBdd(long uid, long role_id, long service_id, boolean isAdmin) {
		this();
		this.uid = uid;
		this.role_id = role_id;
		this.service_id = service_id;
		this.isAdmin = isAdmin;
	}

	// Getters
	public Long getUid() {
		return uid;
	}
	
	public Long getRoleId() {
		return role_id;
	}
	
	public Long getServiceId() {
		return service_id;
	}
	
	public boolean getIsAdmin() {
		return isAdmin;
	}
	
	// Setters
	public void setUid(Long uid) {
		this.uid = uid;
	}

	public void setRoleId(Long role_id) {
		this.role_id = role_id;
	}

	public void setServiceId(Long service_id) {
		this.service_id = service_id;
	}
	
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
