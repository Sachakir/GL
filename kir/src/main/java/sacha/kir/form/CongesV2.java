package sacha.kir.form;

public class CongesV2 
{
	 private Long congesid;
	 private String validationrh;
	 private String validationchefservice;
	 private long uid;
	 private String datefin;
	 private String datedebut;
	 private String prenomNom;
	 private String type;
	 
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getPrenomNom() {
		return prenomNom;
		}
		public void setPrenomNom(String prenomN) {
			prenomNom = prenomN;
		}
		public Long getCongesid() {
			return congesid;
		}
		public void setCongesid(Long congesid) {
			this.congesid = congesid;
		}
		public String getValidationrh() {
			return validationrh;
		}
		public void setValidationrh(String validationrh) {
			this.validationrh = validationrh;
		}
		public String getValidationchefservice() {
			return validationchefservice;
		}
		public void setValidationchefservice(String validationchefservice) {
			this.validationchefservice = validationchefservice;
		}
		public long getUid() {
			return uid;
		}
		public void setUid(long uid) {
			this.uid = uid;
		}
		public String getDatefin() {
			return datefin;
		}
		public void setDatefin(String datefin) {
			this.datefin = datefin;
		}
		public String getDatedebut() {
			return datedebut;
		}
		public void setDatedebut(String datedebut) {
			this.datedebut = datedebut;
		}
}
