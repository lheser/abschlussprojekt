package klassen;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BuchFX {
	
	private Buch modellBuch;
	private SimpleStringProperty isbn;
	private SimpleStringProperty titel;
	private SimpleStringProperty autor;
	private SimpleIntegerProperty jahr;
	private SimpleStringProperty thema;
	private SimpleStringProperty verlag;
	private SimpleDoubleProperty preis;
	private SimpleStringProperty entlehnt;		
	
	public BuchFX(Buch modellBuch) {
		this.modellBuch = modellBuch;
		isbn = new SimpleStringProperty(modellBuch.getIsbn());
		titel = new SimpleStringProperty(modellBuch.getTitel());
		autor = new SimpleStringProperty(modellBuch.getAutor());
		jahr = new SimpleIntegerProperty(modellBuch.getJahr());
		thema = new SimpleStringProperty(modellBuch.getThema().getBezeichnung());
		verlag = new SimpleStringProperty(modellBuch.getVerlag());
		preis = new SimpleDoubleProperty(modellBuch.getPreis());
		entlehnt = new SimpleStringProperty(modellBuch.isEntlehnt()? "Entlehnt" : "Verfügbar");
	}

	public Buch getModellBuch() {
		return modellBuch;
	}

	public final SimpleStringProperty isbnProperty() {
		return this.isbn;
	}
	

	public final String getIsbn() {
		return this.isbnProperty().get();
	}
	

	public final void setIsbn(final String isbn) {
		this.isbnProperty().set(isbn);
	}
	

	public final SimpleStringProperty titelProperty() {
		return this.titel;
	}
	

	public final String getTitel() {
		return this.titelProperty().get();
	}
	

	public final void setTitel(final String titel) {
		this.titelProperty().set(titel);
	}
	

	public final SimpleStringProperty autorProperty() {
		return this.autor;
	}
	

	public final String getAutor() {
		return this.autorProperty().get();
	}
	

	public final void setAutor(final String autor) {
		this.autorProperty().set(autor);
	}
	

	public final SimpleIntegerProperty jahrProperty() {
		return this.jahr;
	}
	

	public final int getJahr() {
		return this.jahrProperty().get();
	}
	

	public final void setJahr(final int jahr) {
		this.jahrProperty().set(jahr);
	}
	

	public final SimpleStringProperty themaProperty() {
		return this.thema;
	}
	

	public final String getThema() {
		return this.themaProperty().get();
	}
	

	public final void setThema(final String thema) {
		this.themaProperty().set(thema);
	}
	

	public final SimpleStringProperty verlagProperty() {
		return this.verlag;
	}
	

	public final String getVerlag() {
		return this.verlagProperty().get();
	}
	

	public final void setVerlag(final String verlag) {
		this.verlagProperty().set(verlag);
	}
	

	public final SimpleDoubleProperty preisProperty() {
		return this.preis;
	}
	

	public final double getPreis() {
		return this.preisProperty().get();
	}
	

	public final void setPreis(final double preis) {
		this.preisProperty().set(preis);
	}
	

	public final SimpleStringProperty entlehntProperty() {
		return this.entlehnt;
	}
	

	public final String getEntlehnt() {
		return this.entlehntProperty().get();
	}
	

	public final void setEntlehnt(final String entlehnt) {
		this.entlehntProperty().set(entlehnt);
	}
		
	
}
