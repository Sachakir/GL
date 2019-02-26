package sacha.kir;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sacha.kir.bdd.approle.InterfaceAppRoleService;
import sacha.kir.bdd.appuser.InterfaceAppUserService;
import sacha.kir.bdd.conges.Conges;
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
import sacha.kir.bdd.remboursement.Remboursement;
import sacha.kir.bdd.remboursement.Statut;
import sacha.kir.bdd.services.InterfaceServiceBddService;
import sacha.kir.bdd.services.ServiceBdd;
import sacha.kir.bdd.services.ServicesFixes;
import sacha.kir.bdd.userrole.InterfaceUserRoleService;
import sacha.kir.bdd.utilisateur.InterfaceUtilisateurService;
import sacha.kir.bdd.utilisateur.Utilisateur;
import sacha.kir.form.CongesV2;
import sacha.kir.form.Passwords;
import sacha.kir.form.RemboursementV2;
import sacha.kir.form.UserForm;
import sacha.kir.form.UserList;

@Controller
public class SachaController 
{
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
	
    @ModelAttribute("username")
    public String getUsername(Principal principal) {
    	if(principal != null)
    	{
    		String[] name = principal.getName().split("\\.");
    		return name[0] + " " + name[1];
    	}
    	else return "";
    }
    
	@GetMapping("/validationConges")
    public String addConges(Principal principal,Model model) 
	{
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	MembresServiceBdd membre = MembresServiceBddService.findById(ut.getUID());
    	
    	System.out.println("Le role est : " + ServiceBddService.findById(membre.getServiceId()).getNom());
    	
    	// Si l'utilisateur est membre du service RH, alors ce morceau de code est executé
    	if (membre.getServiceId() == ServicesFixes.ressourcesHumaines.getServiceId()) 
    	{
        	model.addAttribute("pageValidation","RH");
        	List<CongesV2> conges = new ArrayList<CongesV2>();
        	List<Conges> cs = CongesService.findAll();
        	for (int i =0; i<cs.size(); i++)
        	{	
        		Utilisateur tmpu = UtilisateurService.findById(cs.get(i).getUid());
        			
        		CongesV2 c2 = new CongesV2();
        		c2.setCongesid(cs.get(i).getCongesid());
        		c2.setDatedebut(cs.get(i).getDatedebut());
        		c2.setDatefin(cs.get(i).getDatefin());
        		c2.setUid(cs.get(i).getUid());
        		c2.setValidationchefservice(cs.get(i).getValidationchefdeservice());
        		c2.setValidationrh(cs.get(i).getValidationrh());
        		c2.setPrenomNom(tmpu.getPrenom() + " " + tmpu.getNom());
        		conges.add(c2);
        	}

        	model.addAttribute("listConges",conges);//Envoie de toutes les demandes au RH
            return "showConges";
    	}
    	
    	// Si l'utilisateur est chef de service, ce code est executé
    	else if(membre.getRoleId() == Role.chefDeService.getRoleId()){
    		model.addAttribute("pageValidation","Chef");
    		long service_id = membre.getServiceId();
    		
    		List<Long> membres_service_ids = MembresServiceBddService.getAllUidByServiceId(service_id);
    		membres_service_ids.remove(membre.getUid());
    		List<Conges> cs = CongesService.findAllByIds(membres_service_ids);
        	List<CongesV2> conges = new ArrayList<CongesV2>();
        	
        	for (Conges c : cs)
        	{
        		Utilisateur tmpu = UtilisateurService.findById(c.getUid());
        			
        		CongesV2 c2 = new CongesV2();
        		c2.setCongesid(c.getCongesid());
        		c2.setDatedebut(c.getDatedebut());
        		c2.setDatefin(c.getDatefin());
        		c2.setUid(c.getUid());
        		c2.setValidationchefservice(c.getValidationchefdeservice());
        		c2.setValidationrh(c.getValidationrh());
        		c2.setPrenomNom(tmpu.getPrenom() + " " + tmpu.getNom());
        		conges.add(c2);
        	}
        	model.addAttribute("listConges",conges);
    	}
        return "showConges";
    }
	
