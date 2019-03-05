package root.bdd.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceBddService implements InterfaceServiceBddService {

	@Autowired
	ServiceBddRepository repository;
	
	@Override
	public List<ServiceBdd> findAll() {
		List<ServiceBdd> services = (List<ServiceBdd>) repository.findAll();
		return services;
	}
	
	@Override
	public Map<Long, String> getAllServiceNames() {
		Map<Long, String> result = new HashMap<Long, String>();
		List<ServiceBdd> services = (List<ServiceBdd>) repository.findAll();
		
		for(ServiceBdd service : services) {
			result.put(service.getService_id(), service.getNom());
		}
		
		return result;
	}
	
	@Override
	public List<Long> getServiceIdList() {
		return repository.listAllServiceIdAsc();
	}
	
	@Override
	public ServiceBdd findById(long service_id) {
		return repository.findById(service_id).orElse(null);
	}
	
	@Override
	public ServiceBdd findByName(String nom) {
		return repository.findByName(nom);
	}
	
	@Override
	public void addServiceBdd(String nom) {
		long service_id = 1;
		if(repository.count() > 0) {
			service_id += repository.getMaxId();
		}
		
		repository.save(new ServiceBdd(service_id, nom));
	}
	
	@Override
	public void deleteById(long service_id) {
		repository.deleteById(service_id);
	}
}
