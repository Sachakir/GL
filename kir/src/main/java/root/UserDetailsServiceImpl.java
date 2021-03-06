package root;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import root.bdd.membresservice.InterfaceMembresServiceBddService;
import root.bdd.membresservice.MembresServiceBdd;
import root.bdd.membresservice.Role;
import root.bdd.services.ServicesFixes;
 
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
    @Autowired
    private AppUserDAO appUserDAO;
    
    @Autowired
	InterfaceMembresServiceBddService MembresServiceBddService;
 
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AppUser appUser = this.appUserDAO.findUserAccount(userName);
 
        if (appUser == null) {
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }
        
        // Roles disponibles pour le filtrage des accès au pages WebSecurityConfig :
        // VALIDATOR_RH (accès aux pages de validation : Demandes de congés UNIQUEMENT
        // VALIDATOR_FIN (accès aux pages de validation : Notes de frais UNIQUEMENT
        // CHEF (accès aux pages de chefs)
        // ADMIN (accès aux pages d'administration) (peut se combiner aux autres)
        // Note : il faut ajouter ROLE_ derrière car lors de la vérification celle-ci se fait avec ceci en préfixe
        
        MembresServiceBdd membre = MembresServiceBddService.findById(appUser.getUserId());
        
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        
        // ROLE ADMIN
        if(membre.getIsAdmin()) {
        	grantList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        
        // ROLE CHEF
        if(membre.getRoleId() == Role.chefDeService.getRoleId()) {
        	grantList.add(new SimpleGrantedAuthority("ROLE_CHEF"));
        }
        
        // ROLE VALIDATOR_RH
        if(membre.getRoleId() == Role.chefDeService.getRoleId() || membre.getServiceId() == ServicesFixes.ressourcesHumaines.getServiceId()) {
        	grantList.add(new SimpleGrantedAuthority("ROLE_VALIDATOR_RH"));
        }
        	
        //ROLE VALIDATOR_FIN
        if(membre.getRoleId() == Role.chefDeService.getRoleId() || membre.getServiceId() == ServicesFixes.finances.getServiceId()) {
        	grantList.add(new SimpleGrantedAuthority("ROLE_VALIDATOR_FIN"));
        }
 
        UserDetails userDetails = (UserDetails) new User(appUser.getUserName(), appUser.getEncrytedPassword(), grantList);
 
        return userDetails;
    }
 
}
