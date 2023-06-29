package klassen;

import java.time.LocalDate;

public class Ausleihe {
	
	private int ausleihe_id;
	private LocalDate von;
	private LocalDate bis;
	private Buch buch;
	private Besucher besucher;
	private String bemerkungen;
	private boolean erledigt;	
	
	public Ausleihe() {}
	public Ausleihe(int ausleihe_id, LocalDate von, LocalDate bis, Buch buch, Besucher besucher, String bemerkungen, 
			boolean erledigt) {
		super();
		this.ausleihe_id = ausleihe_id;
		this.von = von;
		this.bis = bis;
		this.buch = buch;
		this.besucher = besucher;
		this.bemerkungen = bemerkungen;
		this.erledigt = erledigt;		
	}
	
	public int getAusleihe_id() {
		return ausleihe_id;
	}
	public void setAusleihe_id(int ausleihe_id) {
		this.ausleihe_id = ausleihe_id;
	}
	public LocalDate getVon() {
		return von;
	}
	public void setVon(LocalDate von) {
		this.von = von;
	}
	public LocalDate getBis() {
		return bis;
	}
	public void setBis(LocalDate bis) {
		this.bis = bis;
	}
	public Buch getBuch() {
		return buch;
	}
	public void setBuch(Buch buch) {
		this.buch = buch;
	}
	public Besucher getBesucher() {
		return besucher;
	}
	public void setBesucher(Besucher besucher) {
		this.besucher = besucher;
	}
	public String getBemerkungen() {
		return bemerkungen;
	}
	public void setBemerkungen(String bemerkungen) {
		this.bemerkungen = bemerkungen;
	}
	public boolean isErledigt() {
		return erledigt;
	}
	public void setErledigt(boolean erledigt) {
		this.erledigt = erledigt;
	}
	@Override
	public String toString() {
		return "Ausleihe [ausleihe_id=" + ausleihe_id + ", von=" + von + ", bis=" + bis + ", buch=" + buch
				+ ", besucher=" + besucher + ", bemerkungen=" + bemerkungen + ", erledigt=" + erledigt + "]";
	}
	
}
