package root.bdd.membresservice;

public enum Role {
	
	utilisateur(1, "Utilisateur"), 
	chefDeService(2, "Chef de Service");
	
	private final long roleId; 
	private final String roleName;
	
	private Role(long roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}
	
	public long getRoleId() {
		return roleId;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public static String getRoleNameById(long roleId) {
		for(Role role : Role.values()) {
			if(role.getRoleId() == roleId) 
			{
				return role.getRoleName();
			}
		}
		
		return "Indéfini";
	}
	
	public static Role getRoleById(long roleId) {
		for(Role role : Role.values()) {
			if(role.getRoleId() == roleId) 
			{
				return role;
			}
		}
		
		return null;
	}
}
