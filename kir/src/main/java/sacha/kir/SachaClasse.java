package sacha.kir;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import sacha.kir.bdd.membresservice.InterfaceMembresServiceBddService;
import sacha.kir.bdd.membresservice.MembresServiceBdd;
import sacha.kir.bdd.membresservice.Role;
import sacha.kir.bdd.remboursement.InterfaceRemboursementService;
import sacha.kir.bdd.remboursement.Remboursement;
import sacha.kir.bdd.remboursement.Statut;
import sacha.kir.bdd.services.ServicesFixes;
import sacha.kir.bdd.utilisateur.Utilisateur;

public class SachaClasse {

	public int getNbRemb(Utilisateur ut,InterfaceMembresServiceBddService MembresServiceBddService,InterfaceRemboursementService RemboursementService)
	{
		int nbRemb = 0;
    	MembresServiceBdd ms = MembresServiceBddService.findById(46);
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
}
