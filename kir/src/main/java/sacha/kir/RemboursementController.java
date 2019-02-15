package sacha.kir;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import sacha.kir.bdd.approle.InterfaceAppRoleService;
import sacha.kir.bdd.appuser.InterfaceAppUserService;
import sacha.kir.bdd.conges.InterfaceCongesService;
import sacha.kir.bdd.justificatif.InterfaceJustificatifService;
import sacha.kir.bdd.justificatif.Justificatif;
import sacha.kir.bdd.membresmission.InterfaceMembresMissionService;
import sacha.kir.bdd.mission.InterfaceMissionService;
import sacha.kir.bdd.mission.Mission;
import sacha.kir.bdd.missionnote.InterfaceMissionsNoteService;
import sacha.kir.bdd.note.InterfaceNoteService;
import sacha.kir.bdd.remboursement.InterfaceRemboursementService;
import sacha.kir.bdd.remboursement.Remboursement;
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
    public String handleFileUpload(Model model, Principal principal, @RequestParam("file") MultipartFile file, @Valid RemboursementForm remboursementForm, Errors errors) {
    	// Recuperation des missions assignees a l'user
    	String[] names = principal.getName().split("\\.");
    	Long userId = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	if(errors.hasErrors())
    	{
    		List<Long> missionsIDs = MembresMissionService.findMissionsByUID(userId);
        	Collections.sort(missionsIDs);
        	List<Mission> userMissions = MissionService.findMissionsById(missionsIDs);
        	
            model.addAttribute("missions", userMissions);
        	
        	return "remboursementForm";
    	}
    	else
    	{
    		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    		df.setLenient(false);
    		
    		try {
    			df.parse(remboursementForm.getDate());
    			
    			Remboursement r = new Remboursement();
        		r.setTitre(remboursementForm.getTitre());
        		r.setMission_id(Long.parseLong(remboursementForm.getMission()));
        		r.setUid(userId);
        		r.setDate(remboursementForm.getDate());
        		r.setMontant(Float.parseFloat(remboursementForm.getMontant()));
        		r.setMotif(remboursementForm.getMotif());
        		r.setValidationchefservice("En attente");
        		r.setValidationrh("En attente");
        		
        		if(!file.isEmpty())
        		{
        			Justificatif j = JustificatifService.storeJustificatif(file);
        			r.setJustificatifid(j.getJustificatif_id());
        		}
        		else
        		{
        			r.setJustificatifid(null);
        		}
        		
        		RemboursementService.addNewRemboursement(r);
        		
        		return "redirect:/Accueil";
    		}
    		catch(ParseException e) {
    			errors.rejectValue("date", "Pattern", "Date incorrecte");
    			
    			List<Long> missionsIDs = MembresMissionService.findMissionsByUID(userId);
            	Collections.sort(missionsIDs);
            	List<Mission> userMissions = MissionService.findMissionsById(missionsIDs);
            	
                model.addAttribute("missions", userMissions);
            	
            	return "remboursementForm";
    		}
    	}
    }
}
