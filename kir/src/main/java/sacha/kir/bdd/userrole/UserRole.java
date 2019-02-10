package sacha.kir.bdd.userrole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_role")
public class UserRole {
	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="USER_ID")
	private Long user_id;
	
	@Column(name="ROLE_ID")
	private Long role_id;
	
	public UserRole() {}

	public UserRole(Long id, Long user_id, Long role_id) {
		this.id = id;
		this.user_id = user_id;
		this.role_id = role_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}
	
	@Override
    public String toString() {
        return id + " " + user_id + " " + role_id;
    }
}
