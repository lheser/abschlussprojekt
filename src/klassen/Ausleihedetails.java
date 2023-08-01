package klassen;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class Ausleihedetails extends Dialog<ButtonType> {

	private Besucher ausleiheBesucher = new Besucher();	

	private TextField id_in = new TextField();		
	private Label besucher_vn = new Label();
	private Label besucher_nn = new Label();
	private Label besucher_gd = new Label();
	private Label besucher_a = new Label();		
	private Label besucher_t = new Label();
	private Label besucher_e = new Label();

	public Ausleihedetails(Buch buch) {
		this.setTitle("Ausleihen");		

		//Linke Spalte vom Fenster
		Label buchInfo = new Label("BUCH DETAILS");
		Label isbn = new Label("Isbn");
		Label titel = new Label("Titel");
		Label autor = new Label("Autor");		
		Label von = new Label("Entlehnt von");
		Label bis = new Label("Entlehnt bis");

		Label besucherInfo = new Label("BESUCHER DETAILS");
		Label besucher_id = new Label("Besuchernummer");
		Label vorname = new Label("Vorname");
		Label nachname = new Label("Nachname");
		Label geburtsdatum = new Label("Geburtsdatum");
		Label adresse = new Label("Adresse");		
		Label telefonnummer = new Label("Telefonnummer");
		Label email = new Label("E-mail");

		//Rechte Splate vom Fenster
		Label buch_isbn = new Label(buch.getIsbn());
		Label buch_titel = new Label(buch.getTitel());
		Label buch_autor = new Label(buch.getAutor());			
		Label ent_von = new Label(LocalDate.now().toString());
		DatePicker ent_bis = new DatePicker();
		ent_bis.getEditor().setDisable(true);
		ent_bis.getEditor().setOpacity(1);
		ent_bis.setOnAction(e ->{
			if(LocalDate.now().compareTo(ent_bis.getValue()) > 0){
				new Alert(AlertType.ERROR, "Rückgabedatum muss nach dem Ausleihedatum liegen").showAndWait();
				ent_bis.setValue(LocalDate.now());
				e.consume();
				return;				
			}
		});

		//Suche von Besucher nach Besucher ID
		Button id_suche = new Button("Suchen");				
		id_suche.setOnAction(e ->{
			if(id_in.getText()!= null && id_in.getText().length() == 13) {
				long id = 0;
				try {
					id = Long.parseLong(id_in.getText());					
				}
				catch(IllegalArgumentException e1) {
					new Alert(AlertType.ERROR, "Nur Ziffern sind erlaubt").showAndWait();
					e.consume();
					return;
				}				
				getAusleiheBesucher(id);
				/**
				 * Falls der Besucher gefunden wird, werden die Daten in den Felder des Ausleihedetailsfenster
				 * angezeigt
				 */				
				if(ausleiheBesucher != null) {
					fillInfoBesucher();					
					e.consume();
					return;
				} else {
					//Falls der Besucher nicht gefunden wird, ein neuen Besucher kann sofort erstellt werden
					Alert unbekannteBesucher = new Alert(AlertType.CONFIRMATION);
					unbekannteBesucher.setTitle("Besucher ID nicht bekannt");
					unbekannteBesucher.setContentText("Diese Besucher ID ist nicht bekannt."
							+ "\nMöchten Sie einen neuen Besucher eintragen?");				
					Optional<ButtonType> r = unbekannteBesucher.showAndWait();
					if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
						ausleiheBesucher = new Besucher();
						Optional<ButtonType> b = new Besucherdetails(ausleiheBesucher).showAndWait();
						if(b.isPresent() && b.get().getButtonData() == ButtonData.OK_DONE) {
							fillInfoBesucher();						
							e.consume();
							return;	
						}					
					}
					resetFelder();
					return;
				}
			}
			new Alert(AlertType.ERROR, "Gültige Besucher ID Nummer eingeben").showAndWait();
			resetFelder();
			e.consume();
			return;
		});

		//GridPane für alle Elemente
		GridPane gp_1 = new GridPane();
		gp_1.setVgap(10);
		gp_1.setHgap(10);
		gp_1.add(buchInfo, 0, 0);
		gp_1.add(isbn, 0, 1);
		gp_1.add(titel, 0, 2);
		gp_1.add(autor, 0, 3);
		gp_1.add(von, 0, 4);
		gp_1.add(bis, 0, 5);		
		gp_1.add(besucherInfo, 0, 6);
		gp_1.add(besucher_id, 0, 7);
		gp_1.add(vorname, 0, 8);
		gp_1.add(nachname, 0, 9);
		gp_1.add(geburtsdatum, 0, 10);
		gp_1.add(adresse, 0, 11);
		gp_1.add(telefonnummer, 0, 12);
		gp_1.add(email, 0, 13);

		gp_1.add(buch_isbn, 1, 1);
		gp_1.add(buch_titel, 1, 2);
		gp_1.add(buch_autor, 1, 3);
		gp_1.add(ent_von, 1, 4);
		gp_1.add(ent_bis, 1, 5);	
		gp_1.add(id_in, 1, 7);
		gp_1.add(id_suche, 2, 7);
		gp_1.add(besucher_vn, 1, 8);
		gp_1.add(besucher_nn, 1, 9);
		gp_1.add(besucher_gd, 1, 10);
		gp_1.add(besucher_a, 1, 11);
		gp_1.add(besucher_t, 1, 12);
		gp_1.add(besucher_e, 1, 13);		

		VBox vb = new VBox(gp_1);

		this.getDialogPane().setContent(vb);
		ButtonType ausleihen = new ButtonType("Ausleihen", ButtonData.OK_DONE);
		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);		
		this.getDialogPane().getButtonTypes().addAll(ausleihen, abbrechen);

		Button s = (Button) this.getDialogPane().lookupButton(ausleihen);
		s.addEventFilter(ActionEvent.ACTION, e->{
			if(ausleiheBesucher == null) {
				new Alert(AlertType.ERROR, "Besucher auswählen").showAndWait();
				e.consume();
				return;
			}			
			if(id_in.getText() == null || id_in.getText().length() != 13) {
				new Alert(AlertType.ERROR, "Gültige Besucher ID-Nummer eingeben").showAndWait();
				e.consume();
				return;
			}
			if(ent_bis.getValue() == null) {
				new Alert(AlertType.ERROR, "Rückgabedatum auswählen").showAndWait();
				e.consume();
				return;
			}			
		});		

		this.setResultConverter(new Callback<ButtonType, ButtonType>(){
			@Override
			public ButtonType call(ButtonType arg0) {
				if(arg0 == ausleihen) {					
					Ausleihe ausleihe = new Ausleihe();
					ausleihe.setVon(LocalDate.now());
					ausleihe.setBis(ent_bis.getValue());
					ausleihe.setBuch(buch);		
					ausleihe.setBesucher(ausleiheBesucher);
					ausleihe.setBemerkungen("");
					ausleihe.setErledigt(false);					
					try {
						Datenbank.insertAusleihe(ausleihe);
						Datenbank.setBuchEntlehnt(buch, true);
					}
					catch(SQLException e) {
						new Alert(AlertType.ERROR, e.toString()).showAndWait();
						return null;
					}					
				}
				return arg0;
			}			
		});		
	}

	/**
	 * Lies ein Besucher für ein BesucherID
	 * @param id
	 * @return Ein Besucher mit der entsprechenden ID
	 */
	private Besucher getAusleiheBesucher(long id) {
		try {
			ausleiheBesucher = Datenbank.getSingleBesucher(id);
			return ausleiheBesucher;
		} catch (NumberFormatException e1) {			
			e1.printStackTrace();
		} catch (SQLException e1) {			
			e1.printStackTrace();
		}
		return null;		
	}

	//Übertragung von Besucher Data an Labels
	private void fillInfoBesucher() {
		id_in.setText(Long.toString(ausleiheBesucher.getBesucherId()));
		besucher_vn.setText(ausleiheBesucher.getVorname());
		besucher_nn.setText(ausleiheBesucher.getNachname());
		besucher_gd.setText(ausleiheBesucher.getGeburtsdatum().toString());
		besucher_a.setText(ausleiheBesucher.getAdresse());
		besucher_t.setText(ausleiheBesucher.getTelefonnummer());
		besucher_e.setText(ausleiheBesucher.getEmail());		
	}
	
	//Reset von Labels
	private void resetFelder() {
		id_in.clear();		
		besucher_vn.setText("");
		besucher_nn.setText("");
		besucher_gd.setText("");
		besucher_a.setText("");
		besucher_t.setText("");
		besucher_e.setText("");
	}
}
