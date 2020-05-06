package model;

public class Myynti {
	
	private int tunniste;
	private String etunimi, sukunimi, puhelin, sposti;
	
	public Myynti() {
		super();
	}

	public Myynti(int tunniste, String etunimi, String sukunimi, String puhelin, String sposti) {
		super();
		this.tunniste = tunniste;
		this.etunimi = etunimi;
		this.sukunimi = sukunimi;
		this.puhelin = puhelin;
		this.sposti = sposti;
	}
	
	

	public int getTunniste() {
		return tunniste;
	}

	public void setTunniste(int tunniste) {
		this.tunniste = tunniste;
	}

	public String getEtunimi() {
		return etunimi;
	}

	public void setEtunimi(String etunimi) {
		this.etunimi = etunimi;
	}

	public String getSukunimi() {
		return sukunimi;
	}

	public void setSukunimi(String sukunimi) {
		this.sukunimi = sukunimi;
	}

	public String getPuhelin() {
		return puhelin;
	}

	public void setPuhelin(String puhelin) {
		this.puhelin = puhelin;
	}

	public String getSposti() {
		return sposti;
	}

	public void setSposti(String sposti) {
		this.sposti = sposti;
	}

	@Override
	public String toString() {
		return "Myynti [tunniste=" + tunniste + ", etunimi=" + etunimi + ", sukunimi=" + sukunimi + ", puhelin="
				+ puhelin + ", sposti=" + sposti + "]";
	}

	
 
	
}
