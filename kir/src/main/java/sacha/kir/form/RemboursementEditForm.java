package sacha.kir.form;


public class RemboursementEditForm 
{
	private String motif;
	private Long justificatif_id;
	private String filename;
	private byte[] pdf;
	
	public RemboursementEditForm() {}

	public RemboursementEditForm(long justificatif_id, byte[] pdf, String filename, String motif) 
	{
		this();
		this.justificatif_id = justificatif_id;
		this.pdf = pdf;
		this.filename = filename;
		this.motif = motif;
	}
	
	// Getters
	public String getMotif() {
		return motif;
	}
	
	public long getJustificatif_id() {
		return justificatif_id;
	}

	public String getFilename() {
		return filename;
	}
	
	public byte[] getPdf() {
		return pdf;
	}
	
	// Setters
	public void setMotif(String motif) {
		this.motif = motif;
	}
	
	public void setJustificatif_id(long justificatif_id) {
		this.justificatif_id = justificatif_id;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}

	@Override
    public String toString() {
        return justificatif_id + " " + pdf.length;
    }
}
