package sacha.kir.form;


public class RemboursementEditForm 
{
	private String motif;
	
	public RemboursementEditForm() {}

	public RemboursementEditForm(String motif) 
	{
		this();
		this.motif = motif;
	}
	
	// Getters
	public String getMotif() {
		return motif;
	}
	
	// Setters
	public void setMotif(String motif) {
		this.motif = motif;
	}
}
