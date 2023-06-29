package klassen;

public class Buch {

	private String isbn;
	private String titel;	
	private String autor;
	private int jahr;
	private Thema thema;
	private String verlag;
	private double preis;
	private String beschreibung;
	private String titelblatt;
	private boolean entlehnt;
	private boolean geloescht;

	public Buch() {}
	
	public Buch(String isbn, String titel, String autor) {
		this.isbn = isbn;
		this.titel = titel;
		this.autor = autor;
	}

	public Buch(String isbn, String titel, String autor, int jahr, Thema thema, String verlag, double preis,
			String beschreibung, String titelblatt, boolean entlehnt, boolean geloescht) {
		super();
		this.isbn = isbn;
		this.titel = titel;
		this.autor = autor;
		this.jahr = jahr;
		this.thema = thema;
		this.verlag = verlag;
		this.preis = preis;
		this.beschreibung = beschreibung;
		this.titelblatt = titelblatt;
		this.entlehnt = entlehnt;
		this.geloescht = geloescht;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public int getJahr() {
		return jahr;
	}

	public void setJahr(int jahr) {
		this.jahr = jahr;
	}

	public Thema getThema() {
		return thema;
	}

	public void setThema(Thema thema) {
		this.thema = thema;
	}

	public String getVerlag() {
		return verlag;
	}

	public void setVerlag(String verlag) {
		this.verlag = verlag;
	}

	public double getPreis() {
		return preis;
	}

	public void setPreis(double preis) {
		this.preis = preis;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getTitelblatt() {
		return titelblatt;
	}

	public void setTitelblatt(String titelblatt) {
		this.titelblatt = titelblatt;
	}

	public boolean isEntlehnt() {
		return entlehnt;
	}

	public void setEntlehnt(boolean entlehnt) {
		this.entlehnt = entlehnt;
	}

	public boolean isGeloescht() {
		return geloescht;
	}

	public void setGeleoscht(boolean entfernt) {
		this.geloescht = entfernt;
	}

	@Override
	public String toString() {
		return "Buch [isbn=" + isbn + ", titel=" + titel + ", autor=" + autor + " path=" + titelblatt + "]";
	}
	
	
}
