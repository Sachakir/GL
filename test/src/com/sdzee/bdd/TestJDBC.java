package com.sdzee.bdd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class TestJDBC {
	
/* La liste qui contiendra tous les r�sultats de nos essais */
private List<String> messages = new ArrayList<String>();
public List<String> executerTests( HttpServletRequest request ) {
    /* Chargement du driver JDBC pour MySQL */
    try {
        messages.add( "Chargement du driver..." );
        Class.forName( "com.mysql.jdbc.Driver" );
        messages.add( "Driver charg� !" );
    } catch ( ClassNotFoundException e ) {
        messages.add( "Erreur lors du chargement : le driver n'a pas �t� trouv� dans le classpath ! <br/>"
                + e.getMessage() );
    }

    /* Connexion � la base de donn�es */
    String url = "jdbc:mysql://sachakir1357.000webhostapp.com:3306/id8146891_intranetgl";
    String utilisateur = "java";
    String motDePasse = "Polytech";
    Connection connexion = null;
    Statement statement = null;
    ResultSet resultat = null;
    try {
        messages.add( "Connexion � la base de donn�es..." );
        connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
        messages.add( "Connexion r�ussie !" );

        /* Cr�ation de l'objet g�rant les requ�tes */
        statement = connexion.createStatement();
        messages.add( "Objet requ�te cr�� !" );

        /* Ex�cution d'une requ�te de lecture */
        
        int statut = statement.executeUpdate( "INSERT INTO `utilisateurs` (`nomCompte`, `mdp`, `id`) VALUES ('Saad', '5', '2');" ); 
        
        resultat = statement.executeQuery( "SELECT * FROM utilisateur;" );
        messages.add( "Requ�te \"SELECT * FROM utilisateur;\" effectu�e !" );
 
        /* R�cup�ration des donn�es du r�sultat de la requ�te de lecture */
        while ( resultat.next() ) {
            int idUtilisateur = resultat.getInt( "id" );
            String emailUtilisateur = resultat.getString( "email" );
            String motDePasseUtilisateur = resultat.getString( "mot_de_passe" );
            String nomUtilisateur = resultat.getString( "nom" );
            /* Formatage des donn�es pour affichage dans la JSP finale. */
            messages.add( "Donn�es retourn�es par la requ�te : id = " + idUtilisateur + ", email = " + emailUtilisateur
                    + ", motdepasse = "
                    + motDePasseUtilisateur + ", nom = " + nomUtilisateur + "." );
        }
    } catch ( SQLException e ) {
        messages.add( "Erreur lors de la connexion : <br/>"
                + e.getMessage() );
    } finally {
        messages.add( "Fermeture de l'objet ResultSet." );
        if ( resultat != null ) {
            try {
                resultat.close();
            } catch ( SQLException ignore ) {
            }
        }
        messages.add( "Fermeture de l'objet Statement." );
        if ( statement != null ) {
            try {
                statement.close();
            } catch ( SQLException ignore ) {
            }
        }
        messages.add( "Fermeture de l'objet Connection." );
        if ( connexion != null ) {
            try {
                connexion.close();
            } catch ( SQLException ignore ) {
            }
        }
    }

    return messages;
}


}