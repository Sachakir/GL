package root.bdd.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainErrorController /*implements ErrorController */{

	/*// TODO IMPORTANT A METTRE AU DEBUT DE CHAQUE CONTROLLER
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
	
    @Override
    public String getErrorPath() {
        return "/notFound";
    }
    
	@GetMapping("/notFound")
	public String notFound()
	{
		return "error/notFound";
	}*/
}