	@RequestMapping(path="/Chef/{id}")
    public String getMessage(@PathVariable("id") long id,CongesV2 congesv2,Model model,Principal principal) 
    {
		Conges c = CongesService.findByCongesId(id);

		Utilisateur ut = UtilisateurService.findById(c.getUid());
		congesv2.setCongesid(id);
		congesv2.setDatedebut(c.getDatedebut());
		congesv2.setDatefin(c.getDatefin());
		congesv2.setPrenomNom(ut.getPrenom() + " " + ut.getNom());
		congesv2.setValidationchefservice(c.getValidationchefdeservice());
		congesv2.setValidationrh(c.getValidationrh());
		
		model.addAttribute("conges", congesv2);
		return "showCongesDetailsChef";
    }
	
	@GetMapping("/congesChanged")
    public String congesChanged(CongesV2 congesv2,Model model)
    {
		if ( (congesv2.getValidationchefservice() != null && !congesv2.getValidationchefservice().contains("NoChanges") ) || (congesv2.getValidationrh() != null && !congesv2.getValidationrh().contains("NoChanges") ) )
		{
			if (congesv2.getValidationchefservice() != null)
			{
				CongesService.updateChefState(congesv2.getCongesid(), congesv2.getValidationchefservice());
			}
			else if (congesv2.getValidationrh() != null)
			{
				CongesService.updateRHState(congesv2.getCongesid(), congesv2.getValidationrh());
			}
			model.addAttribute("change","Changement effectue !");
		}
		else
		{
			model.addAttribute("change","Pas de changement");
		}
		return "resultatValidation";
    }
	
	@RequestMapping(path="/RH/{id}")
    public String getMessageRH(@PathVariable("id") long id,CongesV2 congesv2,Model model,Principal principal) 
    {
		Conges c = CongesService.findByCongesId(id);

		Utilisateur ut = UtilisateurService.findById(c.getUid());
		congesv2.setCongesid(id);
		congesv2.setDatedebut(c.getDatedebut());
		congesv2.setDatefin(c.getDatefin());
		congesv2.setPrenomNom(ut.getPrenom() + " " + ut.getNom());
		congesv2.setValidationchefservice(c.getValidationchefdeservice());
		congesv2.setValidationrh(c.getValidationrh());
		
		model.addAttribute("conges", congesv2);
		return "showCongesDetailsRH";
    }
	
	@RequestMapping(path="/deleteUser/{id}")
    public String getMessage(@PathVariable("id") long id,Model model,UserForm userForm,Principal principal) 
    {
		// Morceau test pour l'affichage d'un user
		MembresServiceBdd membre = MembresServiceBddService.findById(id);
		if (membre != null)
		{
			Map<Long, String> listeServices = ServiceBddService.getAllServiceNames();
			System.out.println(membre.getUid());
			System.out.println(listeServices.get(membre.getServiceId()));
			System.out.println(Role.getRoleNameById(membre.getRoleId()));
		}
		//////////////////////////////////////
		
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
			return "redirect:/adminShow";
		}
		
		MembresMissionService.deleteMembresMission(id);
		System.out.println("Delete de membresMission");
		CongesService.deleteConges(id);
		System.out.println("Delete de conges");
		
		// Lignes pas ok
		UserRoleService.deleteUserRole(id);
		System.out.println("Delete de UserRole");

		AppUserService.deleteAppUser(id);
		System.out.println("Delete de AppUser");
		////////////////////////////////////////////

		RemboursementService.deleteRembUid(id);
		System.out.println("Delete de Remboursement");

		NoteService.deleteNoteUid(id);
		System.out.println("Delete de Note");

		UtilisateurService.deleteUser(id);
		System.out.println("Delete de Utilisateur");
		
		
		System.out.println("DELETED " + id);
		
