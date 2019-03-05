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
import root.bdd.utilisateur.InterfaceUtilisateurService;
import root.bdd.utilisateur.Utilisateur;

public class NotifClasse {
	
	public int getNbRemb(Utilisateur ut, InterfaceMembresServiceBddService MembresServiceBddService,InterfaceRemboursementService RemboursementService)
	{
		int nbRemb = 0;

		MembresServiceBdd ms = MembresServiceBddService.findById(ut.getUID());
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
						nbRemb++;
					}
				}
			}
			else//Service Finances
			{
				if (allRemboursements.get(i).getValidationfinances().equals(Statut.enAttente.statut()))//Demandes en attente
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
		for(int j=0;j<conges.size();j++) {
			uidConges = conges.get(j).getUid();
			if (validateurRoles.getRoleId() == Role.chefDeService.getRoleId() && MembresServiceBddService.findById(uidConges).getServiceId() == myServiceId)
			{
				if (conges.get(j).getValidationchefdeservice().equals(Statut.enAttente.statut()))//Demandes en attente
				{
					if (conges.get(j).getUid() != validateurRoles.getUid())//Check si pas autovalidation
					{
						nbConges++;
					}
				}
			}
			else//Service RH
			{
				if (conges.get(j).getValidationrh().equals(Statut.enAttente.statut()))//Demandes en attente
				{
					if (conges.get(j).getUid() != validateurRoles.getUid())//Check si pas autovalidation
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
