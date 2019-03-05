package root.bdd.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import root.bdd.appuser.InterfaceAppUserService;
import root.bdd.conges.InterfaceCongesService;
import root.bdd.justificatif.InterfaceJustificatifService;
import root.bdd.membresmission.InterfaceMembresMissionService;
import root.bdd.membresservice.InterfaceMembresServiceBddService;
import root.bdd.mission.InterfaceMissionService;
import root.bdd.note.InterfaceNoteService;
import root.bdd.notif.InterfaceNotifService;
import root.bdd.remboursement.InterfaceRemboursementService;
import root.bdd.remboursementsnote.InterfaceRemboursementsNoteService;
import root.bdd.services.InterfaceServiceBddService;
import root.bdd.services.ServiceBdd;
import root.bdd.utilisateur.InterfaceUtilisateurService;
import root.forms.service.ServiceForm;
import root.forms.utilisateur.UserForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/administration/gestion-services")
public class GestionServicesController {
	
	// TODO IMPORTANT A METTRE AU DEBUT DE CHAQUE CONTROLLER
	@ModelAttribute("authentication")
    public List<String> setRoles(Authentication authentication) {
		// Si l'utilisateur est loggé, on insère dans le model ses roles
		if(authentication != null) {
			List<String> toReturn = new ArrayList<String>();
			for(GrantedAuthority gr : authentication.getAuthorities()) {
				toReturn.add(gr.toString());
			}
	        return toReturn;
		}
		return null;
    }
	
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
	InterfaceServiceBddService ServiceBddService;
	@Autowired
	InterfaceMembresServiceBddService MembresServiceBddService;
	@Autowired
	InterfaceNotifService NotifService;
	
	private final String root = "/administration/gestion-services";
	
	@RequestMapping(value = {"/", ""})
    public String showServices(Model model, Principal principal)
    {
    	// Informations pour les services disponibles (liste des services)
    	List<Long> services_ids = ServiceBddService.getServiceIdList();
    	Map<Long, String> services = ServiceBddService.getAllServiceNames();
    	
    	model.addAttribute("services_ids", services_ids);
    	model.addAttribute("services", services);
    	
    	if(!model.containsAttribute("serviceForm")) {
    		model.addAttribute("serviceForm", new ServiceForm());
    	}
    	
        return "admin/services/showServices";
    }
    
	@PostMapping("/create")
    public @ResponseBody Map<String, String> createService (@Valid ServiceForm serviceForm, BindingResult result, Model model, Principal principal) {
		Map<String, String> errors = new HashMap<String, String>();
		
		ServiceBdd service = ServiceBddService.findByName(serviceForm.getNom());
		// Service déja existant
		if(service != null) {
			System.out.println("It exists");
			errors.put("#service_exists_error", "Le service entré existe déja");
		}
		
		if(result.hasErrors()) {
			 List<FieldError> fieldErrors = result.getFieldErrors();
        	 for(FieldError f : fieldErrors) {
        		 errors.put("#" + f.getField() + "_error", f.getDefaultMessage());
        	 }
		}
		
		if(errors.isEmpty())
		{
			ServiceBddService.addServiceBdd(serviceForm.getNom());
		}
		
        return errors;
    }
    
    @RequestMapping("/delete")
    public String getMessage(@RequestParam(value="id", required=false) String idStr, Model model,UserForm userForm,Principal principal) 
    {
    	if(idStr != null) try
		{
			long id = Long.parseLong(idStr);
			ServiceBdd service = ServiceBddService.findById(id);
			
			if(service != null) {
				List<Long> membresService = MembresServiceBddService.getAllUidByServiceId(id);
				if (membresService.isEmpty())
				{
					ServiceBddService.deleteById(id);
					return "redirect:" + root;
				}
			}
		}
    	catch(NumberFormatException e) {
    		e.printStackTrace();
    	}
    	
    	return "forward:/notFound";
    }
}

