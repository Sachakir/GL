package sacha.kir.form;

public class CongesV2 
{
	 private Long congesid;
	 private String validationrh;
	 private String validationchefservice;
	 private long uid;
	 private String datefin;
	 private String datedebut;
	 private String PrenomNom;
	 
		public String getPrenomNom() {
		return PrenomNom;
		}
		public void setPrenomNom(String prenomNom) {
			PrenomNom = prenomNom;
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
