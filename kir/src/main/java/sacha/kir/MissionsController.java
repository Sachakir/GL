package sacha.kir;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
import sacha.kir.form.missions.MissionEditForm;
import sacha.kir.form.missions.MissionForm;

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
    	    			
    	    			// Création du missionForm si non existant
    	    			if(!model.containsAttribute("missionEditForm")) {
    	    				MissionEditForm missionEditForm = new MissionEditForm();
    	    				missionEditForm.setDescription(missionUser.getDescription());
    	    				missionEditForm.setDate_fin(missionUser.getDate_fin().toString());
    	    				
    	    				model.addAttribute("missionEditForm", missionEditForm);
    	    			}
    	    			if(!model.containsAttribute("userList")) {
    	    				UserList userList = new UserList();
    	    				userList.setUserList(membres);
    	    				model.addAttribute("userList", userList);
    	    			}
    	    	    	
    					break;
    				}
    			}
    		}
    		catch (NumberFormatException e) {
    			e.printStackTrace();
    		}
    	}
 	
    	model.addAttribute("mesmissions",mesMissions);
    	/////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		SachaClasse nbCongesEtRemb = new SachaClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
		return "missions/choixMission";
    }
	
	@GetMapping("/edit") 
    public String roleChanged() {
    	return "forward:/notFound";
    }
	
	@PostMapping("/edit")
	public String addMembresMission(@RequestParam(value = "id", required = false) String mission_id, @Valid UserList userList, @Valid MissionEditForm missionEditForm, BindingResult result, RedirectAttributes redirectAttributes, Model model, Principal principal)
	{
		if(mission_id != null) try
		{
			long missionId = Long.parseLong(mission_id);
			Mission mission = MissionService.findMissionById(missionId);
			
			String prenomnom = principal.getName();
	    	String[] names = prenomnom.split("\\.");
	    	long uid = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
	    	
	    	if(mission.getResponsable_id() == uid) {
	    		LocalDate date_fin = null;
				if(!missionEditForm.getDate_fin().equals("")) {
					date_fin = LocalDate.parse(missionEditForm.getDate_fin(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					
					if(date_fin.isBefore(mission.getDate_debut())){
						result.rejectValue("date_fin", "dateBeforeStart", "Date de fin précédant la date de début");
					} else if(date_fin.isBefore(LocalDate.now())) {
						result.rejectValue("date_fin", "dateTooEarly", "Date de fin précédant la date d'aujourd'hui");
					}
				}
				
				if(result.hasErrors()) {
					redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.missionEditForm", result);
					redirectAttributes.addFlashAttribute("missionEditForm", missionEditForm);
					redirectAttributes.addFlashAttribute("userList", userList);
					redirectAttributes.addFlashAttribute("success", false);
				}
				else {
					mission.setDate_fin(date_fin);
					mission.setDescription(missionEditForm.getDescription());
					MissionService.update(mission);
					
					MembresMissionService.deleteMembresByMissionId(mission.getMission_id());
					
					MembresMission membresMission = new MembresMission();
					for (int i = 0;i < userList.getUserList().size();i++)
					{
						membresMission.setMission_id((mission.getMission_id()));
						membresMission.setMembre_uid(userList.getUserList().get(i).getUID());
						MembresMissionService.addMembresMission(membresMission);
					}
					
					redirectAttributes.addFlashAttribute("success", true);
				}
				
				return "redirect:/missions?mission_id=" + mission.getMission_id();
	    	}
	    	else {
	    		throw new AccessDeniedException("Mission non accessible car vous n'êtes pas le responsable de celle-ci.");
	    	}
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		return "redirect:/notFound";
	}
	
	@GetMapping("/create")
    public String addMission(@ModelAttribute("userlist") UserList userlist, Model model,Principal principal)
    {	
		if(!model.containsAttribute("missionForm")) {
    		model.addAttribute("missionForm", new MissionForm());
    	}
		
		if(!model.containsAttribute("userlist")) {
			userlist = new UserList();
			model.addAttribute("userlist", userlist);
		}
		
		// Services et id respectifs
		List<Long> services_ids = ServiceBddService.getServiceIdList();
		Map<Long, String> services = ServiceBddService.getAllServiceNames();
		
		// Stockage des chefs de services
		List<Utilisateur> chefsServices = new ArrayList<Utilisateur>();
		
		// Stockage des utilisateurs non membres de la mission
		List<Utilisateur> nonMembres = new ArrayList<Utilisateur>();
		Map<Long, List<Utilisateur>> nonMembresServices = new HashMap<Long, List<Utilisateur>>();
		
		// Stockage des utilisateurs membres de la mission
		List<Utilisateur> membres = new ArrayList<Utilisateur>();
		Map<Long, List<Utilisateur>> membresServices = new HashMap<Long, List<Utilisateur>>();
		List<Long> membres_id = new ArrayList<Long>();
		for(Utilisateur u : userlist.getUserList()) {
			membres_id.add(u.getUID());
		}
		
		for(long service_id : services_ids) {
			
			membresServices.put(service_id, new ArrayList<Utilisateur>());
			nonMembresServices.put(service_id, new ArrayList<Utilisateur>());
			
			List<Long> membresService =  MembresServiceBddService.getAllUidByServiceId(service_id);
			for(long membre_uid : membresService) {
				Utilisateur user = UtilisateurService.findById(membre_uid);
				MembresServiceBdd membre = MembresServiceBddService.findById(membre_uid);
				
				// Ajout a la liste des membres ou non membres
				if(membres_id.contains(user.getUID())) {
					membres.add(user);
					membresServices.get(service_id).add(user);
				}
				else {
					nonMembres.add(user);
					nonMembresServices.get(service_id).add(user);
				}
				
				// Ajout a la liste des chefs si necessaire
				if(membre.getRoleId() == Role.chefDeService.getRoleId()) {
					chefsServices.add(user);
				}
			}
		}
		
    	model.addAttribute("services", services);
    	model.addAttribute("services_ids", services_ids);
    	model.addAttribute("chefsServices", chefsServices);
    	model.addAttribute("membres", membres);
		model.addAttribute("membresServices", membresServices);
		model.addAttribute("nonMembres", nonMembres);
		model.addAttribute("nonMembresServices", nonMembresServices);
    	/////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		SachaClasse nbCongesEtRemb = new SachaClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
        return "missions/newMissionPage";
    }
	
	@PostMapping("/create")
    public String checkMission(@Valid MissionForm missionForm, BindingResult result, RedirectAttributes redirectAttributes, @ModelAttribute("userlist") UserList userlist)
    {
		// Verification des dates pour les missions
		LocalDate date_debut = null;
		LocalDate date_fin = null;
		
		if(!missionForm.getDate_debut().equals("") && !missionForm.getDate_fin().equals("")) {
			date_debut = LocalDate.parse(missionForm.getDate_debut(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			date_fin = LocalDate.parse(missionForm.getDate_fin(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			
			if(date_debut.isBefore(LocalDate.now())) {
				result.rejectValue("date_debut", "dateTooEarly", "Date de début précédant la date d'aujourd'hui");
			}
			if(date_fin.isBefore(date_debut)) {
				result.rejectValue("date_fin", "dateBeforeStart", "Date de fin précédant la date de début");
			}
		}
		
		// Erreur de formulaire
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.missionForm", result);
			redirectAttributes.addFlashAttribute("missionForm", missionForm);
			redirectAttributes.addFlashAttribute("userlist", userlist);
			
			return "redirect:/missions/create";
		}
		
		//Creation de la mission
		Mission mission = new Mission();
		
		int maxid = MissionService.getMaxMissionId();
		mission.setMission_id((long) (maxid + 1));
		mission.setTitre(missionForm.getTitre());
		mission.setDescription(missionForm.getDescription());
		mission.setResponsable_id(Long.parseLong(missionForm.getResponsable_id()));
		mission.setDate_debut(date_debut);
		mission.setDate_fin(date_fin);
		
		MissionService.addMission(mission);
		//Ajout des membres
		MembresMission membresMission = new MembresMission();

		for (int i = 0;i < userlist.getUserList().size();i++)
		{
			membresMission.setMission_id((long) (maxid + 1));
			membresMission.setMembre_uid(userlist.getUserList().get(i).getUID());
			MembresMissionService.addMembresMission(membresMission);
		}
		
        return "redirect:/missions";
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
