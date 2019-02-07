package sacha.kir.bdd.approle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "app_role")
public class AppRole {
	@Id
	@Column(name="ROLE_ID")
	@NotNull
	private Long role_id;
	@NotNull
	@Size(min=2, max=30)
	@Column(name="ROLE_NAME")
	private String role_name;
	
	public AppRole() {}

	public AppRole(Long role_id, String role_name) {
		this.role_id = role_id;
		this.role_name = role_name;
	}

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	
	@Override
    public String toString() {
        return role_name + " " + role_id;
    }
}
