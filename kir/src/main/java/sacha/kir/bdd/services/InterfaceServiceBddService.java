package sacha.kir.bdd.services;

import java.util.List;
import java.util.Map;

public interface InterfaceServiceBddService {
	public List<ServiceBdd> findAll();
	public Map<Long, String> getAllServiceNames();
	public List<Long> getServiceIdList();
	public ServiceBdd findById(long service_id);
	public void addServiceBdd(String nom);
}
