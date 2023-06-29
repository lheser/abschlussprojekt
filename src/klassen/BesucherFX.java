package klassen;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class BesucherFX {

	private Besucher modellBesucher;
	private SimpleLongProperty besucherId;
	private SimpleStringProperty vorname;
	private SimpleStringProperty nachname;
	private SimpleObjectProperty<LocalDate> gebDatum;
	private SimpleStringProperty besucherTelefon;
	private SimpleStringProperty besucherAdresse;
	private ArrayList<BuchFX> entlehnteBuecher;
	

	public BesucherFX(Besucher modellBesucher) {
		this.modellBesucher = modellBesucher;
		besucherId = new SimpleLongProperty(modellBesucher.getBesucherId());
		vorname = new SimpleStringProperty(modellBesucher.getVorname());
		nachname = new SimpleStringProperty(modellBesucher.getNachname());
		gebDatum = new SimpleObjectProperty<>(modellBesucher.getGeburtsdatum());
		besucherTelefon = new SimpleStringProperty(modellBesucher.getTelefonnummer());
		besucherAdresse = new SimpleStringProperty(modellBesucher.getAdresse());		
		entlehnteBuecher = new ArrayList<>();
	}

	public Besucher getModellBesucher() {
		return modellBesucher;
	}

	public ArrayList<BuchFX> getEntlehnteBuecher() {
		return entlehnteBuecher;
	}

	public final SimpleLongProperty besucherIdProperty() {
		return this.besucherId;
	}
	

	public final long getBesucherId() {
		return this.besucherIdProperty().get();
	}
	

	public final void setBesucherId(final long besucherId) {
		this.besucherIdProperty().set(besucherId);
	}
	

	public final SimpleStringProperty vornameProperty() {
		return this.vorname;
	}
	

	public final String getVorname() {
		return this.vornameProperty().get();
	}
	

	public final void setVorname(final String vorname) {
		this.vornameProperty().set(vorname);
	}
	

	public final SimpleStringProperty nachnameProperty() {
		return this.nachname;
	}
	

	public final String getNachname() {
		return this.nachnameProperty().get();
	}
	

	public final void setNachname(final String nachname) {
		this.nachnameProperty().set(nachname);
	}

	public final SimpleObjectProperty<LocalDate> gebDatumProperty() {
		return this.gebDatum;
	}
	

	public final LocalDate getGebDatum() {
		return this.gebDatumProperty().get();
	}
	

	public final void setGebDatum(final LocalDate gebDatum) {
		this.gebDatumProperty().set(gebDatum);
	}
	

	public final SimpleStringProperty besucherTelefonProperty() {
		return this.besucherTelefon;
	}
	

	public final String getBesucherTelefon() {
		return this.besucherTelefonProperty().get();
	}
	

	public final void setBesucherTelefon(final String besucherTelefon) {
		this.besucherTelefonProperty().set(besucherTelefon);
	}
	

	public final SimpleStringProperty besucherAdresseProperty() {
		return this.besucherAdresse;
	}
	

	public final String getBesucherAdresse() {
		return this.besucherAdresseProperty().get();
	}
	

	public final void setBesucherAdresse(final String besucherAdresse) {
		this.besucherAdresseProperty().set(besucherAdresse);
	}
	
	
}
