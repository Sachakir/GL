package sacha.kir;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import sacha.kir.bdd.approle.AppRole;
import sacha.kir.bdd.approle.InterfaceAppRoleService;
import sacha.kir.bdd.conges.Conges;
import sacha.kir.bdd.conges.InterfaceCongesService;
import sacha.kir.bdd.userrole.InterfaceUserRoleService;
import sacha.kir.bdd.userrole.UserRole;
import sacha.kir.bdd.utilisateur.InterfaceUtilisateurService;
import sacha.kir.bdd.utilisateur.Utilisateur;
import sacha.kir.form.CongesV2;
import sacha.kir.form.UserForm;

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
	
	@GetMapping("/validationConges")
    public String addConges(Principal principal,Model model) 
	{
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	UserRole ur = UserRoleService.findById(ut.getUID());
    	AppRole ar = AppRoleService.findById(ur.getRole_id());
    	System.out.println("Le role est : " + ar.getRole_name());
    	if (ar.getRole_name().contains("RH")) 
    	{
        	model.addAttribute("pageValidation","RH");
    	}
    	else
    	{
    		model.addAttribute("pageValidation","Chef");
    	}
    	long roleId = -1;//Le id du role a selectionner pour les demandes de conges
    	if (ar.getRole_name().contains("ChefInfo"))//TODO pour autres chefs de services, rh
    	{
    		ar = AppRoleService.findByRole("UserInfo");
    		roleId = ar.getRole_id();
    	}
    	
    	List<Conges> cs = CongesService.findAll();
    	List<CongesV2> conges = new ArrayList<CongesV2>();
    	
    	for (int i =0; i<cs.size(); i++)
    	{
    		if (UserRoleService.findById(cs.get(i).getUid()).getRole_id() == roleId)
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
    	}
    	model.addAttribute("listConges",conges);
        return "showConges";
    }
	
	@RequestMapping(path="/Chef/{id}")
    public String getMessage(@PathVariable("id") long id,CongesV2 conges,Model model) 
    {
		
		return "showCongesDetails";
    }
}
