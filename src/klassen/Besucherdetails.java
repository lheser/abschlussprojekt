package klassen;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.DateStringConverter;

public class Besucherdetails extends Dialog<ButtonType>{

	private final double fieldsWidth = 300;
	private ObservableList<AusleiheFX> obsListAusleihen = FXCollections.observableArrayList();
	private Pattern detectLetter = Pattern.compile("\\D");


	public Besucherdetails(Besucher besucher) {
		this.setTitle("Details des Besuchers");		

		//Spalte links
		Label besucher_id = new Label("Besuchernummer");
		Label vorname = new Label("Vorname");
		Label nachname = new Label("Nachname");
		Label geburtsdatum = new Label("Geburtsdatum");
		Label adresse = new Label("Adresse");		
		Label telefonnummer = new Label("Telefonnummer");
		Label email = new Label("E-mail");
		Label entlehnte = new Label("Entlehnte Bücher");

		Label id_in = new Label(Long.toString(besucher.getBesucherId()));	
		id_in.setDisable(false);
		TextField vorname_in = new TextField(besucher.getVorname());
		vorname_in.setPrefWidth(fieldsWidth);
		TextField nachname_in = new TextField(besucher.getNachname());
		nachname_in.setPrefWidth(fieldsWidth);
		TextField adresse_in = new TextField(besucher.getAdresse());
		adresse_in.setPrefWidth(fieldsWidth);

		/*
		 * Date Picker Object für das Genurtsdatum + Disable für Datum in der Zukunft
		 */
		DatePicker datePicker = new DatePicker();
		datePicker.setDayCellFactory(dp -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				setDisable(empty || date.compareTo(today) > 0 );
			}
		});
		datePicker.setEditable(false);
		datePicker.setOpacity(1);		
		if(besucher.getBesucherId() != 0)
			datePicker.setValue(besucher.getGeburtsdatum());
		/*
		 * Ersetzt dürch die setDayCellFactory
		datePicker.setOnAction(e -> {
			if(LocalDate.now().compareTo(datePicker.getValue()) < 0){
				new Alert(AlertType.ERROR, "Geburtsdatum nicht gültig").showAndWait();
				datePicker.setValue(LocalDate.now());
				e.consume();
				return;				
			}
		});*/

		//TextFeld input für die Telefunnummer und TextFormatter um Buchstaben zum Filtern
		TextField telefon_in = new TextField(besucher.getTelefonnummer());
		telefon_in.setPrefWidth(fieldsWidth);
		telefon_in.setTextFormatter(new TextFormatter<String>(write -> detectLetter.matcher(write.getControlNewText()).find() ? null : write));

		TextField email_in = new TextField(besucher.getEmail());
		email_in.setPrefWidth(fieldsWidth);		
		Button zurueckgeben = new Button("Buch zurückgeben");
		zurueckgeben.setDisable(true);		

		//GridPane für alle Nodes
		GridPane gp = new GridPane();
		gp.setVgap(10);
		gp.setHgap(10);		
		gp.add(besucher_id, 0, 1);
		gp.add(vorname, 0, 2);
		gp.add(nachname, 0, 3);
		gp.add(geburtsdatum, 0, 4);
		gp.add(adresse, 0, 5);
		gp.add(telefonnummer, 0, 6);
		gp.add(email, 0, 7);
		gp.add(entlehnte, 0, 8);

		gp.add(id_in, 1, 1);
		gp.add(vorname_in, 1, 2);
		gp.add(nachname_in, 1, 3);
		gp.add(datePicker, 1, 4);
		gp.add(adresse_in, 1, 5);
		gp.add(telefon_in, 1, 6);	
		gp.add(email_in, 1, 7);
		gp.add(zurueckgeben, 1, 8);

		//TableColumns und TableView für die aktive Ausleihen des Besuchers
		TableColumn<AusleiheFX, String> colTitel = new TableColumn<>("Titel");
		colTitel.setCellValueFactory(new PropertyValueFactory<>("buch_tiltel"));
		TableColumn<AusleiheFX, String> colAutor = new TableColumn<>("Autor");	
		colAutor.setCellValueFactory(new PropertyValueFactory<>("buch_autor"));
		TableColumn<AusleiheFX, String> colIsbn = new TableColumn<>("ISBN");
		colIsbn.setCellValueFactory(new PropertyValueFactory<>("buch_isbn"));
		TableColumn<AusleiheFX, String> colEntlehntVon = new TableColumn<>("Entlehnt von");
		colEntlehntVon.setCellValueFactory(new PropertyValueFactory<>("entlehnt_von"));
		TableColumn<AusleiheFX, String> colEntlehntBis = new TableColumn<>("Entlehnt bis");
		colEntlehntBis.setCellValueFactory(new PropertyValueFactory<>("entlehnt_bis"));

		TableView<AusleiheFX> tvAusleihe = new TableView<>(obsListAusleihen);
		tvAusleihe.getColumns().addAll(colIsbn, colTitel, colAutor, colEntlehntVon, colEntlehntBis);
		tvAusleihe.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tvAusleihe.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AusleiheFX>() {
			
			//Schaltung von "Buch zurückgeben" Button
			@Override
			public void changed(ObservableValue<? extends AusleiheFX> arg0, AusleiheFX arg1, AusleiheFX arg2) {
				if(arg2 != null) {
					
					zurueckgeben.setDisable(false);
				}
				else {
					zurueckgeben.setDisable(true);
				}
				return;
			}
		});

		tvAusleihe.setRowFactory(tv -> new TableRow<AusleiheFX>() {
			public void updateItem(AusleiheFX item, boolean empty) {
				super.updateItem(item, empty) ;
				if (item == null) {
					setStyle("");
				} else if (LocalDate.now().compareTo(item.getModellAusleihe().getBis()) > 0) {
					setStyle("-fx-background-color: lightpink;");
				} else {
					setStyle("");
				}
			}
		});

		//Buch zurüuckgeben setOnAction
		zurueckgeben.setOnAction(e ->{
			int index = tvAusleihe.getSelectionModel().getSelectedIndex();
			Buch buch = tvAusleihe.getSelectionModel().getSelectedItem().getModellAusleihe().getBuch();
			Ausleihe ausleihe = tvAusleihe.getSelectionModel().getSelectedItem().getModellAusleihe();
			//TextInputDialogue zur Eingabe von Bemerkungen zu der Ausleihe
			TextInputDialog tid = new TextInputDialog();
			tid.setTitle("Rückgabe");
			tid.setHeaderText("Bemerkungen?");
			Optional <String> r = tid.showAndWait();
			//Falls eine Bemerkung anwesend ist, wird die Ausleihe Object in der Datenbank updated
			if(r.isPresent() && r.get().length() > 0) {
				ausleihe.setBemerkungen(r.get());
				try {
					Datenbank.setAusleiheBemerkugen(ausleihe.getAusleihe_id(), r.get());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			//Ausleihe Erledigen
			try {				
				Datenbank.ausleiheErledigen(ausleihe.getAusleihe_id());		
				Datenbank.setBuchEntlehnt(buch, false);	
				//ObservableList updaten
				obsListAusleihen.remove(index);
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
		});

		VBox vb = new VBox(10, gp, tvAusleihe);		
		vb.setPrefSize(510, 620);

		//Set Content und Fenster Buttons
		this.getDialogPane().setContent(vb);		
		ButtonType speichern = new ButtonType("Speichern", ButtonData.OK_DONE);
		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);		
		this.getDialogPane().getButtonTypes().addAll(speichern, abbrechen);	
		this.setResizable(true);

		if(besucher.getBesucherId() != 0) {
			getAusleihen(besucher.getBesucherId());
		}

		//Kontrol von Eingegebene Daten
		Button s = (Button) this.getDialogPane().lookupButton(speichern);
		s.addEventFilter(ActionEvent.ACTION, e ->{			
			if(vorname_in.getText() == null || vorname_in.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Vorname eingeben").showAndWait();
				e.consume();
				return;
			}
			if(nachname_in.getText() == null || nachname_in.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Nachname eingeben").showAndWait();
				e.consume();
				return;
			}
			if(datePicker.getValue() == null) {
				new Alert(AlertType.ERROR, "Geburtsdatum auswählen").showAndWait();
				e.consume();
				return;
			}
			if(adresse_in.getText() == null || adresse_in.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Adresse eingeben").showAndWait();
				e.consume();
				return;
			}
			if(telefon_in.getText() == null || telefon_in.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Telefonnummer eingeben").showAndWait();
				e.consume();
				return;
			}
			if(email_in.getText() == null || email_in.getText().length() == 0) {
				new Alert(AlertType.ERROR, "e-mail eingeben").showAndWait();
				e.consume();
				return;
			}

		});

		//Daten übertragung vom Feld zum Object
		this.setResultConverter(new Callback<ButtonType, ButtonType>() {
			@Override
			public ButtonType call(ButtonType arg0) {
				if(arg0 == speichern) {					
					besucher.setVorname(vorname_in.getText());
					besucher.setNachname(nachname_in.getText());					
					besucher.setGeburtsdatum(datePicker.getValue());
					besucher.setAdresse(adresse_in.getText());
					besucher.setTelefonnummer(telefon_in.getText());
					besucher.setEmail(email_in.getText());
					besucher.setGeloescht(false);
					try {
						if(besucher.getBesucherId() == 0) {
							besucher.setBesucherId(System.currentTimeMillis());
							Datenbank.insertBesucher(besucher);							
						}
						else {
							Datenbank.updateBesucher(besucher);
						}
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
	 * Lies alle Ausleihen eines Besuchers wo erledigt = false
	 * @param besucherID
	 * @return ArrayList mit den Aktiven Ausleihen dieser Besucher
	 */
	private ArrayList<Ausleihe> getAusleihen(long besucherID) {
		try {
			ArrayList<Ausleihe> alAusleihe = Datenbank.readBesucherAusleihen(besucherID);
			obsListAusleihen.clear();
			for(Ausleihe ausleihe : alAusleihe)
				obsListAusleihen.add(new AusleiheFX(ausleihe));
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return null;
	}
}
