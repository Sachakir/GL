package root.bdd.conges;

public enum TypesConges {
	CongePaye(0, "Congé payé"),
    RTT(1,"RTT");

	public int id;
	public String name;

    private TypesConges (int id, String name){
	    	this.id = id;    
	    	this.name = name;
    }
};
