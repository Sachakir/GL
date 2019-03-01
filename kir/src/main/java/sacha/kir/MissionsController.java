package sacha.kir;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sacha.kir.bdd.approle.InterfaceAppRoleService;
import sacha.kir.bdd.appuser.InterfaceAppUserService;
import sacha.kir.bdd.conges.InterfaceCongesService;
import sacha.kir.bdd.membresmission.InterfaceMembresMissionService;
import sacha.kir.bdd.membresmission.MembresMission;
import sacha.kir.bdd.membresservice.InterfaceMembresServiceBddService;
import sacha.kir.bdd.membresservice.MembresServiceBdd;
import sacha.kir.bdd.membresservice.Role;
import sacha.kir.bdd.mission.InterfaceMissionService;
import sacha.kir.bdd.mission.Mission;
import sacha.kir.bdd.note.InterfaceNoteService;
import sacha.kir.bdd.remboursement.InterfaceRemboursementService;
import sacha.kir.bdd.services.InterfaceServiceBddService;
import sacha.kir.bdd.userrole.InterfaceUserRoleService;
import sacha.kir.bdd.utilisateur.InterfaceUtilisateurService;
import sacha.kir.bdd.utilisateur.Utilisateur;
import sacha.kir.form.UserList;

@Controller
@RequestMapping("/missions")
public class MissionsController {
	@Autowired
    InterfaceCongesService CongesService;
	@Autowired
    InterfaceUtilisateurService UtilisateurService;
	@Autowired
	InterfaceUserRoleService UserRoleService;
	@Autowired
    InterfaceAppRoleService AppRoleService;
	@Autowired
    InterfaceAppUserService AppUserService;
	@Autowired
    InterfaceMembresMissionService MembresMissionService;
	@Autowired
    InterfaceMissionService MissionService;
	@Autowired
    InterfaceRemboursementService RemboursementService;
    @Autowired
    InterfaceNoteService NoteService;
    @Autowired
	InterfaceServiceBddService ServiceBddService;
	@Autowired
	InterfaceMembresServiceBddService MembresServiceBddService;
	
	@GetMapping(value = {"", "/"})
    public String listMissions(@RequestParam(value = "mission_id", required = false) String mission_id, Principal principal, Model model)
    {
		// Recuperation de l'uid de la personne voulant gérer ses missions
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	long uid = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	
    	// Recherche des missions dont l'utilisateur est responsable
    	List<Mission> missions = MissionService.findAll();
    	List<Mission> mesMissions = new ArrayList<Mission>();
    	for (int i = 0;i < missions.size();i++) {
    		if (missions.get(i).getResponsable_id() == uid) {
    			mesMissions.add(missions.get(i));
    		}
    	}
    	
    	// Listage des membres d'une mission et des autres utilisateurs si mission_id renseigne
    	if(mission_id != null) {
    		try {
    			long missionId = Long.parseLong(mission_id);
    			for(Mission missionUser : mesMissions) {
    				if(missionUser.getMission_id() == missionId) {
    					
    					// Services et id respectifs
    	    			List<Long> services_ids = ServiceBddService.getServiceIdList();
    	    			Map<Long, String> services = ServiceBddService.getAllServiceNames();
    	    			
    	    			// Stockage des utilisateurs non membres de la mission
    	    			List<Utilisateur> nonMembres = new ArrayList<Utilisateur>();
    	    			Map<Long, List<Utilisateur>> nonMembresServices = new HashMap<Long, List<Utilisateur>>();
    	    			
    	    			// Stockage des utilisateurs membres de la mission
    	    			List<Utilisateur> membres = new ArrayList<Utilisateur>();
    	    			Map<Long, List<Utilisateur>> membresServices = new HashMap<Long, List<Utilisateur>>();
    	    			List<Long> membres_id = MembresMissionService.findMembersByMissionId(missionId);	
    	    			
    	    			for(long service_id : services_ids) {
    	    				// Initialisation pour le service
    	    				membresServices.put(service_id, new ArrayList<Utilisateur>());
    	    				nonMembresServices.put(service_id, new ArrayList<Utilisateur>());
    	    				
    	    				List<Long> membresService =  MembresServiceBddService.getAllUidByServiceId(service_id);
    	    				for(long membre_uid : membresService) {
    	    					Utilisateur user = UtilisateurService.findById(membre_uid);
    	    					
    	    					// Ajout a la liste des membres ou non membres
    	    					if(membres_id.contains(user.getUID())) {
    	    						membres.add(user);
    	    						membresServices.get(service_id).add(user);
    	    					}
    	    					else {
    	    						nonMembres.add(user);
    	    						nonMembresServices.get(service_id).add(user);
    	    					}
    	    				}
    	    			}
    	    			
    	    			model.addAttribute("mission", missionUser);
    	    			model.addAttribute("services", services);
    	    			model.addAttribute("services_ids", services_ids);
    	    			model.addAttribute("membres", membres);
    	    			model.addAttribute("membresServices", membresServices);
    	    			model.addAttribute("nonMembres", nonMembres);
    	    			model.addAttribute("nonMembresServices", nonMembresServices);
    	    	    	
    					break;
    				}
    			}
    		}
    		catch (NumberFormatException e) {
    			e.printStackTrace();
    		}
    	}
 	
    	model.addAttribute("userList", new UserList());
    	model.addAttribute("mesmissions",mesMissions);
  /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		SachaClasse nbCongesEtRemb = new SachaClasse();	
  		boolean IsChef = nbCongesEtRemb.isChef(principal, UtilisateurService, MembresServiceBddService);
  		if (IsChef)
  		{
  			int nbConges = nbCongesEtRemb.getNbConges(CongesService, UtilisateurService, MembresServiceBddService, principal);
  			int nbRemb = nbCongesEtRemb.getNbRemb(principal, MembresServiceBddService, RemboursementService, UtilisateurService);
  			
  	        model.addAttribute("nbRemb", nbRemb);
  			model.addAttribute("nbConges",nbConges);
  			model.addAttribute("IsChef", IsChef);
  		}
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
		return "missions/choixMission";
    }
	
