package sacha.kir;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import sacha.kir.bdd.approle.InterfaceAppRoleService;
import sacha.kir.bdd.appuser.InterfaceAppUserService;
import sacha.kir.bdd.conges.InterfaceCongesService;
import sacha.kir.bdd.justificatif.InterfaceJustificatifService;
import sacha.kir.bdd.membresmission.InterfaceMembresMissionService;
import sacha.kir.bdd.mission.InterfaceMissionService;
import sacha.kir.bdd.mission.Mission;
import sacha.kir.bdd.missionnote.InterfaceMissionsNoteService;
import sacha.kir.bdd.note.InterfaceNoteService;
import sacha.kir.bdd.remboursement.InterfaceRemboursementService;
import sacha.kir.bdd.userrole.InterfaceUserRoleService;
import sacha.kir.bdd.utilisateur.InterfaceUtilisateurService;
import sacha.kir.form.RemboursementForm;

@Controller
public class RemboursementController {
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
	
	// TEST FORMULAIRE DEMANDE REMBOURSEMENT
    /* *************************************************** */
    @GetMapping("/demandeRemboursement")
    public String remboursementForm(Model model, Principal principal) {
    	// Recuperation des missions assignees a l'user
    	String[] names = principal.getName().split("\\.");
    	Long userId = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	List<Long> missionsIDs = MembresMissionService.findMissionsByUID(userId);
    	Collections.sort(missionsIDs);
    	List<Mission> userMissions = MissionService.findMissionsById(missionsIDs);
    	
    	// Ajout des attributs pour la mise en forme du formulaire
        model.addAttribute("remboursementForm", new RemboursementForm());
        model.addAttribute("missions", userMissions);
        
        // Appel de la page du formulaire
        return "remboursementForm";
    }
    
    
    @PostMapping("/demandeRemboursement")
    public String handleFileUpload(Model model, Principal principal, @Valid RemboursementForm remboursementForm, Errors errors) {
    	if(errors.hasErrors())
    	{
    		for(ObjectError e : errors.getAllErrors())
    		{
    			System.out.println(e.getDefaultMessage());
    		}
    		System.out.println();
    		System.out.println(remboursementForm.getMission());
    		
    		// Recuperation des missions assignees a l'user
        	String[] names = principal.getName().split("\\.");
        	Long userId = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
        	List<Long> missionsIDs = MembresMissionService.findMissionsByUID(userId);
        	Collections.sort(missionsIDs);
        	List<Mission> userMissions = MissionService.findMissionsById(missionsIDs);
        	
        	model.addAttribute("remboursementForm", remboursementForm);
            model.addAttribute("missions", userMissions);
        	
        	return "remboursementForm";
    	}
    	else
    	{
    		//JustificatifService.storeJustificatif(file);
            //model.addAttribute("message",
                 //   "You successfully uploaded " + file.getOriginalFilename() + "!");
    		
    		return "redirect:/Accueil";
    	}
    }
}
