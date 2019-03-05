package root.bdd.appuser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_user")
public class AppUser {
	@Id
	@Column(name="USER_ID")
	private Long user_id;
	
	@Column(name="USER_NAME")
	private String user_name;
	
	@Column(name="ENCRYTED_PASSWORD")
	private String encrypted_password;
	
	public AppUser() {}

	public AppUser(Long user_id, String user_name, String encrypted_password) {
		this.user_id = user_id;
		this.user_name = user_name;
		this.encrypted_password = encrypted_password;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getEncrypted_password() {
		return encrypted_password;
	}

	public void setEncrypted_password(String encrypted_password) {
		this.encrypted_password = encrypted_password;
	}
	
	@Override
    public String toString() {
        return user_name + " " + user_id;
    }
	
}
