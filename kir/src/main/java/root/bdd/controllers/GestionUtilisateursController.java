package root.bdd.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import root.EncrytedPasswordUtils;
import root.NotifClasse;
import root.bdd.appuser.InterfaceAppUserService;
import root.bdd.conges.InterfaceCongesService;
import root.bdd.justificatif.InterfaceJustificatifService;
import root.bdd.membresmission.InterfaceMembresMissionService;
import root.bdd.membresservice.InterfaceMembresServiceBddService;
import root.bdd.membresservice.MembresServiceBdd;
import root.bdd.membresservice.MembresServiceBddRepository;
import root.bdd.membresservice.Role;
import root.bdd.mission.InterfaceMissionService;
import root.bdd.mission.Mission;
import root.bdd.note.InterfaceNoteService;
import root.bdd.notif.InterfaceNotifService;
import root.bdd.remboursement.InterfaceRemboursementService;
import root.bdd.remboursementsnote.InterfaceRemboursementsNoteService;
import root.bdd.services.InterfaceServiceBddService;
import root.bdd.utilisateur.InterfaceUtilisateurService;
import root.bdd.utilisateur.Utilisateur;
import root.bdd.utilisateur.UtilisateurRepository;
import root.forms.utilisateur.UserForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administration/gestion-utilisateurs")
public class GestionUtilisateursController {
	
	// TODO IMPORTANT A METTRE AU DEBUT DE CHAQUE CONTROLLER
	@ModelAttribute("authentication")
    public List<String> setRoles(Authentication authentication) {
		// Si l'utilisateur est loggé, on insère dans le model ses roles
		if(authentication != null) {
			List<String> toReturn = new ArrayList<String>();
			for(GrantedAuthority gr : authentication.getAuthorities()) {
				toReturn.add(gr.toString());
			}
	        return toReturn;
		}
		return null;
    }
	
	@Autowired
    InterfaceCongesService CongesService;
	@Autowired
    InterfaceUtilisateurService UtilisateurService;
    @Autowired
    InterfaceMissionService MissionService;
    @Autowired
    InterfaceMembresMissionService MembresMissionService;
    @Autowired
    InterfaceJustificatifService JustificatifService;
    @Autowired
    InterfaceRemboursementService RemboursementService;
    @Autowired
    InterfaceNoteService NoteService;
    @Autowired
    InterfaceRemboursementsNoteService RemboursementsNoteService;
    @Autowired
    InterfaceAppUserService AppUserService;
	@Autowired
	InterfaceServiceBddService ServiceBddService;
	@Autowired
	InterfaceMembresServiceBddService MembresServiceBddService;
	@Autowired
	InterfaceNotifService NotifService;
	
	/** Repositories **/
	@Autowired
	UtilisateurRepository utilisateurRepository;
	@Autowired
	MembresServiceBddRepository membresServiceBddRepository;
	/******************/
	
	private final String root = "/administration/gestion-utilisateurs";
	
	// Roles et Services
	public Model addServiceListIntoModel(Model model) {
		List<Long> services_ids = ServiceBddService.getServiceIdList();
		List<Long> roles_ids = new ArrayList<Long>();
		Map<Long, String> services = ServiceBddService.getAllServiceNames();
		Map<Long, String> roles = new HashMap<Long, String>();
		
		for(Role r : Role.values()) {
			roles.put(r.getRoleId(), r.getRoleName());
			roles_ids.add(r.getRoleId());
		}
		
		model.addAttribute("services_ids", services_ids);
		model.addAttribute("services", services);
		model.addAttribute("roles_ids", roles_ids);
		model.addAttribute("roles", roles);
		
		return model;
	}
	