		List<Utilisateur> cs = UtilisateurService.findAll();
    	model.addAttribute("listUsers", cs);
    	String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	model.addAttribute("notAdmin",UtilisateurService.findPrenomNom(names[1], names[0]).getUID());
		return "redirect:/adminShow";
    }
	
	@RequestMapping("/validationNDF")
    public String validationNDF(Model model,Principal principal)
    {
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	MembresServiceBdd ms = MembresServiceBddService.findById(ut.getUID());
		List<RemboursementV2> remboursementAssocies = new ArrayList<RemboursementV2>();
		List<Remboursement> allRemboursements = RemboursementService.findAll();
    	if (ms.getRoleId() == Role.chefDeService.getRoleId())//Seulement pour un chef de service
    	{
    		long myServiceId = ms.getServiceId();
    		long uidConges;
    		for (int i = 0;i < allRemboursements.size();i++)
    		{
    			uidConges = allRemboursements.get(i).getUid();
    			if (MembresServiceBddService.findById(uidConges).getServiceId() == myServiceId)//Du meme service
    			{
    				if (allRemboursements.get(i).getValidationchefservice().equals(Statut.enAttente.statut()))//Demandes en attente
    				{
	    				if (allRemboursements.get(i).getUid() != ut.getUID())//Check si pas autovalidation
	    				{
	    					Utilisateur tmpUs = UtilisateurService.findById(allRemboursements.get(i).getUid());
	        				RemboursementV2 tmpRem = new RemboursementV2();
	        				tmpRem.setDate(allRemboursements.get(i).getDate().toString());
	        				tmpRem.setDemande_id(allRemboursements.get(i).getDemande_id());
	        				tmpRem.setJustificatifid(allRemboursements.get(i).getJustificatifid());
	        				tmpRem.setMission_id(allRemboursements.get(i).getMission_id());
	        				tmpRem.setMontant(allRemboursements.get(i).getMontant());
	        				tmpRem.setMotif(allRemboursements.get(i).getMotif());
	        				tmpRem.setPrenomnom(tmpUs.getPrenom() + " " + tmpUs.getNom());
	        				tmpRem.setTitre(allRemboursements.get(i).getTitre());
	        				tmpRem.setValidationchefservice(allRemboursements.get(i).getValidationchefservice());
	        				tmpRem.setValidationfinances(allRemboursements.get(i).getValidationfinances());
	        				
	        				remboursementAssocies.add(tmpRem);
	    				}
    				}
    			}
    			else if (myServiceId == ServicesFixes.finances.getServiceId() && allRemboursements.get(i).getValidationchefservice().equals(Statut.valide.statut()) && allRemboursements.get(i).getValidationfinances().equals(Statut.enAttente.statut()))
    			//Service different, mais la demande est validé par leur chef de service
    			{
    				if (allRemboursements.get(i).getUid() != ut.getUID())//Check si pas autovalidation
    				{					
    					Utilisateur tmpUs = UtilisateurService.findById(allRemboursements.get(i).getUid());
        				RemboursementV2 tmpRem = new RemboursementV2();
        				tmpRem.setDate(allRemboursements.get(i).getDate().toString());
        				tmpRem.setDemande_id(allRemboursements.get(i).getDemande_id());
        				tmpRem.setJustificatifid(allRemboursements.get(i).getJustificatifid());
        				tmpRem.setMission_id(allRemboursements.get(i).getMission_id());
        				tmpRem.setMontant(allRemboursements.get(i).getMontant());
        				tmpRem.setMotif(allRemboursements.get(i).getMotif());
        				tmpRem.setPrenomnom(tmpUs.getPrenom() + " " + tmpUs.getNom());
        				tmpRem.setTitre(allRemboursements.get(i).getTitre());
        				tmpRem.setValidationchefservice(allRemboursements.get(i).getValidationchefservice());
        				tmpRem.setValidationfinances(allRemboursements.get(i).getValidationfinances());
        				
        				remboursementAssocies.add(tmpRem);
    				}
    			}
    		}
    	}
		model.addAttribute("remboursements", remboursementAssocies);
		return "validerndf";
    }
	
	
	@RequestMapping(path="/ValidationRemb/{id}")
    public String ValidationRemb(@PathVariable("id") long demandeId,Principal principal)
    {
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	
    	MembresServiceBdd membre = MembresServiceBddService.findById(ut.getUID());
    	if (membre.getServiceId() == ServicesFixes.finances.getServiceId())
    	{
    		long uidDemande = RemboursementService.findById(demandeId).getUid();
    		long serviceIdDemande = MembresServiceBddService.findById(uidDemande).getServiceId();
    		if (serviceIdDemande == ServicesFixes.finances.getServiceId())
    		{
        		RemboursementService.updateChefState(demandeId, Statut.valide.statut());
    		}
    		RemboursementService.updateFinancesState(demandeId, Statut.valide.statut());
    	}
    	else if (membre.getRoleId() == Role.chefDeService.getRoleId())
    	{
    		RemboursementService.updateChefState(demandeId, Statut.valide.statut());
    	}
    	else
    	{
    		System.out.println("Vous ne pouvez pas valider de demandes de remboursement vous etes : " + membre.getRoleId());
    	}
    	
		return "redirect:/validationNDF";
    }
	
	
	@RequestMapping(path="/RefusRemb/{id}")
    public String RefusRemb(@PathVariable("id") long demandeId,Principal principal)
    {
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	
    	MembresServiceBdd membre = MembresServiceBddService.findById(ut.getUID());
    	if (membre.getServiceId() == ServicesFixes.finances.getServiceId())
    	{
    		long uidDemande = RemboursementService.findById(demandeId).getUid();
    		long serviceIdDemande = MembresServiceBddService.findById(uidDemande).getServiceId();
    		if (serviceIdDemande == ServicesFixes.finances.getServiceId())
    		{
        		RemboursementService.updateChefState(demandeId, Statut.refuse.statut());
    		}
    		RemboursementService.updateFinancesState(demandeId, Statut.refuse.statut());
    	}
    	else if (membre.getRoleId() == Role.chefDeService.getRoleId())
    	{
    		RemboursementService.updateChefState(demandeId, Statut.refuse.statut());
    	}
    	else
    	{
    		System.out.println("Vous ne pouvez refuser de demandes de remboursement");
    	}
    	
		return "redirect:/validationNDF";
    }
	
	@RequestMapping("/parametres")
    public String parametres(Principal principal,Model model)
    {
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	
    	UserForm userform = new UserForm();
    	userform.setNumTel(ut.getNumeroTel());
    	userform.setNom(ut.getNom());
    	userform.setPrenom(ut.getPrenom());
    	
    	long myServiceId = MembresServiceBddService.findById(ut.getUID()).getServiceId();
    	ServiceBdd sbdd = ServiceBddService.findById(myServiceId);
    	sbdd.getNom();
    	
    	model.addAttribute("userform", userform);
    	model.addAttribute("service", sbdd.getNom());
    	
    	
		return "parametres";
    }
	
	@GetMapping("/changeMdp")
    public String changeMdp(Passwords passwords)
    {
		return "changeMdp";
    }
	
	@PostMapping("/changeMdp")
    public String checkMdp(Passwords passwords,Principal principal)
    {
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	sacha.kir.bdd.appuser.AppUser au = AppUserService.findByUid(ut.getUID());
    	
    	if (!EncrytedPasswordUtils.comparePass(passwords.getOldpass(), au.getEncrypted_password()))
    	{
    		System.out.println("Mot de passe different!");
    		return "changeMdp";
    	}
    	if (passwords.getNewPass().length() == 0)
    	{
    		System.out.println("Mot de passe null");
    		return "changeMdp";
    	}
    	else
    	{
    		String newPass = EncrytedPasswordUtils.encryptePassword(passwords.getNewPass());
    		AppUserService.updatePassword(ut.getUID(), newPass);
    	}
		return "redirect:/parametres";
    }
	
	
	
	@RequestMapping(path="/ValidationConges/{id}")
    public String ValidationConges(@PathVariable("id") long congesId,Principal principal) throws Exception
    {
		Conges congesAValider = CongesService.findByCongesId(congesId);
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); // format jour / mois / année
		LocalDate date1 = LocalDate.parse(congesAValider.getDatedebut()+":00", format);
		LocalDate date2 = LocalDate.parse(congesAValider.getDatefin()+":00", format);
		String[] d1=congesAValider.getDatedebut().split(" ");
		String[] d2=congesAValider.getDatefin().split(" ");
		String[] h1= d1[1].split(":");
		String[] h2= d2[1].split(":");
		long conges = ChronoUnit.DAYS.between(date1, date2);
		long heuresDemandes = Long.parseLong(h2[0]) - Long.parseLong(h1[0]);

		float joursDemandes = conges;
		if(heuresDemandes<=4 && heuresDemandes>0)
			joursDemandes+=0.5;
		else if(heuresDemandes>4) {
			joursDemandes+=1.0;
		}
		else if(heuresDemandes>-24 && heuresDemandes<20)
		{
			joursDemandes-=0.5;
		}
		System.out.println("joursDemandes (inshallah float): "+joursDemandes);
		if (joursDemandes == 0)
		{
			throw new Exception("AH ! 0 jours de conges demandés. Corrigez la damande de conges SVP !");
		}
		else if (joursDemandes < 0)
		{
			throw new Exception("Oula ! Les dates de debut et de fin doivent etre inversés...");
		}
		else
		{
			long uidDuDemandeur = CongesService.getCongesbyId(congesId).getUid();
			Utilisateur demandeur = UtilisateurService.findById(uidDuDemandeur);
			Conges congesDemande = CongesService.findByCongesId(congesId);
			if (congesDemande.isRtt())
			{
				if (demandeur.getRtt() < joursDemandes)
				{
					throw new Exception(demandeur.getPrenom() + " " + demandeur.getNom() + " a " + demandeur.getRtt() + " jours de RTT et demande " + joursDemandes + " jours de conges RTT");
				}
			}
			else if (!congesDemande.isRtt())
			{
				if (demandeur.getJoursCongesRestants() < joursDemandes)
				{
					throw new Exception(demandeur.getPrenom() + " " + demandeur.getNom() + " a " + demandeur.getJoursCongesRestants() + " jours de conges et demande " + joursDemandes + " jours de conges");
				}
			}
		}
		
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	
    	MembresServiceBdd validateur = MembresServiceBddService.findById(ut.getUID());
    	if (validateur.getRoleId() == (Role.chefDeService.getRoleId()))
    	{
    		if (validateur.getServiceId() == ServicesFixes.ressourcesHumaines.getServiceId())
    		{
    			long demandeUID = CongesService.getCongesbyId(congesId).getUid();
    			long serviceId = MembresServiceBddService.findById(demandeUID).getServiceId();
    			if (serviceId == ServicesFixes.ressourcesHumaines.getServiceId())
    			{
    				CongesService.updateChefState(congesId, Statut.valide.statut());
    			}
    			CongesService.updateRHState(congesId, Statut.valide.statut());
    			Conges congesDemande = CongesService.findByCongesId(congesId);
				Utilisateur demandeur = UtilisateurService.findById(demandeUID);
    			if (congesDemande.isRtt())
    			{
    				UtilisateurService.updateJoursRtt(demandeUID, (long) (demandeur.getRtt() - joursDemandes));
    			}
    			else if (!congesDemande.isRtt())
    			{
    				UtilisateurService.updateJoursConges(demandeUID, demandeur.getJoursCongesRestants() - joursDemandes);
    			}
    			//Refus automatique de tous les autres conges du demandeur
    			//qui sont devenus impossibles à valider
    			ArrayList<Long> demandeurUID = new ArrayList<Long>();
    			demandeurUID.add(demandeur.getUID());
    			Utilisateur demandeurRefresh = UtilisateurService.findById(demandeur.getUID());
    			List<Conges> congesDuDemandeur = CongesService.findAllByIds(demandeurUID);
    			for (int cong = 0;cong < congesDuDemandeur.size();cong++)
    			{
    				LocalDate datedebut = LocalDate.parse(congesDuDemandeur.get(cong).getDatedebut()+":00", format);
    				LocalDate datefin = LocalDate.parse(congesDuDemandeur.get(cong).getDatefin()+":00", format);		
    				long joursCongesDemandes = ChronoUnit.DAYS.between(datedebut, datefin);
    				boolean isRTT = CongesService.findByCongesId(congesDuDemandeur.get(cong).getCongesid()).isRtt();
    				if (isRTT)
    				{
    					if (demandeurRefresh.getRtt() < joursCongesDemandes)
    					{
    	    				CongesService.updateRHState(congesDuDemandeur.get(cong).getCongesid(), Statut.refuse.statut());
    					}
    				}
    				else//Conges non RTT
    				{
    					if (demandeurRefresh.getJoursCongesRestants() < joursCongesDemandes)
    					{
    	    				CongesService.updateRHState(congesDuDemandeur.get(cong).getCongesid(), Statut.refuse.statut());
    					}
    				} 				
    			}
    		}
    		else//Validateur n'est pas chef du service RH, mais chef d'un autre service
    		{
    			CongesService.updateChefState(congesId, Statut.valide.statut());
    		}
    	}
    	else//Le validateur n'est pas chef de service
    	{
    		throw new Exception("La personne connectée n'a pas le droit de valider de conges !");
    	}  	
		return "redirect:/Accueil";
		
    }
	
	@RequestMapping(path="/RefusConges/{id}")
    public String RefusConges(@PathVariable("id") long congesId,Principal principal) throws Exception
    {
		Conges congesAValider = CongesService.findByCongesId(congesId);
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); // format jour / mois / année
		LocalDate date1 = LocalDate.parse(congesAValider.getDatedebut()+":00", format);
		LocalDate date2 = LocalDate.parse(congesAValider.getDatefin()+":00", format);
		
		String[] d1=congesAValider.getDatedebut().split(" ");
		String[] d2=congesAValider.getDatefin().split(" ");
		String[] h1= d1[1].split(":");
		String[] h2= d2[1].split(":");
		
		long conges = ChronoUnit.DAYS.between(date1, date2);
		long heuresDemandes = Long.parseLong(h2[0]) - Long.parseLong(h1[0]);
		float joursDemandes = conges;
		if(heuresDemandes<=4 && heuresDemandes>0)
			joursDemandes+=0.5;
		else if(heuresDemandes>4) {
			joursDemandes+=1.0;
		}
		else if(heuresDemandes>-24 && heuresDemandes<20)
		{
			joursDemandes-=0.5;
		}
		if (joursDemandes == 0)
		{
			throw new Exception("AH ! 0 jours de conges demandés. Corrigez la damande de conges SVP !");
		}
		else if (joursDemandes < 0)
		{
			throw new Exception("Oula ! Les dates de debut et de fin doivent etre inversés...");
		}
		
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	
    	MembresServiceBdd validateur = MembresServiceBddService.findById(ut.getUID());
    	if (validateur.getRoleId() == (Role.chefDeService.getRoleId()))
    	{
    		if (validateur.getServiceId() == ServicesFixes.ressourcesHumaines.getServiceId())
    		{
    			long demandeUID = CongesService.getCongesbyId(congesId).getUid();
    			long serviceId = MembresServiceBddService.findById(demandeUID).getServiceId();
    			if (serviceId == ServicesFixes.ressourcesHumaines.getServiceId())
    			{
    				CongesService.updateChefState(congesId, Statut.refuse.statut());
    			}
    			CongesService.updateRHState(congesId, Statut.refuse.statut());
    		}
    		else//Validateur n'est pas chef du service RH, mais chef d'un autre service
    		{
    			CongesService.updateChefState(congesId, Statut.refuse.statut());
    		}
    	}
    	else//Le validateur n'est pas chef de service
    	{
    		throw new Exception("La personne connectée n'a pas le droit de refuser de conges !");
    	}  	
		return "redirect:/Accueil";
    }
	
	@RequestMapping(path="/DeleteConges/{id}")
    public String DeleteConges(@PathVariable("id") long congesId,Principal principal) throws Exception
    {
		CongesService.deleteCongesbyCongesID(congesId);
		System.out.println("Conges avec l'id : " + congesId + " a été supprimé");
		return "redirect:/GererConges";
    }
}
