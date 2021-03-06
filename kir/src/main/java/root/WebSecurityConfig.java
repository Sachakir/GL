package root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import root.bdd.controllers.MainErrorController;
 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    MainErrorController mainAccessDeniedHandler;
 
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
     
     
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { 
 
        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());     
 
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// Permet de gerer les droits d'accès aux pages 
    	// Toujours donner les droits du plus au moins restrictif
 
        http.csrf().disable();
 
        // The pages does not require login
        http.authorizeRequests().antMatchers("/", "/login", "/logout","/login-Error").permitAll();
        
        // For ADMIN only.
        http.authorizeRequests().antMatchers("/administration/**").hasRole("ADMIN");
        
        http.authorizeRequests().antMatchers("/Calendrier","/missions/**","/validationNDF","/ValidationRemb/**","/RefusRemb/**","/ValidationConges/**","/RefusConges/**").hasRole("CHEF");
        http.authorizeRequests().antMatchers("/validationNDF","/ValidationRemb/**","/RefusRemb/**").hasRole("VALIDATOR_FIN");
        http.authorizeRequests().antMatchers("/Calendrier","/ValidationConges/**","/RefusConges/**").hasRole("VALIDATOR_RH");

        // Redirect to login page if the user is not connected
        http.authorizeRequests().antMatchers("/assets/**").permitAll();
        http.authorizeRequests().antMatchers("/**").authenticated();
 
        // When the user has logged in as XX.
        // But access a page that requires role YY,
        // AccessDeniedException will be thrown.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
 
        // Config for Login Form
        http.authorizeRequests().and().formLogin()//
                // Submit URL of login page.
                .loginProcessingUrl("/j_spring_security_check") // Submit URL
                .loginPage("/login")//
                .defaultSuccessUrl("/Accueil")//
                .failureUrl("/login-Error")//
                .usernameParameter("username")//
                .passwordParameter("password")
                // Config for Logout Page
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }
}
