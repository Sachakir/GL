package sacha.kir;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sacha.kir.bdd.approle.AppRole;
import sacha.kir.bdd.approle.InterfaceAppRoleService;
import sacha.kir.bdd.appuser.InterfaceAppUserService;
import sacha.kir.bdd.conges.Conges;
import sacha.kir.bdd.conges.InterfaceCongesService;
import sacha.kir.bdd.justificatif.InterfaceJustificatifService;
import sacha.kir.bdd.membresmission.InterfaceMembresMissionService;
import sacha.kir.bdd.membresservice.InterfaceMembresServiceBddService;
import sacha.kir.bdd.membresservice.MembresServiceBdd;
import sacha.kir.bdd.membresservice.Role;
import sacha.kir.bdd.mission.InterfaceMissionService;
import sacha.kir.bdd.mission.Mission;
import sacha.kir.bdd.note.InterfaceNoteService;
import sacha.kir.bdd.note.Note;
import sacha.kir.bdd.remboursement.InterfaceRemboursementService;
import sacha.kir.bdd.remboursement.Remboursement;
import sacha.kir.bdd.remboursementsnote.InterfaceRemboursementsNoteService;
import sacha.kir.bdd.services.InterfaceServiceBddService;
import sacha.kir.bdd.services.ServicesFixes;
import sacha.kir.bdd.userrole.InterfaceUserRoleService;
import sacha.kir.bdd.userrole.UserRole;
import sacha.kir.bdd.utilisateur.InterfaceUtilisateurService;
import sacha.kir.bdd.utilisateur.Utilisateur;
import sacha.kir.form.CongeForm;
import sacha.kir.form.CongesV2;
import sacha.kir.form.UserForm;
 
@Controller
public class MainController {
 
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
    InterfaceAppRoleService AppRoleService;
	@Autowired
	InterfaceUserRoleService UserRoleService;
	@Autowired
	InterfaceServiceBddService ServiceBddService;
	@Autowired
	InterfaceMembresServiceBddService MembresServiceBddService;
	
	// Methodes communes aux pages
	
	@ModelAttribute("username")
	public String getUsername(Principal principal) {
		if(principal != null)
		{
			String[] name = principal.getName().split("\\.");
			return name[0] + " " + name[1];
		}
		else return "";
	}
	
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
	
	//////////////////////////////
	
    @RequestMapping(value = {"/", "/login"} , method = RequestMethod.GET)
    public String welcomePage(Model model, Principal principal) {
        if(principal != null)
        {
        	return "redirect:/Accueil";
        }
        else return "Login";   
    }
 
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
         
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
       
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
         
