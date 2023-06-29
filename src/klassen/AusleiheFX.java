package klassen;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class AusleiheFX {	
	private SimpleIntegerProperty ausleihe_ID;
	private SimpleStringProperty buch_isbn;	
	private SimpleStringProperty buch_tiltel;
	private SimpleStringProperty buch_autor;
	private SimpleObjectProperty<LocalDate> entlehnt_von;
	private SimpleObjectProperty<LocalDate> entlehnt_bis;
	private SimpleStringProperty besucher_name;
	private SimpleLongProperty aus_besucherId;
	private SimpleStringProperty bemerkungen;
	private Ausleihe modellAusleihe;	
	
	public AusleiheFX(Ausleihe modellAusleihe) {
		this.modellAusleihe = modellAusleihe;
		this.ausleihe_ID = new SimpleIntegerProperty(modellAusleihe.getAusleihe_id());
		this.buch_isbn = new SimpleStringProperty(modellAusleihe.getBuch().getIsbn());
		this.buch_tiltel = new SimpleStringProperty(modellAusleihe.getBuch().getTitel());
		this.buch_autor = new SimpleStringProperty(modellAusleihe.getBuch().getAutor());
		this.entlehnt_von = new SimpleObjectProperty<>(modellAusleihe.getVon());
		this.entlehnt_bis = new SimpleObjectProperty<>(modellAusleihe.getBis());		
		this.besucher_name = new SimpleStringProperty(modellAusleihe.getBesucher().getVorname() + " " +
				modellAusleihe.getBesucher().getNachname());
		this.aus_besucherId = new SimpleLongProperty(modellAusleihe.getBesucher().getBesucherId());
		this.bemerkungen = new SimpleStringProperty(modellAusleihe.getBemerkungen());
	}

	public Ausleihe getModellAusleihe() {
		return modellAusleihe;
	}

	public void setModellAusleihe(Ausleihe modellAusleihe) {
		this.modellAusleihe = modellAusleihe;
	}

	public final SimpleIntegerProperty ausleihe_IDProperty() {
		return this.ausleihe_ID;
	}
	

	public final int getAusleihe_ID() {
		return this.ausleihe_IDProperty().get();
	}
	

	public final void setAusleihe_ID(final int ausleihe_ID) {
		this.ausleihe_IDProperty().set(ausleihe_ID);
	}
	

	public final SimpleStringProperty buch_isbnProperty() {
		return this.buch_isbn;
	}
	

	public final String getBuch_isbn() {
		return this.buch_isbnProperty().get();
	}
	

	public final void setBuch_isbn(final String buch_isbn) {
		this.buch_isbnProperty().set(buch_isbn);
	}
	

	public final SimpleStringProperty buch_tiltelProperty() {
		return this.buch_tiltel;
	}
	

	public final String getBuch_tiltel() {
		return this.buch_tiltelProperty().get();
	}
	

	public final void setBuch_tiltel(final String buch_tiltel) {
		this.buch_tiltelProperty().set(buch_tiltel);
	}
	

	public final SimpleStringProperty buch_autorProperty() {
		return this.buch_autor;
	}
	

	public final String getBuch_autor() {
		return this.buch_autorProperty().get();
	}
	

	public final void setBuch_autor(final String buch_autor) {
		this.buch_autorProperty().set(buch_autor);
	}
	

	public final SimpleObjectProperty<LocalDate> entlehnt_bisProperty() {
		return this.entlehnt_bis;
	}
	

	public final LocalDate getEntlehnt_bis() {
		return this.entlehnt_bisProperty().get();
	}
	

	public final void setEntlehnt_bis(final LocalDate entlehnt_bis) {
		this.entlehnt_bisProperty().set(entlehnt_bis);
	}
	

	public final SimpleStringProperty besucher_nameProperty() {
		return this.besucher_name;
	}
	

	public final String getBesucher_name() {
		return this.besucher_nameProperty().get();
	}
	

	public final void setBesucher_name(final String besucher_name) {
		this.besucher_nameProperty().set(besucher_name);
	}
	

	public final SimpleLongProperty aus_besucherIdProperty() {
		return this.aus_besucherId;
	}
	

	public final long getAus_besucherId() {
		return this.aus_besucherIdProperty().get();
	}
	

	public final void setAus_besucherId(final long aus_besucherId) {
		this.aus_besucherIdProperty().set(aus_besucherId);
	}

	public final SimpleStringProperty bemerkungenProperty() {
		return this.bemerkungen;
	}
	

	public final String getBemerkungen() {
		return this.bemerkungenProperty().get();
	}
	

	public final void setBemerkungen(final String bemerkungen) {
		this.bemerkungenProperty().set(bemerkungen);
	}

	public final SimpleObjectProperty<LocalDate> entlehnt_vonProperty() {
		return this.entlehnt_von;
	}
	

	public final LocalDate getEntlehnt_von() {
		return this.entlehnt_vonProperty().get();
	}
	

	public final void setEntlehnt_von(final LocalDate entlehnt_von) {
		this.entlehnt_vonProperty().set(entlehnt_von);
	}
			
}
