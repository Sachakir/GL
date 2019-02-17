package sacha.kir;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sacha.kir.bdd.approle.AppRole;
import sacha.kir.bdd.approle.InterfaceAppRoleService;
import sacha.kir.bdd.appuser.AppUserService;
import sacha.kir.bdd.appuser.InterfaceAppUserService;
import sacha.kir.bdd.conges.Conges;
import sacha.kir.bdd.conges.InterfaceCongesService;
import sacha.kir.bdd.membresmission.InterfaceMembresMissionService;
import sacha.kir.bdd.membresmission.MembresMission;
import sacha.kir.bdd.mission.InterfaceMissionService;
import sacha.kir.bdd.mission.Mission;
import sacha.kir.bdd.remboursement.InterfaceRemboursementService;
import sacha.kir.bdd.remboursement.Remboursement;
import sacha.kir.bdd.userrole.InterfaceUserRoleService;
import sacha.kir.bdd.userrole.UserRole;
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
    	else if (ar.getRole_name().contains("ChefFinances"))
    	{
    		ar = AppRoleService.findByRole("UserFinances");
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
		UserRole ur = UserRoleService.findById(id);
		if (ur != null)
		{
			System.out.println(ur.getId());
			System.out.println(ur.getRole_id());
			System.out.println(ur.getUser_id());
		}
		MembresMissionService.deleteMembresMission(id);
		CongesService.deleteConges(id);
		
		UserRoleService.deleteUserRole(id);
		AppUserService.deleteAppUser(id);
		UtilisateurService.deleteUser(id);
		
		System.out.println("DELETED " + id);
		
		List<Utilisateur> cs = UtilisateurService.findAll();
    	model.addAttribute("listUsers", cs);
    	String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	model.addAttribute("notAdmin",UtilisateurService.findPrenomNom(names[1], names[0]).getUID());
		return "redirect:/adminShow";
    }
	
	@GetMapping("/newMission")
    public String addMission(Mission mission,Model model)
    {
		AppRole chefsInfos = AppRoleService.findByRole("ChefInfo");
		AppRole chefsFinances = AppRoleService.findByRole("ChefFinances");
		
		List<UserRole> userroles = UserRoleService.findAll();
    	List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
    	List<Utilisateur> usersMembres = UtilisateurService.findAll();
    	
    	for (int i = 0;i < userroles.size();i++)
    	{
    		if (userroles.get(i).getRole_id() == chefsInfos.getRole_id() || userroles.get(i).getRole_id() == chefsFinances.getRole_id())
    		{
    			Utilisateur utilisateur = UtilisateurService.findById(userroles.get(i).getUser_id());
    			Utilisateur u = new Utilisateur();
    			u.setPrenom(utilisateur.getPrenom());
    			u.setNom(utilisateur.getNom());
    			u.setUID(utilisateur.getUID());
    			utilisateurs.add(u);
    		}
    	}
    	
    	UserList ul = new UserList(); 	
    	model.addAttribute("users",utilisateurs);
    	model.addAttribute("userlist",ul);
    	model.addAttribute("membres",usersMembres);
        return "newMissionPage";
    }
	
	@PostMapping("/newMission")
    public String checkMission(@Valid Mission mission,@ModelAttribute("userlist") UserList userlist)
    {
		//Creation de la mission
		
		System.out.println(mission.getResponsable_id());
		System.out.println(mission.getDate_debut());
		System.out.println(mission.getDate_fin());
		System.out.println(mission.getTitre());
		System.out.println(mission.getDescription());
		int maxid = MissionService.getMaxMissionId();
		mission.setMission_id((long) (maxid + 1));
		
		MissionService.addMission(mission);
		//Ajout des membres
		System.out.println(userlist.getUserList().size());
		MembresMission membresMission = new MembresMission();

		for (int i = 0;i < userlist.getUserList().size();i++)
		{
			membresMission.setMission_id((long) (maxid + 1));
			membresMission.setMembre_uid(userlist.getUserList().get(i).getUID());
			MembresMissionService.addMembresMission(membresMission);
		}
		
        return "redirect:/Accueil";
    }
	
	@RequestMapping("/validationNDF")
    public String validationNDF(Model model)
    {
		List<Remboursement> rembs = RemboursementService.findAll();
		List<RemboursementV2> rv2 = new ArrayList<RemboursementV2>();
		for (int i =0;i<rembs.size();i++)
		{
			Utilisateur tmpUs = UtilisateurService.findById(rembs.get(i).getUid());
			
			
			RemboursementV2 tmpRem = new RemboursementV2();
			tmpRem.setDate(rembs.get(i).getDate());
			tmpRem.setDemande_id(rembs.get(i).getDemande_id());
			tmpRem.setJustificatifid(rembs.get(i).getJustificatifid());
			tmpRem.setMission_id(rembs.get(i).getMission_id());
			tmpRem.setMontant(rembs.get(i).getMontant());
			tmpRem.setMotif(rembs.get(i).getMotif());
			tmpRem.setPrenomnom(tmpUs.getPrenom() + " " + tmpUs.getNom());
			tmpRem.setTitre(rembs.get(i).getTitre());
			tmpRem.setValidationchefservice(rembs.get(i).getValidationchefservice());
			tmpRem.setValidationrh(rembs.get(i).getValidationrh());
			if (rembs.get(i).getValidationchefservice().contains("Valide") && rembs.get(i).getValidationrh().contains("Valide"))
			{
				tmpRem.setEtatFinal("Valide");
			}
			else if (rembs.get(i).getValidationchefservice().contains("Refuse") && rembs.get(i).getValidationrh().contains("Refuse"))
			{
				tmpRem.setEtatFinal("Refuse");
			}
			else 
			{
				tmpRem.setEtatFinal("EnAttente");
			}
			rv2.add(tmpRem);
			
		}
		model.addAttribute("remboursements", rv2);
		return "validerndf";
    }
	
	
	@RequestMapping(path="/ValidationRemb/{id}")
    public String ValidationRemb(@PathVariable("id") long demandeId,Principal principal)
    {
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	UserRole ur = UserRoleService.findById(ut.getUID());
    	String role = AppRoleService.findById(ur.getRole_id()).getRole_name();
    	System.out.println(role);
    	if (role.contains("RH"))
    	{
    		RemboursementService.updateRHState(demandeId, "Valide");
    	}
    	else if (role.contains("Chef"))
    	{
    		RemboursementService.updateChefState(demandeId, "Valide");
    	}
    	else
    	{
    		System.out.println("Vous ne pouvez pas valider de demandes de remboursement votre role est : " + role);
    	}
		return "redirect:/validationNDF";
    }
	
	
	@RequestMapping(path="/RefusRemb/{id}")
    public String RefusRemb(@PathVariable("id") long demandeId,Principal principal)
    {
		String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
    	UserRole ur = UserRoleService.findById(ut.getUID());
    	String role = AppRoleService.findById(ur.getRole_id()).getRole_name();
    	System.out.println(role);
    	if (role.contains("RH"))
    	{
    		RemboursementService.updateRHState(demandeId, "Refuse");
    	}
    	else if (role.contains("Chef"))
    	{
    		RemboursementService.updateChefState(demandeId, "Refuse");
    	}
    	else
    	{
    		System.out.println("Vous ne pouvez pas refuser de demandes de remboursement votre role est : " + role);
    	}
		return "redirect:/validationNDF";
    }
	
	@RequestMapping("/parametres")
    public String parametres()
    {
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
}
