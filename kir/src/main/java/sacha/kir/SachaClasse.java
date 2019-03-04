package sacha.kir;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import sacha.kir.bdd.conges.Conges;
import sacha.kir.bdd.conges.InterfaceCongesService;
import sacha.kir.bdd.membresservice.InterfaceMembresServiceBddService;
import sacha.kir.bdd.membresservice.MembresServiceBdd;
import sacha.kir.bdd.membresservice.Role;
import sacha.kir.bdd.notif.InterfaceNotifService;
import sacha.kir.bdd.notif.Notif;
import sacha.kir.bdd.remboursement.InterfaceRemboursementService;
import sacha.kir.bdd.remboursement.Remboursement;
import sacha.kir.bdd.remboursement.Statut;
import sacha.kir.bdd.services.ServicesFixes;
import sacha.kir.bdd.utilisateur.InterfaceUtilisateurService;
import sacha.kir.bdd.utilisateur.Utilisateur;
import sacha.kir.form.CongesV2;

public class SachaClasse {

	public int getNbRemb(Principal principal,InterfaceMembresServiceBddService MembresServiceBddService,InterfaceRemboursementService RemboursementService,InterfaceUtilisateurService UtilisateurService)
	{
		int nbRemb = 0;
		String[] names = principal.getName().split("\\.");
		Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	MembresServiceBdd ms = MembresServiceBddService.findById(ut.getUID());
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
	    					nbRemb++;
	    				}
    				}
    			}
    			else if (myServiceId == ServicesFixes.finances.getServiceId() && allRemboursements.get(i).getValidationchefservice().equals(Statut.valide.statut()) && allRemboursements.get(i).getValidationfinances().equals(Statut.enAttente.statut()))
    			//Service different, mais la demande est validé par leur chef de service
    			{
    				if (allRemboursements.get(i).getUid() != ut.getUID())//Check si pas autovalidation
    				{
    					nbRemb++;
    				}
    			}
    		}
    	}
		
		return nbRemb;
	}
	
	public boolean isChef(Principal principal, InterfaceUtilisateurService UtilisateurService, InterfaceMembresServiceBddService MembresServiceBddService)
	{
		String[] names = principal.getName().split("\\.");
		long myUID = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
		if (MembresServiceBddService.findById(myUID).getRoleId() == Role.chefDeService.getRoleId())
		{
			return true;
		}
		return false;
	}
	
	public int getNbConges(InterfaceCongesService CongesService,InterfaceUtilisateurService UtilisateurService,InterfaceMembresServiceBddService MembresServiceBddService,Principal principal)
	{
		int nbConges = 0;
		
		String[] names = principal.getName().split("\\.");
		Utilisateur validateur = UtilisateurService.findPrenomNom(names[1], names[0]);
		MembresServiceBdd validateurRoles = MembresServiceBddService.findById(validateur.getUID());
		long myServiceId = validateurRoles.getServiceId();
		long uidConges;
		
		List<Conges> conges = CongesService.findAll();
		
		for(int j=0;j<conges.size();j++) {			
			if (validateurRoles.getRoleId() == Role.chefDeService.getRoleId())
			{
				uidConges = conges.get(j).getUid();
				if (MembresServiceBddService.findById(uidConges).getServiceId() == myServiceId)//Du meme service
				{
					if (conges.get(j).getValidationchefdeservice().equals(Statut.enAttente.statut()))//Demandes en attente
					{
						if (conges.get(j).getUid() != validateur.getUID())//Check si pas autovalidation
						{
							nbConges++;
						}
					}
				}
				else if (myServiceId == ServicesFixes.ressourcesHumaines.getServiceId() && conges.get(j).getValidationchefdeservice().equals(Statut.valide.statut()) && conges.get(j).getValidationrh().equals(Statut.enAttente.statut()))
	    		//Service different, mais la demande est validé par leur chef de service
				{
					if (conges.get(j).getUid() != validateur.getUID())//Check si pas autovalidation
					{
						nbConges++;
					}
				}
			}			
		}		
		return nbConges;
	}
	
	public Model addNumbersToModel(Model model,Principal principal,InterfaceCongesService CongesService,InterfaceUtilisateurService UtilisateurService,InterfaceMembresServiceBddService MembresServiceBddService,InterfaceRemboursementService RemboursementService, InterfaceNotifService NotifService)
	{
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
			
			String[] names = principal.getName().split("\\.");
			Utilisateur personne = UtilisateurService.findPrenomNom(names[1], names[0]);
			List<Notif> ln = NotifService.getAllByIdDesc(personne.getUID());
			int nbNotif = 0;
			for (int i = 0;i < ln.size();i++)
			{
				if (ln.get(i).getVue() == false)
				{
					nbNotif++;
				}
			}
			System.out.println("NOTIF NB : " + nbNotif);
			model.addAttribute("nbNotifs", nbNotif);

			return model;
			/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
	}
}
