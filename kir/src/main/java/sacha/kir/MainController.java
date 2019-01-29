package sacha.kir;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

import java.util.List;

import javax.validation.Valid;
 
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
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "welcomePage";
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
        //UtilisateurService.killBill();
    	System.out.println(UtilisateurService.findbyname("Test"));
        return "loginPage";
    }
   
    
    @RequestMapping("/addConges")
    public String addConges(Model model)
    {
    	CongesService.addConges();

    	List<Conges> cs = CongesService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
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
    public String showForm(PersonForm personForm) {
        return "form";
    }

    @PostMapping("/adminAdd")
    public String checkPersonInfo(@Valid PersonForm personForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "form";
        }
        int a = personForm.getAge();
        System.out.println(a + " " + personForm.getName());

        return "results";
    }
    
    @RequestMapping("/addAppUser")
    public String addAppUser(Model model)
    {
    	AppUserService.addAppUser();

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
    	UserRoleService.addUserRole();

    	List<UserRole> cs = UserRoleService.findAll();
    	for (int i =0;i < cs.size();i++)
    	{
    		System.out.println(cs.get(i).toString());
    	}
        return "loginPage";
    }
}
