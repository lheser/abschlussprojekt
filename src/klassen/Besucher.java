package klassen;

import java.time.LocalDate;

public class Besucher {
	
	private long besucherId;
	private String vorname;
	private String nachname;
	private String adresse;	
	private LocalDate geburtsdatum;
	private String telefonnummer;
	private String email;
	private boolean geloescht;
	
	
	public Besucher() {}
	
	public Besucher(long besucherId, String vorname, String nachname) {
		this.besucherId = besucherId;
		this.vorname = vorname;
		this.nachname = nachname;		
	}
	
	public Besucher(long besucherId, String vorname, String nachname, LocalDate geburtsdatum, String adresse, 
			String telefonnummer, String email, boolean geloescht) {
		this.besucherId = besucherId;
		this.vorname = vorname;
		this.nachname = nachname;
		this.adresse = adresse;
		this.geburtsdatum = geburtsdatum;
		this.telefonnummer = telefonnummer;
		this.email = email;
		this.geloescht = geloescht;
		
	}

	public long getBesucherId() {
		return besucherId;
	}

	public void setBesucherId(long besucherId) {
		this.besucherId = besucherId;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public LocalDate getGeburtsdatum() {
		return geburtsdatum;
	}

	public void setGeburtsdatum(LocalDate geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	public String getTelefonnummer() {
		return telefonnummer;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	
	public boolean isGeloescht() {
		return geloescht;
	}
	
	public void setGeloescht(boolean entfernt) {
		this.geloescht = entfernt;
	}	

	@Override
	public String toString() {
		return "Besucher [besucherId=" + besucherId + ", vorname=" + vorname + ", nachname=" + nachname + ", adresse="
				+ adresse + ", geburtsdatum=" + geburtsdatum + ", telefonnummer=" + telefonnummer + ", email=" + email
				+ "]";
	}
}
