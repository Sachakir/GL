package sacha.kir.bdd.services;

public enum ServicesFixes {
	
	ressourcesHumaines(1), 
	finances(2);
	
	private final long serviceId; 
	
	private ServicesFixes(long serviceId) {
		this.serviceId = serviceId;
	}
	
	public long getServiceId() {
		return serviceId;
	}
}
