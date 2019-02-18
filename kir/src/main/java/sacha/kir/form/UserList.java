package sacha.kir.form;

import java.util.List;

import sacha.kir.bdd.utilisateur.Utilisateur;

public class UserList {
	private List<Utilisateur> userList;
	private long missionId;
	
	public long getMissionId() {
		return missionId;
	}

	public void setMissionId(long missionId) {
		this.missionId = missionId;
	}

	public List<Utilisateur> getUserList() {
		return userList;
	}

	public void setUserList(List<Utilisateur> userList) {
		this.userList = userList;
	}

	
}
