package sacha.kir;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sacha.kir.bdd.approle.AppRole;
import sacha.kir.bdd.approle.InterfaceAppRoleService;
import sacha.kir.bdd.appuser.InterfaceAppUserService;
import sacha.kir.bdd.conges.Conges;
import sacha.kir.bdd.conges.InterfaceCongesService;
import sacha.kir.bdd.justificatif.InterfaceJustificatifService;
import sacha.kir.bdd.justificatif.Justificatif;
import sacha.kir.bdd.membresmission.InterfaceMembresMissionService;
import sacha.kir.bdd.membresmission.MembresMission;
import sacha.kir.bdd.mission.InterfaceMissionService;
import sacha.kir.bdd.mission.Mission;
import sacha.kir.bdd.missionnote.InterfaceMissionsNoteService;
import sacha.kir.bdd.missionnote.MissionsNote;
import sacha.kir.bdd.note.InterfaceNoteService;
import sacha.kir.bdd.note.Note;
import sacha.kir.bdd.remboursement.InterfaceRemboursementService;
import sacha.kir.bdd.remboursement.Remboursement;
import sacha.kir.bdd.userrole.InterfaceUserRoleService;
import sacha.kir.bdd.userrole.UserRole;
import sacha.kir.bdd.utilisateur.InterfaceUtilisateurService;
import sacha.kir.bdd.utilisateur.Utilisateur;
import sacha.kir.form.CongeForm;
import sacha.kir.form.RemboursementForm;
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
    InterfaceMissionsNoteService MissionsNoteService;
    @Autowired
    InterfaceAppUserService AppUserService;
    @Autowired
    InterfaceAppRoleService AppRoleService;
	@Autowired
	InterfaceUserRoleService UserRoleService;
	
    @RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String welcomePage(Model model, Principal principal) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");

        if(principal == null)
        {
        	System.out.println("Pas d'utilisateur !!!");
        	return "Login";
        }
        else
        {
        	return "Login";//"welcomePage-Thibaut";
        }
        
    }
 
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
         
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
       
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
         
        return "adminPage";
    }
 
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
    	
        return "Login";
    }
 
    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }
 
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
 
        // (1) (en)
        // After user login successfully.
        // (vi)
        // Sau khi user login thanh cong se co principal
        String userName = principal.getName();
 
        System.out.println("User Name: " + userName);
 
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
 
        return "userInfoPage";
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
 
        }
 
        return "403Page";
    }
    
    
    @RequestMapping("/showCities")
    public String findCities(Model model) {
        
        List<Utilisateur> cities = (List<Utilisateur>) UtilisateurService.findAll();
        for (int i = 0;i<cities.size();i++)
        {
        	System.out.println(cities.get(i));
        }
        model.addAttribute("cities", cities);
        
        return "loginPage";
    }
    
    @RequestMapping("/addUser")
    public String addUser(Model model)
    {      
    	System.out.println(UtilisateurService.findbyname("Test"));
        return "loginPage";
    }
    
    @RequestMapping("/addMission")
    public String addMission(Model model)
    {
    	MissionService.addMission();

    	List<Mission> cs = MissionService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @RequestMapping("/addMembresMission")
    public String addMembresMission(Model model)
    {
    	MembresMissionService.addMembresMission();

    	List<MembresMission> cs = MembresMissionService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @RequestMapping("/addJustificatif")
    public String addJustificatif(Model model)
    {
    	JustificatifService.addJustificatif();

    	List<Justificatif> cs = JustificatifService.findAll();
    	for (int i = 0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @RequestMapping("/addRemboursement")
    public String addRemboursement(Model model)
    {
    	RemboursementService.addRemboursement();

    	List<Remboursement> cs = RemboursementService.findAll();
    	for (int i = 0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @RequestMapping("/addNote")
    public String addNote(Model model)
    {
    	NoteService.addNote();

    	List<Note> cs = NoteService.findAll();
    	for (int i = 0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @RequestMapping("/addMissionsNote")
    public String addMissionsNote(Model model)
    {
    	MissionsNoteService.addNote();

    	List<MissionsNote> cs = MissionsNoteService.findAll();
    	for (int i = 0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @GetMapping("/adminAdd")
    public String showForm(UserForm userForm) {
        return "form";
    }

    @PostMapping("/adminAdd")
    public String checkPersonInfo(@Valid UserForm userForm, BindingResult bindingResult,Model model) {
    	
        if (bindingResult.hasErrors()) {
            return "welcomePage-Thibaut";
        }
        
        System.out.println(userForm.getDateNaissance());
        System.out.println(userForm.getJoursCongesRest());
        System.out.println(userForm.getMdp());
        System.out.println(userForm.getNom());
        System.out.println(userForm.getNumTel());
        System.out.println(userForm.getPrenom());
        System.out.println(userForm.getRole());
        
        Utilisateur u = UtilisateurService.findPrenomNom(userForm.getNom(),userForm.getPrenom());
        if (u != null)//Utilisateur avec ce nom et prenom existe deja
        {
        	model.addAttribute("userExists", "Cet utilisateur existe deja");
        	return "welcomePage-Thibaut";
        }
        //Sinon on met le user dans la bd.
        Utilisateur user = new Utilisateur();
        user.setDateNaissance(userForm.getDateNaissance());
        user.setJoursCongesRestants(userForm.getJoursCongesRest());
        user.setNom(userForm.getNom());
        user.setNumeroTel(userForm.getNumTel());
        user.setPrenom(userForm.getPrenom());
        
        int maxId = UtilisateurService.getMaxId();//On recupere ID le plus haut de la table.
        user.setUID((long) (maxId + 1));        
        UtilisateurService.addUser(user);
        
        sacha.kir.bdd.appuser.AppUser appUser = new sacha.kir.bdd.appuser.AppUser();
        appUser.setUser_id((long) (maxId + 1));
		appUser.setEncrypted_password(EncrytedPasswordUtils.encryptePassword(userForm.getMdp()));
		appUser.setUser_name(userForm.getPrenom() + "." + userForm.getNom());
		
		AppUserService.addAppUser(appUser);
        
        UserRole userRole = new UserRole();
        userRole.setUser_id((long) (maxId + 1));
        userRole.setRole_id((long) 2);//Admin par defaut TODO a changer
        userRole.setId((long) UserRoleService.getMaxId() + 1);

        UserRoleService.addUserRole(userRole);

        return "welcomePage-Thibaut";
    }
    
    @GetMapping("/addConges")
    public String addConges(CongeForm congeForm) {

    	List<Conges> cs = CongesService.findAll();
    	for (int i =0; i<cs.size(); i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "addCongesPage";
    }
    
    @PostMapping("/addConges")
    public String submitConges(@Valid CongeForm congeForm, BindingResult bindingResult, Model model) {
    	//CongesService.addConges();
    	
    	System.out.println(congeForm.getUsername());
        System.out.println(congeForm.getDateDebut());
        System.out.println(congeForm.getDateFin());
    	
    	return "login";
    }
    
    @RequestMapping("/addAppUser")
    public String addAppUser(Model model)
    {
    	//AppUserService.addAppUser();

    	List<sacha.kir.bdd.appuser.AppUser> cs = AppUserService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @RequestMapping("/addAppRole")
    public String addAppRole(Model model)
    {
    	AppRoleService.addAppRole();

    	List<AppRole> cs = AppRoleService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @RequestMapping("/addUserRole")
    public String addUserRole(Model model)
    {
    	//UserRoleService.addUserRole();

    	List<UserRole> cs = UserRoleService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
    
    @RequestMapping("/Accueil")
    public String accueil(Model model,Principal principal,UserForm userForm)
    {
    	String prenomnom = principal.getName();
    	String[] names = prenomnom.split("\\.");
    	model.addAttribute("WelcomeMsg", "Bienvenue " + names[0] + " " + names[1]);
    	
    	/**** NOTIFICATION ***********/
        LocalDate localDate = LocalDate.now();
    	System.out.println(localDate.getDayOfMonth());
    	System.out.println("Mois : " + localDate.getMonthValue());
        System.out.println(DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate));
        if (localDate.getDayOfMonth() >= 14)
        {   
	        Utilisateur ut = UtilisateurService.findPrenomNom(names[1], names[0]);
	        long uid = ut.getUID();
	        boolean noteExiste = false;
	        List<Note> notes = NoteService.findAll();
	        for (int i = 0;i < notes.size();i++)
	        {
	        	if (notes.get(i).getUid() == uid)
	        	{
	        		DateUtilities du = new DateUtilities();
	        		int moisValue = du.moisToInt(notes.get(i).getMois());
	        		if (localDate.getMonthValue() != 1)
	        		{
	        			if (localDate.getMonthValue() == moisValue + 1)
	        			{
	        				noteExiste = true;
	        				break;
	        			}
	        		}
	        	}
	        }
	        if (!noteExiste)
	        {
	        	System.out.println("Pas de note pour le mois davant !");
	        	model.addAttribute("NotifNote", "Vous n\'avez pas de note de frais pour le mois precedent !");
	        }
	        else
	        {
	        	System.out.println("Une note pour le mois davant !");
	        }
        }
        /**** NOTIFICATION ***********/

        return "welcomePage-Thibaut";
    }
    
    @RequestMapping("/adminShow")
    public String adminShow(Model model)
    {
    	List<Utilisateur> cs = UtilisateurService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
    	model.addAttribute("listUsers", cs);
        return "showUsers";
    }
    
    @RequestMapping(path="/showUserDetails/{id}")
    public String getMessage(@PathVariable("id") long id,Model model,UserForm userForm) 
    {
        System.out.println("ID : " + id);
        
        Utilisateur u = UtilisateurService.findById(id);
        UserRole ur = UserRoleService.findById(id);
        long roleId = ur.getRole_id();
        AppRole ar = AppRoleService.findById(roleId);
        
        userForm.setDateNaissance(u.getDateNaissance());
        userForm.setJoursCongesRest((int) u.getJoursCongesRestants());
        userForm.setNom(u.getNom());
        userForm.setNumTel(u.getNumeroTel());
        userForm.setPrenom(u.getPrenom());
        userForm.setRole(ar.getRole_name());
  		userForm.setUid(id);

        model.addAttribute("user", userForm);
        return "showUserDetails";
    }
    
    @GetMapping("/roleChanged")
    public String roleChanged(UserForm userForm,Model model) {
    	if (userForm.getRole().contains("NoChanges"))
    	{
    		model.addAttribute("change", "non change");
    		return "roleChanged";
    	}
    	AppRole ar = AppRoleService.findByRole(userForm.getRole());
    	System.out.println(ar.getRole_id());
    	System.out.println("userForm.getUid() " + userForm.getUid() + userForm.getRole());
    	UserRoleService.updateRole(userForm.getUid(), ar.getRole_id());
    	System.out.println("APRES updateRole");
    	
    	model.addAttribute("change", "change");
    	
        return "roleChanged";
    }
    
    // TEST FORMULAIRE DEMANDE REMBOURSEMENT
    /* *************************************************** */
    @GetMapping("/demandeRemboursement")
    public String remboursementForm(Model model, Principal principal) {
    	String[] names = principal.getName().split("\\.");
    	Long userId = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	List<Long> missionsIDs = MembresMissionService.findMissionsByUID(userId);
    	for(long missionID : missionsIDs)
    	{
    		System.out.println("Mission ID : " + missionID);
    	}
        model.addAttribute("remboursementForm", new RemboursementForm());
        //model.addAttribute("missionsAssociated", attributeValue);
        return "remboursementForm";
        
        //List<Mission> listm = MissionService.findAll();
    }
    /* *************************************************** */
}
