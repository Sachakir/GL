package sacha.kir.form;


public class RemboursementEditForm 
{
	private String motif;
	private boolean updateFile;
	
	public RemboursementEditForm() {
		this.updateFile = false;
	}

	public RemboursementEditForm(String motif, boolean updateFile) 
	{
		this();
		this.motif = motif;
		this.updateFile = updateFile;
	}
	
	// Getters
	public String getMotif() {
		return motif;
	}
	
	public boolean getUpdateFile() {
		return updateFile;
	}
	
	// Setters
	public void setMotif(String motif) {
		this.motif = motif;
	}
	
	public void setUpdateFile(boolean updateFile) {
		this.updateFile = updateFile;
	}
}
