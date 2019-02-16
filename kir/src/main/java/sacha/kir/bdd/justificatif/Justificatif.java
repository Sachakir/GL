package sacha.kir.bdd.justificatif;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "justificatif")
public class Justificatif 
{
	@Id
	@Column(name="JustificatifID")
	private Long justificatif_id;
	
	@Lob
    @Column(name = "PDF", columnDefinition="BLOB")
    private byte[] pdf;
	
	@Column(name="Del")
	private boolean del;
	
	public Justificatif() {}

	public Justificatif(Long justificatif_id, byte[] pdf) 
	{
		this.justificatif_id = justificatif_id;
		this.pdf = pdf;
		this.del = false;
	}

	public Long getJustificatif_id() {
		return justificatif_id;
	}

	public void setJustificatif_id(Long justificatif_id) {
		this.justificatif_id = justificatif_id;
	}

	public byte[] getPdf() {
		return pdf;
	}

	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}
	
	public boolean getDel() {
		return del;
	}
	
	public void setDel(boolean del) {
		this.del = del;
	}
	
	@Override
    public String toString() {
        return justificatif_id + " " + pdf.length;
    }
}
