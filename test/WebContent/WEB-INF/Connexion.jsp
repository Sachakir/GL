<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Inscription</title>
        <link type="text/css" rel="stylesheet" href="form.css" />
    </head>
    <body>
        <form method="post" action="connexion">
            <fieldset>
                <legend>Connexion</legend>
                <p>Vous pouvez vous connecter via ce formulaire.</p>

                <label for="nom">Nom du compte</label>
                <input type="text" id="nom" name="nom" value="" size="20" maxlength="20" />
                <br />
                
                <label for="motdepasse">Mot de passe <span class="requis"></span></label>
                <input type="password" id="motdepasse" name="motdepasse" value="" size="20" maxlength="20" />
                <br />

                <input type="submit" value="Connexion" class="sansLabel" />
                <br />
            </fieldset>
        </form>
        
        <p>
            <% 
            out.println("OK");
            String attribut = (String) request.getAttribute("resultat");
        	out.println( attribut );
            %>
        </p>
    </body>
</html>