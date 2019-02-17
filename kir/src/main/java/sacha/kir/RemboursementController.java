package sacha.kir;

import java.security.Principal;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
import sacha.kir.bdd.note.InterfaceNoteService;
import sacha.kir.bdd.note.Note;
import sacha.kir.bdd.remboursement.InterfaceRemboursementService;
import sacha.kir.bdd.remboursement.Remboursement;
import sacha.kir.bdd.remboursementsnote.InterfaceRemboursementsNoteService;
import sacha.kir.bdd.userrole.InterfaceUserRoleService;
import sacha.kir.bdd.utilisateur.InterfaceUtilisateurService;
import sacha.kir.form.RemboursementForm;

@Controller
@RequestMapping("/remboursements")
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
    InterfaceRemboursementsNoteService RemboursementsNoteService;
    @Autowired
    InterfaceAppUserService AppUserService;
    @Autowired
    InterfaceAppRoleService AppRoleService;
	@Autowired
	InterfaceUserRoleService UserRoleService;
	
	@GetMapping(value = {"/", ""})
	public String displayNotesFrais(Model model, Principal principal) {
		String[] names = principal.getName().split("\\.");
    	Long userId = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	
		List<Remboursement> remboursements = RemboursementService.getAllByIdAsc(userId, 2);
		for(Remboursement r : remboursements) {
			System.out.println("Remboursement n" + r.getDemande_id() + ", timestamp = " + r.getTimestamp());
		}
		return "remboursements";
	}
	
	@GetMapping("/demande-remboursement")
    public String remboursementForm(@RequestParam(value = "mois", required = false) String monthRequested, Model model, Principal principal) {
		if(monthRequested == null) {
    		monthRequested = "actuel";
    	}
    	else if(!monthRequested.equals("actuel") && !monthRequested.equals("precedent")) {
    		return "redirect:/remboursements/demande-remboursement";
    	}
		
		// Deux cas : soit la note de frais pour l'utilisateur et pour le mois est crée,
    	// 		      soit elle ne l'est pas dans quel cas on en crée une
    	
    	// Recuperation des missions assignees a l'user
    	String[] names = principal.getName().split("\\.");
    	Long userId = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	List<Long> missionsIDs = MembresMissionService.findMissionsByUID(userId);
    	Collections.sort(missionsIDs);
    	List<Mission> userMissions = MissionService.findMissionsById(missionsIDs);
    	
    	// Ajout des attributs pour la mise en forme du formulaire
        model.addAttribute("remboursementForm", new RemboursementForm());
        model.addAttribute("missions", userMissions);
        model.addAttribute("monthRequested", monthRequested);
        
        // Recherche d'une note de frais poir le mois actuel
        LocalDate localDate = LocalDate.now();
        int moisInt = localDate.getMonthValue();
        int yearInt = localDate.getYear();
        
        if(monthRequested.equals("precedent")) {
        	if(moisInt == 1)
        	{
        		moisInt = 12;
        		yearInt--;
        	}
        	else moisInt--;
        }
        
        String moisStr = (moisInt < 10 ? "0" + moisInt : moisInt) + "/" + yearInt;
        
        String monthToDisplay = (moisInt == 4 || moisInt == 8 || moisInt == 10 ? "d'" : "de ");
        String moisNote = new DateFormatSymbols().getMonths()[moisInt-1];
        moisNote = moisNote.substring(0, 1).toUpperCase() + moisNote.substring(1);
        monthToDisplay += moisNote + " " + yearInt;
        
        model.addAttribute("monthToDisplay", monthToDisplay);
        
        // Verification de l'existence d'une note de frais
        Note n = NoteService.findNoteByMonthAndUID(moisStr, userId);
        if(n == null) //Creer une note de frais
        {
        	String message = "La note du mois ";
        	if(monthRequested.equals("actuel")) {
        		message += "actuel";
        	}
        	else {
        		message += "précédent";
        	}
        	message += " n'a pas été créée. Voulez vous la créer ou annuler votre demande de remboursement ?";
        	
        	model.addAttribute("noNoteFrais", "");
        	model.addAttribute("messageConfirmation", message);
        	model.addAttribute("urlNote", "/remboursements/creer-note-frais?mois=" + monthRequested);
        	model.addAttribute("urlRedirect", "/Accueil");	
        }
        
        // Appel de la page du formulaire
        return "remboursementForm";
    }

	@PostMapping("/demande-remboursement")
    public String sendRemboursementForm(Model model, Principal principal, @RequestParam("file") MultipartFile file, @Valid RemboursementForm remboursementForm, Errors errors) {
    	// Recuperation des missions assignees a l'user
    	String monthRequested = remboursementForm.getMoisNote();
    	
    	String[] names = principal.getName().split("\\.");
    	Long userId = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
    	LocalDate localDate = LocalDate.now();
    	int moisNow = localDate.getMonthValue();
		int yearNow = localDate.getYear();
    	
    	try {
    		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    		
    		// Verification de l'entree de la date (dans le mois de la note de frais)
			df.parse(remboursementForm.getDate());
			LocalDate date = LocalDate.parse(remboursementForm.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			
			if(monthRequested.equals("precedent")) {
				if(moisNow == 1) {
					moisNow = 12;
					yearNow--;
				}
				else {
					moisNow--;
				}
			}
			if(moisNow != date.getMonthValue() || yearNow != date.getYear()) {
				errors.rejectValue("date", "Pattern", "Date non comprise dans le mois " + monthRequested);
			}
    	}
    	catch(DateTimeParseException e) {
			errors.rejectValue("date", "Pattern", "Date incorrecte");
    	}
    	
    	if(errors.hasErrors())
    	{
    		List<Long> missionsIDs = MembresMissionService.findMissionsByUID(userId);
        	Collections.sort(missionsIDs);
        	List<Mission> userMissions = MissionService.findMissionsById(missionsIDs);
        	
            model.addAttribute("missions", userMissions);
            model.addAttribute("monthRequested", monthRequested);
            
            String monthToDisplay = (moisNow == 4 || moisNow == 8 || moisNow == 10 ? "d'" : "de ");
            String moisNote = new DateFormatSymbols().getMonths()[moisNow-1];
            moisNote = moisNote.substring(0, 1).toUpperCase() + moisNote.substring(1);
            monthToDisplay += moisNote + " " + yearNow;
            
            model.addAttribute("monthToDisplay", monthToDisplay);
        	
        	return "remboursementForm";
    	}
    	else
    	{	
			// Creation du remboursement
    		Remboursement r = new Remboursement();
    		r.setTitre(remboursementForm.getTitre());
    		r.setMission_id(Long.parseLong(remboursementForm.getMission()));
    		r.setUid(userId);
    		r.setDate(remboursementForm.getDate());
    		r.setMontant(Float.parseFloat(remboursementForm.getMontant()));
    		r.setMotif(remboursementForm.getMotif());
    		r.setValidationchefservice("En attente");
    		r.setValidationrh("En attente");
    		
    		// Stockage du fichier dans la BD
    		if(!file.isEmpty())
    		{
    			Justificatif j = JustificatifService.storeJustificatif(file);
    			r.setJustificatifid(j.getJustificatif_id());
    		}
    		else
    		{
    			r.setJustificatifid(null);
    		}
    		
    		// Ajout de la demande de remboursement à la BD
    		r = RemboursementService.addNewRemboursement(r);
    		
    		// Association de la demande avec la note de frais
    		String moisStr = (moisNow < 10 ? "0" + moisNow : moisNow) + "/" + yearNow;
    		Note n = NoteService.findNoteByMonthAndUID(moisStr, userId);
    		
    		RemboursementsNoteService.addRemboursementToNote(n.getNote_id(), r.getDemande_id());
    		
    		return "redirect:/Accueil";
    	}
    }
    
    @GetMapping("/creer-note-frais")
    public String createNoteFrais(@RequestHeader(value = "referer", required = false) final String referer, 
    							  @RequestParam(value = "mois", required = false) String monthRequested,
    							  Model model, Principal principal) {
    	if(referer == null || monthRequested == null)
    		return "redirect:/Accueil";
    	else 
    	{
    		String[] names = principal.getName().split("\\.");
        	Long userId = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
        	
        	LocalDate localDate = LocalDate.now();
            int moisInt = localDate.getMonthValue();
            int yearInt = localDate.getYear();
            String mois = "";
    		
    		if(monthRequested.equals("precedent")) {
    			if(moisInt == 1) {
    				moisInt = 12;
    				yearInt--;
    			}
    			else {
    				moisInt--;
    			}
    		}
    		else if(!monthRequested.equals("actuel")) {
    			return "redirect:/error";
    		}
    		
    		mois = (moisInt < 10 ? "0" + moisInt : moisInt) + "/" + yearInt;
    		NoteService.addNote(mois, userId);
    			
    		System.out.println(referer);
    		return "redirect:" + referer;
    	}
    }
    
    @GetMapping("/files")
    public ResponseEntity<byte[]> getPDF(@RequestParam(value = "file_id", required = true) int file_id) {
        HttpHeaders headers = new HttpHeaders();
        Justificatif j = JustificatifService.getFile(file_id);
    	if(j != null)
    	{
    		String filename = j.getFilename();
    		System.out.println(filename);
    		if(filename.endsWith(".pdf")) {
    			headers.setContentType(MediaType.parseMediaType("application/pdf"));
    		}
    		else if (filename.endsWith(".html"))  {
    			headers.setContentType(MediaType.parseMediaType("text/html"));
    		}
    		else if (filename.endsWith(".txt"))  {
    			headers.setContentType(MediaType.parseMediaType("text/plain"));
    		}
    		else if (filename.endsWith(".gif"))  {
    			headers.setContentType(MediaType.parseMediaType("image/gif"));
    		}
    		else if (filename.endsWith(".jpeg"))  {
    			headers.setContentType(MediaType.parseMediaType("image/jpeg"));
    		}
    		else if (filename.endsWith(".png"))  {
    			headers.setContentType(MediaType.parseMediaType("image/png"));
    		}
    		else {
    			headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
    		}
    		
    		headers.add("content-disposition", "inline;filename=" + filename);
    		headers.setContentDispositionFormData(filename, filename);
    		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(j.getPdf(), headers, HttpStatus.OK);
            return response;
    	}
    	
    	else {
    		ResponseEntity<byte[]> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    		return response;
    	}
    }
}
