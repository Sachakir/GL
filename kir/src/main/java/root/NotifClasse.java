package root;

import java.security.Principal;
import java.util.List;

import org.springframework.ui.Model;

import root.bdd.conges.Conges;
import root.bdd.conges.InterfaceCongesService;
import root.bdd.membresservice.InterfaceMembresServiceBddService;
import root.bdd.membresservice.MembresServiceBdd;
import root.bdd.membresservice.Role;
import root.bdd.notif.InterfaceNotifService;
import root.bdd.notif.Notif;
import root.bdd.remboursement.InterfaceRemboursementService;
import root.bdd.remboursement.Remboursement;
import root.bdd.remboursement.Statut;
import root.bdd.services.ServicesFixes;
import root.bdd.utilisateur.InterfaceUtilisateurService;
import root.bdd.utilisateur.Utilisateur;

public class NotifClasse {
	
	public int getNbRemb(Utilisateur ut, InterfaceMembresServiceBddService MembresServiceBddService,InterfaceRemboursementService RemboursementService)
	{
		int nbRemb = 0;
    	MembresServiceBdd ms = MembresServiceBddService.findById(ut.getUID());
		List<Remboursement> allRemboursements = RemboursementService.findAll();
    	if (ms.getRoleId() == Role.chefDeService.getRoleId())//Seulement pour un chef de service
    	{
    		long myServiceId = ms.getServiceId();
    		long uidConges;
    		
    		List<MembresServiceBdd> listMembresServ = MembresServiceBddService.findAll();
    		
    		
    		for (int i = 0;i < allRemboursements.size();i++)
    		{
    			long serviceIdConges = 0;
    			uidConges = allRemboursements.get(i).getUid();
    			
    			for (int j = 0;j < listMembresServ.size();j++)
        		{
        			if (listMembresServ.get(j).getUid() == uidConges)
        			{
        				serviceIdConges = listMembresServ.get(j).getServiceId();
        				break;
        			}
        		}
    			
    			if (serviceIdConges == myServiceId)//Du meme service
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
	
	public boolean isChef(Utilisateur ut, InterfaceMembresServiceBddService MembresServiceBddService)
	{
		long myUID = ut.getUID();
		if (MembresServiceBddService.findById(myUID).getRoleId() == Role.chefDeService.getRoleId())
		{
			return true;
		}
		return false;
	}
	
	public int getNbConges(Utilisateur ut, InterfaceCongesService CongesService, InterfaceMembresServiceBddService MembresServiceBddService)
	{
		int nbConges = 0;
		
		MembresServiceBdd validateurRoles = MembresServiceBddService.findById(ut.getUID());
		long myServiceId = validateurRoles.getServiceId();
		long uidConges;
		
		List<Conges> conges = CongesService.findAll();
		List<MembresServiceBdd> listMembresServ = MembresServiceBddService.findAll();

		for(int j=0;j<conges.size();j++) {
			if (validateurRoles.getRoleId() == Role.chefDeService.getRoleId())
			{
				uidConges = conges.get(j).getUid();
				long serviceIdConges = 0;
				for (int k = 0;k < listMembresServ.size();k++)
	    		{
	    			if (listMembresServ.get(k).getUid() == uidConges)
	    			{
	    				serviceIdConges = listMembresServ.get(k).getServiceId();
	    				break;
	    			}
	    		}
				if (serviceIdConges == myServiceId)//Du meme service
				{
					if (conges.get(j).getValidationchefdeservice().equals(Statut.enAttente.statut()))//Demandes en attente
					{
						if (conges.get(j).getUid() != ut.getUID())//Check si pas autovalidation
						{
							nbConges++;
						}
					}
				}
				else if (myServiceId == ServicesFixes.ressourcesHumaines.getServiceId() && conges.get(j).getValidationchefdeservice().equals(Statut.valide.statut()) && conges.get(j).getValidationrh().equals(Statut.enAttente.statut()))
	    		//Service different, mais la demande est validé par leur chef de service
				{
					if (conges.get(j).getUid() != ut.getUID())//Check si pas autovalidation
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
		long debut = System.currentTimeMillis();

	
	/////// CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
			NotifClasse nbCongesEtRemb = new NotifClasse();	
			String[] names = principal.getName().split("\\.");
			Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
			boolean IsChef = nbCongesEtRemb.isChef(ut, MembresServiceBddService);
			if (IsChef)
			{
				int nbConges = nbCongesEtRemb.getNbConges(ut, CongesService, MembresServiceBddService);
				int nbRemb = nbCongesEtRemb.getNbRemb(ut, MembresServiceBddService, RemboursementService);
				
		        model.addAttribute("nbRemb", nbRemb);
				model.addAttribute("nbConges", nbConges);
				model.addAttribute("IsChef", IsChef);
			}
			
			List<Notif> ln = NotifService.getAllByIdDesc(ut.getUID());
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
			
			long time = (System.currentTimeMillis()-debut);
			System.out.println("Temps en ms des notifs : " + time);
			return model;
			
			/////// FIN DU CODE QUI GERE LES NOMBRES DE CONGES ET REMB ////////
	}
}
