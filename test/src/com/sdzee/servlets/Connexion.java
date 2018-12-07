package com.sdzee.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sdzee.bdd.PhpToBDD;

public class Connexion extends HttpServlet {
    public static final String VUE = "/WEB-INF/Connexion.jsp";
	
    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
        /* Affichage de la page d'inscription */
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
    
    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
        /* Traitement des données du formulaire */
    	String mdp = request.getParameter("motdepasse");
    	String nom = request.getParameter("nom");
    	
    	PhpToBDD liasonBD = new PhpToBDD();
    	liasonBD.setRequete("req=SELECT%20#id#%20FROM%20#utilisateurs#%20WHERE%20nomCompte=" + "\"" + nom + "\"" + "%20AND%20mdp=" + "\"" + mdp + "\"");
    	String resultatRequete = liasonBD.sendPost();
    	request.setAttribute( "resultat", resultatRequete );
    	this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }
}
