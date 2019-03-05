package root.bdd.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import root.EncrytedPasswordUtils;
import root.NotifClasse;
import root.bdd.appuser.InterfaceAppUserService;
import root.bdd.conges.Conges;
import root.bdd.conges.InterfaceCongesService;
import root.bdd.membresmission.InterfaceMembresMissionService;
import root.bdd.membresservice.InterfaceMembresServiceBddService;
import root.bdd.membresservice.MembresServiceBdd;
import root.bdd.membresservice.Role;
import root.bdd.mission.InterfaceMissionService;
import root.bdd.mission.Mission;
import root.bdd.note.InterfaceNoteService;
import root.bdd.notif.InterfaceNotifService;
import root.bdd.notif.Notif;
import root.bdd.remboursement.InterfaceRemboursementService;
import root.bdd.remboursement.Remboursement;
import root.bdd.remboursement.Statut;
import root.bdd.services.InterfaceServiceBddService;
import root.bdd.services.ServiceBdd;
import root.bdd.services.ServicesFixes;
import root.bdd.utilisateur.InterfaceUtilisateurService;
import root.bdd.utilisateur.Utilisateur;
import root.forms.conges.CongeForm;
import root.forms.password.Passwords;
import root.forms.remboursement.RemboursementV2;
import root.forms.utilisateur.UserForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SachaController 
{
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
	@Autowired
	InterfaceNotifService NotifService;

	@ModelAttribute("username")
	public String getUsername(Principal principal) {
		if(principal != null)
		{
			String[] name = principal.getName().split("\\.");
			return name[0] + " " + name[1];
		}
		else return "";
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

		long myServiceId = ms.getServiceId();
		long uidConges;
		for (int i = 0;i < allRemboursements.size();i++)
		{
			uidConges = allRemboursements.get(i).getUid();
			if (ms.getRoleId() == Role.chefDeService.getRoleId() && MembresServiceBddService.findById(uidConges).getServiceId() == myServiceId)
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
			else//Service Finances
			{
				if (allRemboursements.get(i).getValidationfinances().equals(Statut.enAttente.statut()))//Demandes en attente
				{
					System.out.println("170");
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



		/// NOTIF DEBUT ///
		List<Notif> allNotif = NotifService.findAll();
		for (int i = 0;i < allNotif.size();i++)
		{
			if (allNotif.get(i).getUid() == ut.getUID())
			{
				if (allNotif.get(i).getLien().equals("/validationNDF"))
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

		/////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
		NotifClasse nbCongesEtRemb = new NotifClasse();
		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService,NotifService);
		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////


		return "parametres";
	}

	@GetMapping("/changeMdp")
	public String changeMdp(Passwords passwords,Principal principal,Model model)
	{
		/////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
		NotifClasse nbCongesEtRemb = new NotifClasse();
		model = nbCongesEtRemb.addNumbersToModel(model, principal, CongesService, UtilisateurService, MembresServiceBddService, RemboursementService,NotifService);
		/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
		return "changeMdp";
	}

	@PostMapping("/changeMdp")
	public String checkMdp(Passwords passwords,Principal principal)
	{
		String prenomnom = principal.getName();
		String[] names = prenomnom.split("\\.");
		Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
		root.bdd.appuser.AppUser au = AppUserService.findByUid(ut.getUID());

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
		double weeks = Math.floor(joursDemandes / 7);
		joursDemandes-= weeks * 2;
		int startDay = date1.getDayOfWeek().getValue();
		int endDay = date2.getDayOfWeek().getValue();
		// Remove weekend not previously removed.   
		if (startDay - endDay > 1) {
			joursDemandes -= 2;
		}
		// Remove start day if span starts on Sunday but ends before Saturday
		if (startDay == 7 && endDay != 6) {
			joursDemandes--;  
		}
		// Remove end day if span ends on Saturday but starts after Sunday
		if (endDay == 6 && startDay != 7) {
			joursDemandes--;
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
					UtilisateurService.updateJoursRtt(demandeUID,  demandeur.getRtt() - joursDemandes);
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
	public String RefusConges(@PathVariable("id") long congesId,Principal principal,@Valid CongeForm congeForm, BindingResult result) throws Exception
	{
		Conges congesAValider = CongesService.findByCongesId(congesId);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); // format jour / mois / année
		LocalDate date1 = LocalDate.parse(congesAValider.getDatedebut()+":00", format);
		LocalDate date2 = LocalDate.parse(congesAValider.getDatefin()+":00", format);
		System.out.println("Motif de refus: "+congeForm.getMotifRefus());
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
				CongesService.updateMotifRefus(congesId,  congeForm.getMotifRefus());
			}
			else//Validateur n'est pas chef du service RH, mais chef d'un autre service
			{
				CongesService.updateChefState(congesId, Statut.refuse.statut());
				CongesService.updateMotifRefus(congesId,  congeForm.getMotifRefus());
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
