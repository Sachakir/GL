package sacha.kir;

import java.security.Principal;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.PathVariable;
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
    	
		List<Note> listeNotes = NoteService.findAllById(userId);
		Map<Long, String> moisNotes = new HashMap<Long, String>();
		
		// Recuperation des mois pour les notes de frais modifiables
		LocalDate localDate = LocalDate.now();
    	int moisNow = localDate.getMonthValue();
		int yearNow = localDate.getYear();
		int moisPrecedent = moisNow-1;
		int yearPrecedent = yearNow;
		if(moisPrecedent == 0) {
			moisPrecedent = 12;
			yearPrecedent--;
		}
		String moisNowStr = (moisNow < 10 ? "0" + moisNow : moisNow) + "/" + yearNow;
		String moisPrecedentStr = (moisPrecedent < 10 ? "0" + moisPrecedent : moisPrecedent) + "/" + yearPrecedent;
		
		Note noteMoisActuel = null;
		Note noteMoisPrecedent = null;
		
		// Recherche des notes modifiables et remplacement des intitules des mois
		for(Note n : listeNotes) {
			if(moisNowStr.equals(n.getMois())) {
				noteMoisActuel = n;
			}
			else if(moisPrecedentStr.equals(n.getMois())) {
				noteMoisPrecedent = n;
			}
			
            int moisNoteInt = Integer.parseInt(n.getMois().substring(0, 2));
            int yearNoteInt = Integer.parseInt(n.getMois().substring(3, 7));
			String moisNote = new DateFormatSymbols().getMonths()[moisNoteInt-1];
            moisNote = moisNote.substring(0, 1).toUpperCase() + moisNote.substring(1);
            moisNote += " " + yearNoteInt;
            
            moisNotes.put(n.getNote_id(), moisNote);
            n.setMois(n.getMois().substring(0, 2) + "-" + n.getMois().substring(3));
			
			if(noteMoisActuel != null && noteMoisPrecedent != null)
				break;
		}
		
		if(noteMoisActuel != null) {
			listeNotes.remove(noteMoisActuel);
		}
		if(noteMoisPrecedent != null) {
			listeNotes.remove(noteMoisPrecedent);
		}
		
		// Recuperation des missions associees a la note de frais de ce mois et leurs remboursements
		if(noteMoisActuel != null)
		{
			List<Mission> missions = new ArrayList<Mission>();
			Map<Long, List<Remboursement>> remboursementsMissions = new HashMap<Long, List<Remboursement>>();
			
			List<Long> remboursementsNoteIds = RemboursementsNoteService.findAllByNoteId(noteMoisActuel.getNote_id());
			if(!remboursementsNoteIds.isEmpty()) {
				List<Remboursement> remboursementsNote = RemboursementService.getAllByIdListAsc(remboursementsNoteIds);
		        
		        for(Remboursement r : remboursementsNote) {
		        	long missionId = r.getMission_id();
		        	 
		        	// Ajout d'une nouvelle mission si non existante
		        	if(!remboursementsMissions.containsKey(missionId)) {
		        		missions.add(MissionService.findMissionById(missionId));
		        		remboursementsMissions.put(missionId, new ArrayList<Remboursement>());
		        	}
		        	
		        	// Ajout de la demande de remboursement a la mission
		        	remboursementsMissions.get(missionId).add(r);
		        }
	        }
	        
	        model.addAttribute("missions", missions);
	        model.addAttribute("remboursementsMissions", remboursementsMissions);
		}
		
		model.addAttribute("listeNotes", listeNotes);
		model.addAttribute("moisNotes", moisNotes);
		model.addAttribute("noteMoisActuel", noteMoisActuel);
		model.addAttribute("noteMoisPrecedent", noteMoisPrecedent);
		model.addAttribute("moisActuel", moisNowStr);
		model.addAttribute("moisPrecedent", moisPrecedentStr);
		
		return "remboursements";
	}
	
	//^(0[1-9]|1[0-2])-[0-9]{4}$
	
	@GetMapping(value = "/note={mois:.+}")
	public String displayRemboursementsNote(@PathVariable String mois, Model model, Principal principal) {
		if(mois.length() > 2) {
			mois = mois.substring(0, 2) + '/' + mois.substring(3);
			
			String[] names = principal.getName().split("\\.");
	    	Long userId = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
	    	Note note = NoteService.findNoteByMonthAndUID(mois, userId);
	    	
			// Recuperation des missions associees a la note de frais de ce mois et leurs remboursements
			if(note != null)
			{
				int moisNoteInt = Integer.parseInt(note.getMois().substring(0, 2));
	            int yearNoteInt = Integer.parseInt(note.getMois().substring(3, 7));
				String moisNote = new DateFormatSymbols().getMonths()[moisNoteInt-1];
	            moisNote = moisNote.substring(0, 1).toUpperCase() + moisNote.substring(1);
	            moisNote += " " + yearNoteInt;
	            
				List<Mission> missions = new ArrayList<Mission>();
				Map<Long, List<Remboursement>> remboursementsMissions = new HashMap<Long, List<Remboursement>>();
				
				List<Long> remboursementsNoteIds = RemboursementsNoteService.findAllByNoteId(note.getNote_id());
				if(!remboursementsNoteIds.isEmpty()) {
					List<Remboursement> remboursementsNote = RemboursementService.getAllByIdListAsc(remboursementsNoteIds);
			        
			        for(Remboursement r : remboursementsNote) {
			        	long missionId = r.getMission_id();
			        	 
			        	// Ajout d'une nouvelle mission si non existante
			        	if(!remboursementsMissions.containsKey(missionId)) {
			        		missions.add(MissionService.findMissionById(missionId));
			        		remboursementsMissions.put(missionId, new ArrayList<Remboursement>());
			        	}
			        	
			        	// Ajout de la demande de remboursement a la mission
			        	remboursementsMissions.get(missionId).add(r);
			        }
		        }
		        
		        model.addAttribute("missions", missions);
		        model.addAttribute("remboursementsMissions", remboursementsMissions);
		        model.addAttribute("moisNote", moisNote);
		        
		        return "noteFrais";
			}
		}
		
		return "forward:/notFound";
	}
	
	@GetMapping(value = "/note={mois:.+}/remboursement_id={remboursement_id:.+}")
	public String displayRemboursement(@PathVariable String mois, @PathVariable String remboursement_id, Model model, Principal principal)
	{
		try {
			long remboursement_id_long = Long.parseLong(remboursement_id);
			String[] names = principal.getName().split("\\.");
	    	Long userId = UtilisateurService.findPrenomNom(names[1], names[0]).getUID();
	    	
			if(mois.length() > 2) {
				String moisDiff = mois.substring(0, 2) + '/' + mois.substring(3);
				Note note = NoteService.findNoteByMonthAndUID(moisDiff, userId);
				
				// On peut afficher la demande dans ce cas là
				if(RemboursementsNoteService.findByNoteIdAndDemandeId(note.getNote_id(), remboursement_id_long) != null) {
					Remboursement r = RemboursementService.findById(remboursement_id_long);
					Mission m = MissionService.findMissionById(r.getMission_id());
					
					model.addAttribute("remboursement", r);
					model.addAttribute("mission", m);
					
					return "remboursementDetail";
				}
			}
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		return "forward:/notFound";
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
        
        String monthToDisplay = "pour ";
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
            
            String monthToDisplay = "pour ";
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
    		r.setValidationfinances("En attente");
    		
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
