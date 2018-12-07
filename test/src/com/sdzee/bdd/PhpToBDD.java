package com.sdzee.bdd;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class PhpToBDD {
	
	private String reponse;
	private String url = "https://sachakir1357.000webhostapp.com/execReq.php";
	private String requete;//Example de requete : "req=INSERT%20INTO%20#utilisateurs#%20(#nomCompte#,#mdp#,#id#)%20VALUES%20(\"TestPhp\",\"42\",\"4\")";
	//String urlParameters = "req=INSERT%20INTO%20#utilisateurs#%20(#nomCompte#,#mdp#,#id#)%20VALUES%20(\"TestPhp\",\"42\",\"4\")";
	//String urlParameters = "req=SELECT%20#id#%20FROM%20#utilisateurs#%20WHERE%20nomCompte=\"TestPhp\"";
	
	public void setRequete(String requete) 
	{
		this.requete = requete;
	}
	
	public String sendPost() throws IOException
	{
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(requete);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) 
		{
			response.append(inputLine);
		}
		in.close();
		
		reponse = response.toString();
		System.out.println(reponse);
		return reponse;
	}
	
	public static void main(String[] args) throws IOException
	{				
		PhpToBDD env = new PhpToBDD();
		String mdp = "42";
		String nom = "TestPhp";
		String chaine = "req=SELECT%20*%20FROM%20#utilisateurs#%20WHERE%20nomCompte=" + nom + "%20AND%20mdp=" + mdp;
		System.out.println(chaine);
		env.setRequete("req=SELECT%20*%20FROM%20#utilisateurs#%20WHERE%20nomCompte=" + "\"" +  nom + "\"" + "%20AND%20mdp=" + "\"" + mdp + "\"");

		System.out.println(env.sendPost());
		
		System.out.println("Fin");

	}
}
