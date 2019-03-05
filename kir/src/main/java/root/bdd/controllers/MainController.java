package root.bdd.controllers;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import root.EncrytedPasswordUtils;
import root.NotifClasse;
import root.WebUtils;
import root.bdd.appuser.InterfaceAppUserService;
import root.bdd.conges.Conges;
import root.bdd.conges.InterfaceCongesService;
import root.bdd.conges.TypesConges;
import root.bdd.justificatif.InterfaceJustificatifService;
import root.bdd.membresmission.InterfaceMembresMissionService;
import root.bdd.membresservice.InterfaceMembresServiceBddService;
import root.bdd.membresservice.MembresServiceBdd;
import root.bdd.membresservice.MembresServiceBddRepository;
import root.bdd.membresservice.Role;
import root.bdd.mission.InterfaceMissionService;
import root.bdd.mission.Mission;
import root.bdd.note.InterfaceNoteService;
import root.bdd.note.Note;
import root.bdd.notif.InterfaceNotifService;
import root.bdd.notif.Notif;
import root.bdd.remboursement.InterfaceRemboursementService;
import root.bdd.remboursement.Remboursement;
import root.bdd.remboursement.Statut;
import root.bdd.remboursementsnote.InterfaceRemboursementsNoteService;
import root.bdd.remboursementsnote.RemboursementsNote;
import root.bdd.services.InterfaceServiceBddService;
import root.bdd.services.ServicesFixes;
import root.bdd.utilisateur.InterfaceUtilisateurService;
import root.bdd.utilisateur.Utilisateur;
import root.bdd.utilisateur.UtilisateurRepository;
import root.forms.conges.CongeForm;
import root.forms.conges.CongesV2;
import root.forms.utilisateur.UserForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
public class MainController {
 
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
    		NotifClasse nbCongesEtRemb = new NotifClasse();
    		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService,NotifService);
    		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
        }
 
        return "403Page";
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
  		NotifClasse nbCongesEtRemb = new NotifClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService,NotifService);
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
    	if (congeForm.getType() == null)
    	{
    		return "redirect:/AjouterConge";
    	}
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
			if(conge.isRtt())
				x.setType("Rtt");
			else
				x.setType("Congé payé");
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
    	

		
  		/// NOTIF DEBUT ///
  		List<Notif> allNotif = NotifService.getAllByIdDesc(validateur.getUID());
  		for (int j = 0;j < allNotif.size();j++)
  		{
  			if (allNotif.get(j).getLien().equals("/Calendrier"))
  			{
  				NotifService.updateNotif(allNotif.get(j).getNotif_id(), true);
  			}
  		}
  		
  		/// NOTIF FIN  ///
  		
  	  /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		NotifClasse nbCongesEtRemb = new NotifClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService,NotifService);
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
  
    	
  		/// NOTIF DEBUT ///
  		List<Notif> allNotif = NotifService.findAll();
  		for (int i = 0;i < allNotif.size();i++)
  		{
  			if (allNotif.get(i).getUid() == uid)
  			{
  				if (allNotif.get(i).getLien().equals("/GererConges"))
  				{
  	  				NotifService.updateNotif(allNotif.get(i).getNotif_id(), true);
  				}
  			}
  		}
  		/// NOTIF FIN  ///
  		
  /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
    		NotifClasse nbCongesEtRemb = new NotifClasse();
    		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService,NotifService);
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
  		NotifClasse nbCongesEtRemb = new NotifClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService,NotifService);
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
        if (localDate.getDayOfMonth() >= 5)
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
        
    	List<Mission> listMissions = MissionService.findAll();
    	List<RemboursementsNote> listRembNote = RemboursementsNoteService.findAll();
    	List<Note> listNote = NoteService.findAll();
    	
        for(Remboursement r : recentDemandesRemboursement) {//TODO
        	Long mission_id = r.getMission_id();
        	int indMission = 0;
        	int indRembNote = 0;
        	String indMois = "";
        	for (int aa = 0;aa < listMissions.size();aa++)
        	{
        		if (listMissions.get(aa).getMission_id() == mission_id)
        		{
        			indMission = aa;
        			break;
        		}
        	}
        	if(!missionNames.containsKey(mission_id)) {
        		Mission m = listMissions.get(indMission);
        		missionNames.put(mission_id, m.getTitre());
        	}
        	
        	for (int bb = 0;bb < listRembNote.size();bb++)
        	{
        		if (listRembNote.get(bb).getDemande_id() == r.getDemande_id())
        		{
        			indRembNote = bb;
        			break;
        		}
        	}
        	
        	Long note_id = listRembNote.get(indRembNote).getNote_id();
        	
        	for (int cc = 0;cc < listNote.size();cc++)
        	{
        		if (listNote.get(cc).getNote_id() == note_id)
        		{
        			indMois = listNote.get(cc).getMois();
        			break;
        		}
        	}
        	
        	String mois = indMois;
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
        NotifClasse SachaEstClasse = new NotifClasse();
        int nbRemb = SachaEstClasse.getNbRemb(principal,MembresServiceBddService,RemboursementService,UtilisateurService);
        model.addAttribute("nbRemb", nbRemb);

		/*** DERNIERES NOTIFS ***/
        List<Notif> notifsFull = NotifService.getAllByIdDesc(userId);
		List<Notif> notifs = notifsFull.subList(0, (notifsFull.size() < 10 ? notifsFull.size() : 10));
		model.addAttribute("notifs", notifs);
		/*** DERNIERES NOTIFS ***/
        
		boolean IsChef = SachaEstClasse.isChef(principal, UtilisateurService, MembresServiceBddService);
		if (IsChef)
		{
			model.addAttribute("IsChef", IsChef);
		}

		int nbConges = SachaEstClasse.getNbConges(CongesService, UtilisateurService, MembresServiceBddService, principal);
		model.addAttribute("nbConges",nbConges);
		
		// NOTIF
		Utilisateur personne = UtilisateurService.findPrenomNom(names[1], names[0]);
		List<Notif> ln = NotifService.getAllByIdDesc(personne.getUID());
		System.out.println("LN : " + ln.size());
		int nbNotif = 0;
		for (int i = 0;i < ln.size();i++)
		{
			System.out.println("Notif ID = " + ln.get(i).getNotif_id());
			if (ln.get(i).getVue() == false)
			{
				nbNotif++;
			}
		}
		model.addAttribute("nbNotifs", nbNotif);
		
        return "accueil";
    }
    
    @RequestMapping("/Notifications")
    public String nofitications(Model model, Principal principal)
    {
    	String[] names = principal.getName().split("\\.");
    	List<Notif> notifs = NotifService.getAllByIdDesc(UtilisateurService.findPrenomNom(names[1], names[0]).getUID());
    	model.addAttribute("notifs", notifs);
  /////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
  		NotifClasse nbCongesEtRemb = new NotifClasse();
  		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService,NotifService);
  		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
    	return "notifications";
    }
    
    
}