	@GetMapping("/create")
    public String addMission(Mission mission,Model model,Principal principal)
    {	
		// Services et id respectifs
		List<Long> services_ids = ServiceBddService.getServiceIdList();
		Map<Long, String> services = ServiceBddService.getAllServiceNames();
		
		// Stockage des chefs de services et utilisateurs
		List<Utilisateur> chefsServices = new ArrayList<Utilisateur>();
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		Map<Long, List<Utilisateur>> utilisateursServices = new HashMap<Long, List<Utilisateur>>();
		
		for(long service_id : services_ids) {
			// Initialisation pour le service
			utilisateursServices.put(service_id, new ArrayList<Utilisateur>());
			
			List<Long> membresService =  MembresServiceBddService.getAllUidByServiceId(service_id);
			for(long uid : membresService) {
				Utilisateur user = UtilisateurService.findById(uid);
				MembresServiceBdd membre = MembresServiceBddService.findById(uid);
				
				// Ajout a la liste des membres
				utilisateursServices.get(service_id).add(user);
				utilisateurs.add(user);
				if(membre.getRoleId() == Role.chefDeService.getRoleId()) {
					chefsServices.add(user);
				}
			}
		}
    	
    	UserList ul = new UserList(); 	
    	model.addAttribute("userlist",ul);
    	model.addAttribute("services", services);
    	model.addAttribute("services_ids", services_ids);
    	model.addAttribute("chefsServices", chefsServices);
    	model.addAttribute("utilisateurs", utilisateurs);
    	model.addAttribute("utilisateursServices", utilisateursServices);
  /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		SachaClasse nbCongesEtRemb = new SachaClasse();	
  		boolean IsChef = nbCongesEtRemb.isChef(principal, UtilisateurService, MembresServiceBddService);
  		if (IsChef)
  		{
  			int nbConges = nbCongesEtRemb.getNbConges(CongesService, UtilisateurService, MembresServiceBddService, principal);
  			int nbRemb = nbCongesEtRemb.getNbRemb(principal, MembresServiceBddService, RemboursementService, UtilisateurService);
  			
  	        model.addAttribute("nbRemb", nbRemb);
  			model.addAttribute("nbConges",nbConges);
  			model.addAttribute("IsChef", IsChef);
  		}
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
        return "missions/newMissionPage";
    }
	
	@PostMapping("/create")
    public String checkMission(@Valid Mission mission,@ModelAttribute("userlist") UserList userlist)
    {
		//Creation de la mission
		
		System.out.println(mission.getResponsable_id());
		System.out.println(mission.getDate_debut());
		System.out.println(mission.getDate_fin());
		System.out.println(mission.getTitre());
		System.out.println(mission.getDescription());
		int maxid = MissionService.getMaxMissionId();
		mission.setMission_id((long) (maxid + 1));
		
		MissionService.addMission(mission);
		//Ajout des membres
		System.out.println(userlist.getUserList().size());
		MembresMission membresMission = new MembresMission();

		for (int i = 0;i < userlist.getUserList().size();i++)
		{
			membresMission.setMission_id((long) (maxid + 1));
			membresMission.setMembre_uid(userlist.getUserList().get(i).getUID());
			MembresMissionService.addMembresMission(membresMission);
		}
		
        return "redirect:/missions";
    }
	
	@GetMapping("/edit") 
    public String roleChanged() {
    	return "forward:/notFound";
    }
	
	@PostMapping("/edit")
	public String addMembresMission(@Valid UserList userList, @Valid Mission mission)
	{
		MembresMissionService.deleteMembresByMissionId(mission.getMission_id());
		
		MembresMission membresMission = new MembresMission();
		for (int i = 0;i < userList.getUserList().size();i++)
		{
			membresMission.setMission_id((mission.getMission_id()));
			membresMission.setMembre_uid(userList.getUserList().get(i).getUID());
			MembresMissionService.addMembresMission(membresMission);
		}
		
		return "redirect:/missions?mission_id=" + mission.getMission_id();
	}
	
	@GetMapping("/delete")
	public String deleteMission(@RequestHeader(value = "referer", required = false) final String referer,
								@RequestParam(value = "id", required = false) String mission_id, 
								Model model, Principal principal) {
		if(mission_id != null && referer != null) {
			String[] names = principal.getName().split("\\.");
			Utilisateur user = UtilisateurService.findPrenomNom(names[1], names[0]);
			try {
				long missionID = Long.parseLong(mission_id);
				Mission mission = MissionService.findMissionById(missionID);
				
				// Devrait être en théorie du code mort
				if(mission.getResponsable_id() != user.getUID()) {
					throw new AccessDeniedException("403 returned");
				}
				
				// Suppression de la mission
				MissionService.deleteMissionById(missionID);
				return "redirect:" + referer;
			}
			catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return "forward:/notFound";
	}
}
