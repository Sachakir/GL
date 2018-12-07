package com.sdzee.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class JustAMain {

	public static void main(String[] args) {
		System.out.println("OUI!");
		
		
		 try {
		        Class.forName( "com.mysql.cj.jdbc.Driver" );
		    } catch ( ClassNotFoundException e ) {
		       System.out.println(e.getMessage());
		    }
		 
		 String url = "jdbc:mysql://sachakir1357.000webhostapp.com/:3306/id8146891_intranetgl";//Marche pas, faut la version payante du host
		    String utilisateur = "id8146891_codemaster";
		    String motDePasse = "Polytech";
		    Connection connexion = null;
		    Statement statement = null;
		    ResultSet resultat = null;
		    try {
		    	System.out.println("Avant getConnection");
		        connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
		        System.out.println("Apres getConnection");
		        /* Création de l'objet gérant les requêtes */
		        statement = connexion.createStatement();

		        /* Exécution d'une requête de lecture */
		        
		        int statut = statement.executeUpdate( "INSERT INTO `utilisateurs` (`nomCompte`, `mdp`, `id`) VALUES ('Saad', '5', '2');" ); 
		        
		       /* resultat = statement.executeQuery( "SELECT * FROM utilisateur;" );
		 
		        while ( resultat.next() ) {
		            int idUtilisateur = resultat.getInt( "id" );
		            String emailUtilisateur = resultat.getString( "email" );
		            String motDePasseUtilisateur = resultat.getString( "mot_de_passe" );
		            String nomUtilisateur = resultat.getString( "nom" );
		        }*/
		    } catch ( SQLException e ) {
		                System.out.println(e.getMessage());
		    }
		    
		    
	}

}
