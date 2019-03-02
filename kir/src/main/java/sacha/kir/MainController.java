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
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sacha.kir.bdd.approle.AppRole;
import sacha.kir.bdd.approle.InterfaceAppRoleService;
import sacha.kir.bdd.appuser.InterfaceAppUserService;
import sacha.kir.bdd.conges.Conges;
import sacha.kir.bdd.conges.InterfaceCongesService;
import sacha.kir.bdd.justificatif.InterfaceJustificatifService;
import sacha.kir.bdd.membresmission.InterfaceMembresMissionService;
import sacha.kir.bdd.membresservice.InterfaceMembresServiceBddService;
import sacha.kir.bdd.membresservice.MembresServiceBdd;
import sacha.kir.bdd.membresservice.MembresServiceBddRepository;
import sacha.kir.bdd.membresservice.Role;
import sacha.kir.bdd.mission.InterfaceMissionService;
import sacha.kir.bdd.mission.Mission;
import sacha.kir.bdd.note.InterfaceNoteService;
import sacha.kir.bdd.note.Note;
import sacha.kir.bdd.notif.InterfaceNotifService;
import sacha.kir.bdd.notif.Notif;
import sacha.kir.bdd.notif.NotifService;
import sacha.kir.bdd.remboursement.InterfaceRemboursementService;
import sacha.kir.bdd.remboursement.Remboursement;
import sacha.kir.bdd.remboursement.Statut;
import sacha.kir.bdd.remboursementsnote.InterfaceRemboursementsNoteService;
import sacha.kir.bdd.services.InterfaceServiceBddService;
import sacha.kir.bdd.services.ServicesFixes;
import sacha.kir.bdd.userrole.InterfaceUserRoleService;
import sacha.kir.bdd.userrole.UserRole;
import sacha.kir.bdd.utilisateur.InterfaceUtilisateurService;
import sacha.kir.bdd.utilisateur.Utilisateur;
import sacha.kir.bdd.utilisateur.UtilisateurRepository;
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
	@Autowired
	InterfaceNotifService NotifService;
	
	/** Repositories **/
	@Autowired
	UtilisateurRepository utilisateurRepository;
	@Autowired
	MembresServiceBddRepository membresServiceBddRepository;
	/******************/
	
	enum TypesConges {
		CongePaye(0, "Congé payé"),
	    RTT(1,"RTT");

		public int id;
		public String name;

	    private TypesConges (int id, String name){
		    	this.id = id;    
		    	this.name = name;
	    }
	};
	
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
    @RequestMapping(value="/login-Error", method = RequestMethod.GET)
    public String welcomePageWithError(Model model, Principal principal) {
    	model.addAttribute("loginError", true);
    	return "Login";
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
      /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
    		SachaClasse nbCongesEtRemb = new SachaClasse();
    		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService);
    		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
        }
 
        return "403Page";
    }
    
    // TODO UPDATE FORM PAGE TO NEW SYSTEM
    @GetMapping("/adminAdd")
    public String showForm(UserForm userForm, Model model,Principal principal) {
    	model = addServiceListIntoModel(model);
  /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		SachaClasse nbCongesEtRemb = new SachaClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
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
        	System.out.println("Cet user existe deja !");
        	model.addAttribute("userExists", "Cet utilisateur existe deja");
        	return "redirect:/Accueil";
        }
        
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
    	String[] names = principal.getName().split("\\.");
    	Long uID = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	List<Utilisateur> cs = UtilisateurService.findAll();
    	for(int i=0;i<cs.size();i++) {
    		if(cs.get(i).getUID()==uID) {
    			UserForm u = new UserForm();
    			u.setJoursCongesRest( cs.get(i).getJoursCongesRestants());
    			u.setRtt(cs.get(i).getRtt());
    			model.addAttribute("user",u);
    		}
    	}
    	model.addAttribute("types", TypesConges.values());
  /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		SachaClasse nbCongesEtRemb = new SachaClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
    	
    	return "ajouterConge";
    }
    @PostMapping("/AjouterConge")
    public String submitC(@Valid CongeForm congeForm, BindingResult bindingResult, Model model, Principal principal) {
    	
        System.out.println(congeForm.getDateDebut());
        System.out.println(congeForm.getDateFin());
        String[] dateDavecTime = congeForm.getDateDebut().split("T");
        String[] dateFavecTime = congeForm.getDateFin().split("T");
        String[] dateD = dateDavecTime[0].split("-");
        String[] dateF = dateFavecTime[0].split("-");
    	String[] names = principal.getName().split("\\.");
    	Long uID = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	String dateDebut = dateD[2]+"/"+dateD[1]+"/"+dateD[0]+" "+dateDavecTime[1];
    	String dateFin = dateF[2]+"/"+dateF[1]+"/"+dateF[0]+" "+dateFavecTime[1];
    	System.out.println("Type: "+congeForm.getType());
    	boolean rtt;
    	if(congeForm.getType().equals("0"))
    		rtt=false;
    	else
    		rtt=true;
    	
    	CongesService.addConges(dateDebut, dateFin, uID,rtt);
        
        return "redirect:/Accueil";
    }
    @GetMapping("/Calendrier")
    public String getCalendrier(Model model,Principal principal) throws Exception {
    	// On selectionne tout les uid
    	List<Utilisateur> cs = UtilisateurService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
    	model.addAttribute("listUsers", cs);
    	String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	
    	long serviceDuValidateur = MembresServiceBddService.findById(UtilisateurService.findPrenomNom(names[1], names[0]).getUID()).getServiceId();
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
    					if (c.getValidationchefservice().equals(Statut.valide.statut()) && c.getValidationrh().equals(Statut.valide.statut()))
    					{
    						if (serviceDuValidateur == ServicesFixes.ressourcesHumaines.getServiceId())//La RH voit les conges de tt le monde
    						{
            					aujourdhuiConges.add(c);
    						}
    						else
    						{
    					    	long serviceDuDemandeur = MembresServiceBddService.findById(c.getUid()).getServiceId();
    					    	if (serviceDuValidateur == serviceDuDemandeur)
    					    	{
                					aujourdhuiConges.add(c);
    					    	}
    						}
    					}
    					for (Utilisateur utilisateur : cs) {
							if(utilisateur.getUID()==c.getUid()) {
								aujourdhuiU.add(utilisateur);
							}
						}
    				}
    			}
    		}
    	}
    	
    	Utilisateur validateur = UtilisateurService.findPrenomNom(names[1], names[0]);
		MembresServiceBdd validateurRoles = MembresServiceBddService.findById(validateur.getUID());
		long myServiceId = validateurRoles.getServiceId();
		long uidConges;
		
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
				}
			}
			c2.add(x);
			
			
			if (validateurRoles.getRoleId() == Role.chefDeService.getRoleId())
			{
				uidConges = conges.get(j).getUid();
				if (MembresServiceBddService.findById(uidConges).getServiceId() == myServiceId)//Du meme service
				{
					if (conges.get(j).getValidationchefdeservice().equals(Statut.enAttente.statut()))//Demandes en attente
					{
						if (conges.get(j).getUid() != validateur.getUID())//Check si pas autovalidation
						{
							demandesConges.add(x);
						}
					}
				}
				else if (myServiceId == ServicesFixes.ressourcesHumaines.getServiceId() && conges.get(j).getValidationchefdeservice().equals(Statut.valide.statut()) && conges.get(j).getValidationrh().equals(Statut.enAttente.statut()))
	    		//Service different, mais la demande est validé par leur chef de service
				{
					if (conges.get(j).getUid() != validateur.getUID())//Check si pas autovalidation
					{
						demandesConges.add(x);
					}
				}
			}
			else
			{
				throw new Exception("Le validateur de conges est n'est pas un chef de service !");
			}
			
			
		}
    	
    	model.addAttribute("demandesConges",demandesConges);
    	model.addAttribute("toutConges",c2);
    	model.addAttribute("listConges",aujourdhuiConges);
    	model.addAttribute("utilisateurs",aujourdhuiU);
    	
  /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		SachaClasse nbCongesEtRemb = new SachaClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
		
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
				if(conge.isRtt()) 
					c.setType("Rtt");
				else
					c.setType("Congé payé");
				mesConges.add(c);
    		}
    	}
    	model.addAttribute("demandesConges",mesConges);
  /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		SachaClasse nbCongesEtRemb = new SachaClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
    	
    	return "gererConges";
    }
    @PostMapping("/GererConges")
    public String editconge(@Valid CongeForm congeForm,BindingResult bindingResult,Model model, Principal principal) {
    	
    	String[] dateDavecTime = congeForm.getDateDebut().split("T");
        String[] dateFavecTime = congeForm.getDateFin().split("T");
    	String[] dateD = dateDavecTime[0].split("-");
        String[] dateF = dateFavecTime[0].split("-");
        String dateDebut = dateD[2]+"/"+dateD[1]+"/"+dateD[0]+" "+dateDavecTime[1];
    	String dateFin = dateF[2]+"/"+dateF[1]+"/"+dateF[0]+" "+dateFavecTime[1];
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

    @GetMapping("/addConges")
    public String addConges(CongeForm congeForm,Principal principal,Model model) {
  /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		SachaClasse nbCongesEtRemb = new SachaClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
        return "addCongesPage";
    }
    
    @PostMapping("/addConges")
    public String submitConges(@Valid CongeForm congeForm, BindingResult bindingResult, Model model, Principal principal) {
    	
        System.out.println(congeForm.getDateDebut());
        System.out.println(congeForm.getDateFin());

    	String[] names = principal.getName().split("\\.");
    	Long uID = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	
    	CongesService.addConges(congeForm.getDateDebut(), congeForm.getDateFin(), uID, false);
        
        return "redirect:/Accueil";
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
	        moisPrecedent = (moisInt < 10 ? "0" + moisInt : moisInt) + "-" + yearInt;
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
        model.addAttribute("joursConges","Jour(s) de congé(s) payé(s) restant(s) : " + UtilisateurService.findPrenomNom(names[1], names[0]).getJoursCongesRestants());
        model.addAttribute("rtt","Jour(s) de RTT restant(s) : " + UtilisateurService.findPrenomNom(names[1], names[0]).getRtt());
        /**** DERNIERES DEMANDES DE CONGES ****/
        
        //Nombre de demandes de remboursement
        SachaClasse SachaEstClasse = new SachaClasse();
        int nbRemb = SachaEstClasse.getNbRemb(principal,MembresServiceBddService,RemboursementService,UtilisateurService);
        model.addAttribute("nbRemb", nbRemb);

		/*** DERNIERES NOTIFS ***/
		List<Notif> notifs = NotifService.getAllByIdDesc(userId);
		model.addAttribute("notifs", notifs);
		/*** DERNIERES NOTIFS ***/
        
		boolean IsChef = SachaEstClasse.isChef(principal, UtilisateurService, MembresServiceBddService);
		if (IsChef)
		{
			model.addAttribute("IsChef", IsChef);
		}

		int nbConges = SachaEstClasse.getNbConges(CongesService, UtilisateurService, MembresServiceBddService, principal);
		model.addAttribute("nbConges",nbConges);
		
        return "welcomePage-Thibaut";
    }
    
    @RequestMapping("/Notifications")
    public String nofitications(Model model, Principal principal)
    {
    	String[] names = principal.getName().split("\\.");
    	List<Notif> notifs = NotifService.getAllByIdDesc(UtilisateurService.findPrenomNom(names[1], names[0]).getUID());
    	model.addAttribute("notifs", notifs);
  /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		SachaClasse nbCongesEtRemb = new SachaClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
    	return "notifications";
    }
    
    @RequestMapping("/adminShow")
    public String adminShow(@RequestParam(value = "user_id", required = false) String user_id, Model model, Principal principal, UserForm userForm)
    {
    	// Informations pour les services disponibles (liste des services)
    	model = addServiceListIntoModel(model);
    	
    	// Listage de tous les utilisateurs
    	List<Utilisateur> cs = (List<Utilisateur>) utilisateurRepository.findAll();
    	String[] names = principal.getName().split("\\.");
    	model.addAttribute("listUsers", cs);
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
  		SachaClasse nbCongesEtRemb = new SachaClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
        return "showUsers";
    }
    
    @PostMapping("/adminShow")
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
    	
        return "redirect:/adminShow?user_id=" + editForm.getUid();
    }
}