        return "adminPage";
    }
 
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
 
        // After user login successfully.
        String userName = principal.getName();
 
        System.out.println("User Name: " + userName);
 
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
 
        return "userInfoPage";
    }
 
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {
 
        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
            String userInfo = WebUtils.toString(loginedUser);
 
            model.addAttribute("userInfo", userInfo);
 
            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);
 
        }
 
        return "403Page";
    }
    
    // TODO UPDATE FORM PAGE TO NEW SYSTEM
    @GetMapping("/adminAdd")
    public String showForm(UserForm userForm, Model model) {
    	model = addServiceListIntoModel(model);
        return "form";
    }

    @PostMapping("/adminAdd")
    public String checkPersonInfo(@Valid UserForm userForm, BindingResult bindingResult,Model model) {
    	
        if (bindingResult.hasErrors()) {
            return "redirect:/Accueil";
        }
        
        System.out.println(Role.getRoleNameById(userForm.getRole_id()));
        
        Utilisateur u = UtilisateurService.findPrenomNom(userForm.getNom(),userForm.getPrenom());
        if (u != null)//Utilisateur avec ce nom et prenom existe deja
        {
        	model.addAttribute("userExists", "Cet utilisateur existe deja");
        	return "redirect:/Accueil";
        }
        
        // Sinon on met le user dans la bd.
        // Utilisateur
        Utilisateur user = new Utilisateur();
        user.setDateNaissance(userForm.getDateNaissance());
        user.setJoursCongesRestants(userForm.getJoursCongesRest());
        user.setNom(userForm.getNom());
        user.setNumeroTel(userForm.getNumTel());
        user.setPrenom(userForm.getPrenom());
        
        Long maxId = UtilisateurService.getMaxId();//On recupere ID le plus haut de la table.
        if(maxId != null)
        	user.setUID((long) (maxId + 1));
        else
        	user.setUID((long) 1);
        UtilisateurService.addUser(user);
        
        // Indentifiant / Mot de passe
        sacha.kir.bdd.appuser.AppUser appUser = new sacha.kir.bdd.appuser.AppUser();
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

        return "welcomePage-Thibaut";
    }
    
    @GetMapping("/AjouterConge")
    public String ajouterc(Model model, Principal principal, CongeForm congeForm) {
    	
    	return "ajouterConge";
    }
    @PostMapping("/AjouterConge")
    public String submitC(@Valid CongeForm congeForm, BindingResult bindingResult, Model model, Principal principal) {
    	
        System.out.println(congeForm.getDateDebut());
        System.out.println(congeForm.getDateFin());
        String[] dateD = congeForm.getDateDebut().split("-");
        String[] dateF = congeForm.getDateFin().split("-");
    	String[] names = principal.getName().split("\\.");
    	Long uID = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	String dateDebut = dateD[2]+"/"+dateD[1]+"/"+dateD[0];
    	String dateFin = dateF[2]+"/"+dateF[1]+"/"+dateF[0];
    	CongesService.addConges(dateDebut, dateFin, uID);
        
        return "redirect:/Accueil";
    }
    @GetMapping("/Calendrier")
    public String getCalendrier(Model model,Principal principal) {
    	// On selectionne tout les uid
    	List<Utilisateur> cs = UtilisateurService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
    	model.addAttribute("listUsers", cs);
    	String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	
    	List<Conges> conges = CongesService.findAll();
    	List<CongesV2> aujourdhuiConges = new ArrayList<CongesV2>();
    	List<CongesV2> c2 = new ArrayList<CongesV2>();
    	List<CongesV2> demandesConges = new ArrayList<CongesV2>();
    	List<Utilisateur> aujourdhuiU = new ArrayList<Utilisateur>();
    	model.addAttribute("notAdmin",UtilisateurService.findPrenomNom(names[1], names[0]).getUID());
    	SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss z");  
    	Date d = new Date();
    	String today = formatter.format(d);
    	for(int i=0;i<conges.size();i++) {
    		int jourDebut = Integer.parseInt(conges.get(i).getDatedebut().substring(0,2));
    		int jourFin = Integer.parseInt(conges.get(i).getDatefin().substring(0,2));
    		int moisDebut = Integer.parseInt(conges.get(i).getDatedebut().substring(3,5));
    		int moisFin = Integer.parseInt(conges.get(i).getDatefin().substring(3,5));
    		int anneeDebut = Integer.parseInt(conges.get(i).getDatedebut().substring(6,10));
    		int anneeFin = Integer.parseInt(conges.get(i).getDatefin().substring(6,10));
    		int jour = Integer.parseInt(today.substring(0,2));
    		int mois = Integer.parseInt(today.substring(3,5));
    		int annee = Integer.parseInt(today.substring(6,10));
    		if(anneeDebut<=annee && anneeFin>=annee) {
    			if(moisDebut <=mois && moisFin>=mois) {
    				if(jourDebut <=jour && jourFin >=jour) {
    					CongesV2 c = new CongesV2();
    					Conges conge = conges.get(i);
    					c.setCongesid(conge.getCongesid());
    					c.setDatedebut(conge.getDatedebut());
    					c.setDatefin(conge.getDatefin());
    					c.setUid(conge.getUid());
    					c.setValidationrh(conge.getValidationrh());
    					c.setValidationchefservice(conge.getValidationchefdeservice());
    					for(int k=0;k<cs.size();k++) {
    						
    						if(cs.get(k).getUID()==c.getUid()) {
    							
    							c.setPrenomNom(cs.get(k).getPrenom()+" "+cs.get(k).getNom());
    							System.out.println(c.getPrenomNom());
    						}
    					}
    					aujourdhuiConges.add(c);
    					for (Utilisateur utilisateur : cs) {
							if(utilisateur.getUID()==c.getUid()) {
								aujourdhuiU.add(utilisateur);
							}
						}
    				}
    			}
    		}
    	}
    	for(int j=0;j<conges.size();j++) {
			CongesV2 x = new CongesV2();
			Conges conge = conges.get(j);
			x.setCongesid(conge.getCongesid());
			x.setDatedebut(conge.getDatedebut());
			x.setDatefin(conge.getDatefin());
			x.setUid(conge.getUid());
			x.setValidationrh(conge.getValidationrh());
			x.setValidationchefservice(conge.getValidationchefdeservice());
			for(int k=0;k<cs.size();k++) {
				
				if(cs.get(k).getUID()==x.getUid()) {
					
					x.setPrenomNom(cs.get(k).getPrenom()+" "+cs.get(k).getNom());
					System.out.println("AHHHHHHHH " + x.getPrenomNom());
				}
			}
			c2.add(x);
			if(x.getValidationchefservice().equals("EnAttente") || x.getValidationrh().contentEquals("EnAttente")) {
				demandesConges.add(x);
			}
			
		}
    	System.out.println(demandesConges.get(0).getValidationchefservice());
    	model.addAttribute("demandesConges",demandesConges);
    	model.addAttribute("toutConges",c2);
    	model.addAttribute("listConges",aujourdhuiConges);
    	model.addAttribute("utilisateurs",aujourdhuiU);
    	
		
        return "calendrier";
    }
    @GetMapping("/GererConges")
    public String gererc(CongeForm congeForm, BindingResult bindingResult,Model model, Principal principal) {
    	String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	long uid = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	model.addAttribute("notAdmin",uid);
    	List<Conges> conges = CongesService.findAll();
    	List<CongesV2> mesConges = new ArrayList<CongesV2>();
    	for(int i=0;i<conges.size();i++) {
    		if(conges.get(i).getUid()==uid) {
    			CongesV2 c = new CongesV2();
				Conges conge = conges.get(i);
				c.setCongesid(conge.getCongesid());
				c.setDatedebut(conge.getDatedebut());
				c.setDatefin(conge.getDatefin());
				c.setUid(conge.getUid());
				c.setValidationrh(conge.getValidationrh());
				c.setValidationchefservice(conge.getValidationchefdeservice());
				c.setPrenomNom(prenomnom);
				mesConges.add(c);
    		}
    	}
    	model.addAttribute("demandesConges",mesConges);
    	return "gererConges";
    }
    @PostMapping("/GererConges")
    public String editconge(@Valid CongeForm congeForm,BindingResult bindingResult,Model model, Principal principal) {
    	
    	String[] dateD = congeForm.getDateDebut().split("-");
        String[] dateF = congeForm.getDateFin().split("-");
        String dateDebut = dateD[2]+"/"+dateD[1]+"/"+dateD[0];
    	String dateFin = dateF[2]+"/"+dateF[1]+"/"+dateF[0];
    	long congeID = congeForm.getCongesid();
    	System.out.println("\nDate de début: "+dateDebut + "\n Date de fin: "+dateFin+"\nCongeID: "+congeID);
    	CongesService.updateConges(congeID, dateDebut, dateFin);
    	
    	String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	long uid = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	model.addAttribute("notAdmin",uid);
    	List<Conges> conges = CongesService.findAll();
    	List<CongesV2> mesConges = new ArrayList<CongesV2>();
    	for(int i=0;i<conges.size();i++) {
    		if(conges.get(i).getUid()==uid) {
    			CongesV2 c = new CongesV2();
				Conges conge = conges.get(i);
				c.setCongesid(conge.getCongesid());
				c.setDatedebut(conge.getDatedebut());
				c.setDatefin(conge.getDatefin());
				c.setUid(conge.getUid());
				c.setValidationrh(conge.getValidationrh());
				c.setValidationchefservice(conge.getValidationchefdeservice());
				c.setPrenomNom(prenomnom);
				mesConges.add(c);
    		}
    	}
    	model.addAttribute("demandesConges",mesConges);
    	return "redirect:/GererConges";
    }
    @RequestMapping(path="/ValidationC/{id}")
    public String ValidationConges(@PathVariable("id") long demandeId,Principal principal)
    {
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	
    	MembresServiceBdd membre = MembresServiceBddService.findById(ut.getUID());
    	if(membre.getRoleId() == Role.chefDeService.getRoleId()) {
    		CongesService.updateChefState(demandeId, "Validé");
    	}
    	else if (membre.getServiceId() == ServicesFixes.ressourcesHumaines.getServiceId()) {
    		CongesService.updateRHState(demandeId, "Validé");
    	}
    	else {
    		System.out.println("Vous ne pouvez pas valider de demandes de congés (Mauvais service ou role) ");
    	}
    	
		return "redirect:/Calendrier";
    }
    @RequestMapping(path="/RefusC/{id}")
    public String RefusConges(@PathVariable("id") long demandeId,Principal principal)
    {
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	
    	MembresServiceBdd membre = MembresServiceBddService.findById(ut.getUID());
    	if(membre.getRoleId() == Role.chefDeService.getRoleId()) {
    		CongesService.updateChefState(demandeId, "Refusé");
    	}
    	else if (membre.getServiceId() == ServicesFixes.ressourcesHumaines.getServiceId()) {
    		CongesService.updateRHState(demandeId, "Refusé");
    	}
    	else {
    		System.out.println("Vous ne pouvez pas valider de demandes de congés (Mauvais service ou role) ");
    	}
    	
		return "redirect:/Calendrier";
    }
    @GetMapping("/addConges")
    public String addConges(CongeForm congeForm) {
        return "addCongesPage";
    }
    
    @PostMapping("/addConges")
    public String submitConges(@Valid CongeForm congeForm, BindingResult bindingResult, Model model, Principal principal) {
    	
        System.out.println(congeForm.getDateDebut());
        System.out.println(congeForm.getDateFin());

    	String[] names = principal.getName().split("\\.");
    	Long uID = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	
    	CongesService.addConges(congeForm.getDateDebut(), congeForm.getDateFin(), uID);
        
        return "redirect:/Accueil";
    }
    
    @RequestMapping("/addAppUser")
    public String addAppUser(Model model)
    {
    	//AppUserService.addAppUser();

    	List<sacha.kir.bdd.appuser.AppUser> cs = AppUserService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @RequestMapping("/addAppRole")
    public String addAppRole(Model model)
    {
    	AppRoleService.addAppRole();

    	List<AppRole> cs = AppRoleService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @RequestMapping("/addUserRole")
    public String addUserRole(Model model)
    {
    	//UserRoleService.addUserRole();

    	List<UserRole> cs = UserRoleService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @RequestMapping("/Accueil")
    public String accueil(Model model,Principal principal)
    {
    	String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	model.addAttribute("WelcomeMsg", "Bienvenue " + names[0]);
    	Long userId = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	MembresServiceBdd membre = MembresServiceBddService.findById(userId);
    	model.addAttribute("isAdmin", membre.getIsAdmin());
    	
    	/**** NOTIFICATION ***********/
        LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfMonth() >= 14)
        {   
	        Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
	        long uid = ut.getUID();
	        String moisPrecedent = "";
	        int moisInt = localDate.getMonthValue();
        	int yearInt = localDate.getYear();
	        if(localDate.getMonthValue() == 1)
	        {
	        	moisInt = 12;
	        	yearInt--;
	        }
	        else 
	        {
	        	moisInt--;
	        }
	        moisPrecedent = (moisInt < 10 ? "0" + moisInt : moisInt) + "/" + yearInt;
	        Note notePrecedente = NoteService.findNoteByMonthAndUID(moisPrecedent, uid);
	        
	        if (notePrecedente == null)
	        {
	        	model.addAttribute("NotifNote", "Vous n\'avez pas de note de frais pour le mois precedent !");
	        }
        }
        /**** NOTIFICATION ***********/
        
        /**** DERNIERES DEMANDES DE CONGES ****/
        List<Remboursement> recentDemandesRemboursement = RemboursementService.getAllByIdDesc(userId, 10);
        Map<Long, String> missionNames = new HashMap<Long, String>();
        Map<Long, String> notesAssociees = new HashMap<Long, String>();
        
        for(Remboursement r : recentDemandesRemboursement) {
        	Long mission_id = r.getMission_id();
        	if(!missionNames.containsKey(mission_id)) {
        		Mission m = MissionService.findMissionById(mission_id);
        		missionNames.put(mission_id, m.getTitre());
        	}
        	
        	Long note_id = RemboursementsNoteService.findNoteIdByDemandeId(r.getDemande_id());
        	String mois = NoteService.findById(note_id).getMois();
        	mois = mois.substring(0, 2) + "-" + mois.substring(3);
        	notesAssociees.put(r.getDemande_id(), mois);
        }
        
        model.addAttribute("recentDemandesRemboursement", recentDemandesRemboursement);
        model.addAttribute("missionNames", missionNames);
        model.addAttribute("notesAssociees", notesAssociees);
        /**** DERNIERES DEMANDES DE CONGES ****/

        return "welcomePage-Thibaut";
    }
    
    @RequestMapping("/adminShow")
    public String adminShow(Model model,Principal principal, UserForm userForm)
    {
    	// Informations pour les services disponibles (liste des services)
    	model = addServiceListIntoModel(model);
    	
    	List<Utilisateur> cs = UtilisateurService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
    	model.addAttribute("listUsers", cs);
    	String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	model.addAttribute("WelcomeMsg", "Bienvenue " + names[0]);
    	
    	model.addAttribute("notAdmin",UtilisateurService.findPrenomNom(names[1], names[0]).getUID());
    	
    	
        return "showUsers";
    }
    
    @RequestMapping(path="/showUserDetails/{id}")
    public String getMessage(@PathVariable("id") long id, Model model, UserForm userForm) 
    {
    	model = addServiceListIntoModel(model);
    	
    	Utilisateur user = UtilisateurService.findById(id);
    	MembresServiceBdd membreService = MembresServiceBddService.findById(id);
        
        if(user != null)
        {
        	userForm.setDateNaissance(user.getDateNaissance());
        	userForm.setJoursCongesRest((int) user.getJoursCongesRestants());
        	userForm.setNom(user.getNom());
        	userForm.setNumTel(user.getNumeroTel());
        	userForm.setPrenom(user.getPrenom());
        	userForm.setRole_id(membreService.getRoleId());
        	userForm.setService_id(membreService.getServiceId());
        	userForm.setIsAdmin(membreService.getIsAdmin());
        	userForm.setUid(id);
        }
        
        model.addAttribute("user", userForm);
        return "showUserDetails";
    }
    
    @GetMapping("/roleChanged")
    public String roleChanged(UserForm userForm,Model model) {
    	if (userForm.getRole_id() == 0 || userForm.getService_id() == 0)
    	{
    		model.addAttribute("change", "non change");
    		return "roleChanged";
    	}
    	
    	long uid = userForm.getUid();
    	MembresServiceBddService.updateServiceById(uid, userForm.getService_id());
    	MembresServiceBddService.updateRoleById(uid, userForm.getRole_id());
    	MembresServiceBddService.updateAdminStatusById(uid, userForm.getIsAdmin());
    	
    	model.addAttribute("change", "change");
    	
        return "redirect:/showUserDetails/" + userForm.getUid();
    }
}