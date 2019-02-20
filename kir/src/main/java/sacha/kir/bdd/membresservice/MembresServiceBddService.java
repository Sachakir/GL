package sacha.kir.bdd.membresservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sacha.kir.bdd.services.InterfaceServiceBddService;
import sacha.kir.bdd.services.ServiceBdd;
import sacha.kir.bdd.utilisateur.InterfaceUtilisateurService;
import sacha.kir.bdd.utilisateur.Utilisateur;

@Service
public class MembresServiceBddService implements InterfaceMembresServiceBddService {

	@Autowired
	MembresServiceBddRepository repository;
	
	@Autowired
    InterfaceUtilisateurService UtilisateurService;
	
	@Autowired
	InterfaceServiceBddService ServiceBddService;
	
	@Override
	public List<MembresServiceBdd> findAll() {
		List<MembresServiceBdd> services = (List<MembresServiceBdd>) repository.findAll();
		return services;
	}
	
	@Override
	public MembresServiceBdd findById(long uid) {
		return repository.findById(uid).orElse(null);
	}
	
	@Override
	public MembresServiceBdd addMembreService (MembresServiceBdd membre) {
		if(membre.getRoleId() != null && membre.getServiceId() != null && membre.getUid() != null) {
			Utilisateur user = UtilisateurService.findById(membre.getUid());
			ServiceBdd service = ServiceBddService.findById(membre.getServiceId());
			Role role = Role.getRoleById(membre.getRoleId());
			if(user != null && service != null && role != null ) {
				repository.save(membre);
				return membre;
			}
		}
		return null;
	}
	
	@Override
	public void updateRoleById(long uid, long role_id) {
		repository.updateRoleById(uid, role_id);
	}
	
	@Override
	public void updateServiceById(long uid, long service_id) {
		repository.updateServiceById(uid, service_id);
	}
	
	@Override
	public void updateAdminStatusById(long uid, boolean isAdmin) {
		repository.updateAdminStatusById(uid, isAdmin);
	}
	
	@Override
	public List<Long> getAllUidByServiceId(long service_id) {
		return repository.getAllUidByServiceId(service_id);
	}
	
	@Override
	public MembresServiceBdd getChefByServiceId(long service_id) {
		return repository.getByServiceIdAndRoleId(service_id, Role.chefDeService.getRoleId());
	}
}