	@RequestMapping(value = {"/", ""})
    public String adminShow(@RequestParam(value = "id", required = false) String user_id, Model model, Principal principal, UserForm userForm)
    {
    	// Informations pour les services disponibles (liste des services)
    	model = addServiceListIntoModel(model);
  
    	// Listage de tous les utilisateurs
    	List<Long> servicesIds = ServiceBddService.getServiceIdList();
    	Map<Long, Map<Long, List<Utilisateur>>> utilisateurs = new HashMap<Long, Map<Long, List<Utilisateur>>>();
    	Map<Long, List<Utilisateur>> usersServices = new HashMap<Long, List<Utilisateur>>();
    	Map<Long, List<Utilisateur>> chefsServices = new HashMap<Long, List<Utilisateur>>();
    	utilisateurs.put(Role.utilisateur.getRoleId(), usersServices);
    	utilisateurs.put(Role.chefDeService.getRoleId(), chefsServices);
    	
    	List<Long> roles_ids = new ArrayList<Long>();
    	roles_ids.add(Role.chefDeService.getRoleId());
    	roles_ids.add(Role.utilisateur.getRoleId());
    	
    	Map<Long, String> roles = new HashMap<Long, String>();
    	roles.put(Role.chefDeService.getRoleId(), "Chefs de service");
    	roles.put(Role.utilisateur.getRoleId(), "Utilisateurs");
    	
    	// Comptage des membres dans chaque service
    	Map<Long, Integer> nbMembresServices = new HashMap<Long, Integer>();
    	
    	for(long serviceId : servicesIds) {
    		usersServices.put(serviceId, new ArrayList<Utilisateur>());
    		chefsServices.put(serviceId, new ArrayList<Utilisateur>());
    		
    		List<Long> membresService = MembresServiceBddService.getAllUidByServiceId(serviceId);
    		nbMembresServices.put(serviceId, membresService.size());
    		
    		for(long membreUid : membresService) {
				Utilisateur user = UtilisateurService.findById(membreUid);
				MembresServiceBdd membre = MembresServiceBddService.findById(membreUid);
				
				if(membre.getRoleId() == Role.utilisateur.getRoleId()) {
					usersServices.get(serviceId).add(user);
				}
				else if(membre.getRoleId() == Role.chefDeService.getRoleId()) {
					chefsServices.get(serviceId).add(user);
				}
    		}
    	}
    	
    	model.addAttribute("utilisateurs", utilisateurs);
    	model.addAttribute("roles_ids_panel", roles_ids);
    	model.addAttribute("roles_panel", roles);
    	model.addAttribute("nbMembresServices", nbMembresServices);
    	
    	// TODO ???
    	String[] names = principal.getName().split("\\.");
    	model.addAttribute("notAdmin",UtilisateurService.findPrenomNom(names[1], names[0]).getUID());
    	
    	// Affichage du form d'affichage (afficher/modifier un compte user)
    	if(user_id != null) {
    		try {
    			long uid = Long.parseLong(user_id);
    			Utilisateur user = UtilisateurService.findById(uid);
    	    	MembresServiceBdd membreService = MembresServiceBddService.findById(uid);
    	        
    	        if(user != null)
    	        {
    	        	if(!model.containsAttribute("editForm"))
    	        	{
    	        		UserForm editForm = new UserForm();
    	        		editForm.setJoursCongesRest((int) user.getJoursCongesRestants());
        	        	editForm.setNom(user.getNom());
        	        	editForm.setNumTel(user.getNumeroTel());
        	        	editForm.setPrenom(user.getPrenom());
        	        	editForm.setRole_id(membreService.getRoleId());
        	        	editForm.setService_id(membreService.getServiceId());
        	        	editForm.setIsAdmin(membreService.getIsAdmin());
        	        	editForm.setUid(uid);
        	        	editForm.setHeurestravail(Long.toString(user.getHeuresContrat()).toString());
        	        	editForm.setRtt(user.getRtt());
        	        	editForm.setMdp("---");
        	        	model.addAttribute("editForm", editForm);
    	        	}
    	        }		
    		} catch (NumberFormatException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	// Ajout du form pour l'ajout de personne
    	model.addAttribute("userForm", userForm);
    	/////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		NotifClasse nbCongesEtRemb = new NotifClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService,NotifService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
        return "admin/users/showUsers";
    }
    
    @PostMapping("")
    public String roleChanged(@Valid UserForm editForm, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
    	
    	if(result.hasErrors()) {
    		redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editForm", result);
            redirectAttributes.addFlashAttribute("editForm", editForm);
            redirectAttributes.addFlashAttribute("success", false);
    	}
    	else {
    		
    		Utilisateur user = new Utilisateur();
        	user.setUID(editForm.getUid());
        	user.setPrenom(editForm.getPrenom());
        	user.setNom(editForm.getNom());
        	user.setNumeroTel(editForm.getNumTel());
        	user.setHeuresContrat(Integer.parseInt(editForm.getHeurestravail()));
        	user.setJoursCongesRestants(editForm.getJoursCongesRest());
        	user.setRtt(editForm.getRtt());
        	
        	MembresServiceBdd membre = new MembresServiceBdd();
        	membre.setUid(editForm.getUid());
        	membre.setRoleId(editForm.getRole_id());
        	membre.setServiceId(editForm.getService_id());
        	membre.setIsAdmin(editForm.getIsAdmin());
        	
        	// Met a jour l'utilisateur
        	utilisateurRepository.save(user);
        	membresServiceBddRepository.save(membre);
        	redirectAttributes.addFlashAttribute("success", true);
    	}
    	
        return "redirect:" + root + "?id=" + editForm.getUid();
    }
    
    //TODO utile ????
    /*
    @GetMapping("/create")
    public String showForm(UserForm userForm, Model model,Principal principal) {
    	model = addServiceListIntoModel(model);
    	/////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		SachaClasse nbCongesEtRemb = new SachaClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService,NotifService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
        return "form";
    }*/

    @PostMapping("/create")
    public @ResponseBody Map<String, String> checkPersonInfo(@Valid UserForm userForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    	
        Map<String, String> errors = new HashMap<String, String>();
        
        Utilisateur u = UtilisateurService.findPrenomNom(userForm.getNom(),userForm.getPrenom());
        //Utilisateur avec ce nom et prenom existe deja
        if (u != null) { 
        	errors.put("#user_exists_error", "Cet utilisateur existe déja");
        }
        
        // On verifie s'il y a des erreurs
    	if (bindingResult.hasErrors()) {
        	 List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        	 for(FieldError f : fieldErrors) {
        		 errors.put("#" + f.getField() + "_error", f.getDefaultMessage());
        	 }
    	}
    	
    	if(errors.isEmpty())
        {
        	// Sinon on met le user dans la bd.
            // Utilisateur
            Utilisateur user = new Utilisateur();
            
            user.setJoursCongesRestants(35);//TODO par défaut 35 jours ?
            user.setNom(userForm.getNom());
            user.setNumeroTel(userForm.getNumTel());
            user.setPrenom(userForm.getPrenom());
            
            Long maxId = UtilisateurService.getMaxId();//On recupere ID le plus haut de la table.
            if(maxId != null)
            	user.setUID((long) (maxId + 1));
            else
            	user.setUID((long) 1);
            user.setRtt(0);
            user.setHeuresContrat(Integer.parseInt(userForm.getHeurestravail()));
            UtilisateurService.addUser(user);
            
            // Indentifiant / Mot de passe
            root.bdd.appuser.AppUser appUser = new root.bdd.appuser.AppUser();
            appUser.setUser_id((long) (maxId + 1));
    		appUser.setEncrypted_password(EncrytedPasswordUtils.encryptePassword(userForm.getMdp()));
    		appUser.setUser_name(userForm.getPrenom() + "." + userForm.getNom());
    		AppUserService.addAppUser(appUser);
            
    		// Etablissement du role
    		MembresServiceBdd membre = new MembresServiceBdd();
    		membre.setUid(user.getUID());
    		membre.setRoleId(userForm.getRole_id());
    		membre.setServiceId(userForm.getService_id());
    		membre.setIsAdmin(userForm.getIsAdmin());
            MembresServiceBddService.addMembreService(membre);
        }
        
        return errors;
    }
    
    @RequestMapping("/delete")
    public String getMessage(@RequestParam(value="id", required=false) String idStr, Model model,UserForm userForm,Principal principal) 
    {
    	if(idStr != null) try
		{
			long id = Long.parseLong(idStr);
			Utilisateur user = UtilisateurService.findById(id);
			
			if(user != null) {
				List<Mission> missions = MissionService.findAll();
				boolean estRespoMission = false;
				for (int i = 0;i< missions.size();i++)
				{
					if (missions.get(i).getResponsable_id() == id)
					{
						estRespoMission = true;
						break;
					}
				}
				if (estRespoMission)
				{
					return "redirect:" + root;
				}
				
				MembresMissionService.deleteMembresMission(id);
				CongesService.deleteConges(id);
				AppUserService.deleteAppUser(id);
				////////////////////////////////////////////

				RemboursementService.deleteRembUid(id);
				NoteService.deleteNoteUid(id);
				UtilisateurService.deleteUser(id);
				
				System.out.println("DELETED " + id);
				
				List<Utilisateur> cs = UtilisateurService.findAll();
		    	model.addAttribute("listUsers", cs);
		    	String prenomnom = principal.getName();
		    	String[] names = prenomnom.split("\\.");
		    	model.addAttribute("notAdmin",UtilisateurService.findPrenomNom(names[1], names[0]).getUID());
				return "redirect:" + root;
			}
		}
    	catch(NumberFormatException e) {
    		e.printStackTrace();
    	}
    	
    	return "forward:/notFound";
    }
}
