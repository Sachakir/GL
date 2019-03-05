package root;

public class DateUtilities {
	
    private String[] m = new String[12];

	public int moisToInt(String mois)
	{
		m[0] = "Janvier";
		m[1] = "Fevrier";
		m[2] = "Mars";
		m[3] = "Avril";
		m[4] = "Mai";
		m[5] = "Juin";
		m[6] = "Juillet";
		m[7] = "Aout";
		m[8] = "Septembre";
		m[9] = "Octobre";
		m[10] = "Novembre";
		m[11] = "Decembre";
		
		for (int i = 0;i < 12;i++)
		{
			if (m[i].contains(mois))
			{
				return i+1;
			}
		}
		return 13;
	}
}
