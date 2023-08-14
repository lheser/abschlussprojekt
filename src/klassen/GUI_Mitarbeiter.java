package klassen;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import klassen.emailService.Penalty;
import klassen.emailService.Reminder;


public class GUI_Mitarbeiter extends Application {

	private enum Buchsuche{
		ISBN, AUTOR, TITEL, THEMA;		
	}
	private enum Besuchersuche{
		ID, NAME;
	}
	private enum Statistiksuche{
		ALLE, ENTLEHNTE, UEBERFAELLIG;
	}

	//Observable Lists für die Bücher und die Besucher
	private ObservableList<BuchFX> obsListBuecher = FXCollections.observableArrayList();
	private ObservableList<BesucherFX> obsListBesucher = FXCollections.observableArrayList();
	private ObservableList<Thema> obsListThemen = FXCollections.observableArrayList();	
	private ObservableList<AusleiheFX> obsListAusleihen  = FXCollections.observableArrayList();
	private String genericPath = ".\\resources\\titelblatt_unknown.jpg";
	private static long ausleihe_besucher_id = 0;

	private ComboBox<Thema> cbThemen = new ComboBox<>(obsListThemen);	
	private TextField buchSuche_input = new TextField("Suchen");
	private TextField besucherSuche_input = new TextField("Suchen");		

	@SuppressWarnings({"exports",  })
	@Override
	public void start(Stage primaryStage) {
		//Einrichtung von Tabellen falls nich vorhanden
		try {	
			Datenbank.createThemenTable();
			Datenbank.createBuecherTable();
			Datenbank.createBesucherTable();
			Datenbank.createAusleihenTable();				
		} catch(SQLException e) {
			e.printStackTrace();
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
			return;
		}

		//Tab, Buttons und Tableview für die Verwaltung und Darstellung von Elemente im Katalog
		Tab katalog = new Tab("Bücherkatalog");

		//Suche Optionen Bücher
		Label lbl_suche = new Label("Suche nach: ");	
		RadioButton rb_isbn = new RadioButton("ISBN");
		RadioButton rb_titel = new RadioButton("TITEL");
		RadioButton rb_autor = new RadioButton("AUTOR");		
		ToggleGroup tg1 = new ToggleGroup();
		rb_isbn.setToggleGroup(tg1);
		rb_titel.setToggleGroup(tg1);
		rb_autor.setToggleGroup(tg1);		
		HBox hb_radioButtonsBuch = new HBox(18, lbl_suche, rb_isbn, rb_titel, rb_autor);
		hb_radioButtonsBuch.setPadding(new Insets(5));	

		//Logic von RadioButtons (isbn, titel, autor) versus ComboBox
		cbThemen.setOnMouseClicked( e-> {
			rb_isbn.setSelected(false);
			rb_titel.setSelected(false);
			rb_autor.setSelected(false);
			if(buchSuche_input.getText().length() > 0)
				buchSuche_input.clear();
		});	

		//Size von TextFeld input und "Suchen" innerhalb den TextFeld
		buchSuche_input.setPrefWidth(250);
		buchSuche_input.setOnMouseClicked(e ->{
			if(buchSuche_input.getText().equals("Suchen"))
				buchSuche_input.clear();
		});

		//Suche Enter Taste EventHandler und Auswahl bearbeitung
		buchSuche_input.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if(arg0.getCode().equals(KeyCode.ENTER)) {
					if(buchSuche_input.getText().length() == 0 && cbThemen.getSelectionModel().getSelectedItem().
							getThema_ID() == 0) {
						buecherSuchen();
						return;
					}
					if(cbThemen.getSelectionModel().getSelectedItem().getThema_ID() !=0) {
						buecherSuchen(Buchsuche.THEMA);
						cbThemen.getSelectionModel().select(0);
						return;
					}
					if(rb_autor.isSelected()) {
						buecherSuchen(Buchsuche.AUTOR);
						return;
					}
					if(rb_titel.isSelected()) {
						buecherSuchen(Buchsuche.TITEL);
						return;
					}						
					if (rb_isbn.isSelected()){
						buecherSuchen(Buchsuche.ISBN);	
						return;
					}
					new Alert(AlertType.ERROR, "ISBN, TITEL, AUTOR oder THEMA auswählen").showAndWait();
					return;
				}
			}
		});

		//Suchefunktion und Auswahl
		Button suchen = new Button("suchen");
		suchen.setOnAction(e ->{			
			if(buchSuche_input.getLength() == 0 && cbThemen.getSelectionModel().getSelectedItem().getThema_ID() == 0) {
				buecherSuchen();
				e.consume();
				return;
			}	
			else {				
				if(rb_autor.isSelected()) {
					buecherSuchen(Buchsuche.AUTOR);
					e.consume();
					return;
				}
				if(rb_titel.isSelected()) {
					buecherSuchen(Buchsuche.TITEL);
					e.consume();
					return;
				}
				if(rb_isbn.isSelected()) {
					buecherSuchen(Buchsuche.ISBN);
					e.consume();
					return;
				}
				if(cbThemen.getSelectionModel().getSelectedItem().getThema_ID() != 0) {
					buecherSuchen(Buchsuche.THEMA);
					buchSuche_input.setText("");
					cbThemen.getSelectionModel().select(0);
					e.consume();
					return;
				}
				new Alert(AlertType.ERROR, "ISBN, TITEL, AUTOR oder THEMA auswählen").showAndWait();
				e.consume();
				return;
			}
		});

		//Node behälter für Sucheelemente
		HBox maSucheBuecher = new HBox(13, hb_radioButtonsBuch, cbThemen, buchSuche_input, suchen);
		maSucheBuecher.setPadding(new Insets(15, 0, 3, 0));

		//Buttons für die Bücherverwaltung
		Button neuesBuch = new Button("Neues Buch");
		Button bearbeitenBuch = new Button("Buch bearbeiten");			
		Button entfernenBuch = new Button("Buch entfernen");		
		Button ausleihenBuch = new Button("Buch ausleihen");		
		Button ruckgabeBuch = new Button("Buch zurückgeben");
		bearbeitenBuch.setDisable(true);
		entfernenBuch.setDisable(true);
		ausleihenBuch.setDisable(true);
		ruckgabeBuch.setDisable(true);

		//Behälter dür die Buttons
		VBox buttonsBuecher = new VBox(10, neuesBuch, bearbeitenBuch, entfernenBuch, ausleihenBuch, ruckgabeBuch);
		buttonsBuecher.setPrefWidth(140);

		//TableColumns und TableView Bücher
		TableColumn<BuchFX, String> colIsbn = new TableColumn<>("ISBN");
		colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		TableColumn<BuchFX, String> colTitel = new TableColumn<>("Titel");
		colTitel.setCellValueFactory(new PropertyValueFactory<>("titel"));
		TableColumn<BuchFX, String> colAutor = new TableColumn<>("Autor");
		colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
		TableColumn<BuchFX, Integer> colJahr = new TableColumn<>("Jahr");
		colJahr.setCellValueFactory(new PropertyValueFactory<>("jahr"));
		TableColumn<BuchFX, String> colThema = new TableColumn<>("Thema");
		colThema.setCellValueFactory(new PropertyValueFactory<>("thema"));
		TableColumn<BuchFX, String> colEntlehnt = new TableColumn<>("Verfügbar");
		colEntlehnt.setCellValueFactory(new PropertyValueFactory<>("entlehnt"));

		TableView<BuchFX> tvBuecher = new TableView<>(obsListBuecher);
		tvBuecher.getColumns().addAll(colIsbn, colTitel, colAutor, colThema, colJahr, colEntlehnt);
		tvBuecher.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	
		tvBuecher.setPrefWidth(780);

		//Width Property von TableColumns
		colIsbn.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()).subtract(25));
		colTitel.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()).add(70));
		colAutor.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()));
		colJahr.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()).subtract(50));
		colThema.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()));
		colEntlehnt.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()).subtract(0));

		//Style von TableColumns
		colIsbn.setStyle( "-fx-alignment: CENTER;");
		colTitel.setStyle( "-fx-alignment: CENTER;");
		colAutor.setStyle( "-fx-alignment: CENTER;");
		colJahr.setStyle( "-fx-alignment: CENTER;");
		colThema.setStyle( "-fx-alignment: CENTER;");
		colEntlehnt.setStyle( "-fx-alignment: CENTER;");

		//Schnelle Preview von Titelblatt sowie aktuelle Ausleihe
		Label coverGross = new Label();
		Image gen_cover = new Image(Paths.get(genericPath).toUri().toString());
		ImageView iv = new ImageView();
		iv.setImage(gen_cover);
		iv.setFitWidth(185);
		iv.setFitHeight(260);
		coverGross.setGraphic(iv);

		//Zeigt Rückgabedatum falls das Buch entlehnt ist
		Label status = new Label("");	
		status.setPadding(new Insets(0, 0, 0, 40));
		status.setPrefHeight(40);
		//Wenn auf Status geklickt wird, kann der Benutzer die Besucherdetails des Besuchers, der das Buch hat, ansehen 
		status.setOnMouseClicked(e ->{
			if(ausleihe_besucher_id != 0) {
				Besucher besucher = null;
				try {
					besucher = Datenbank.getSingleBesucher(ausleihe_besucher_id);
				} catch (SQLException e1) {					
					e1.printStackTrace();
				}
				Besucherdetails b = new Besucherdetails(besucher);
				b.showAndWait();
				status.setText("");
				buecherSuchen();
			}
		});
		buttonsBuecher.getChildren().add(coverGross);
		buttonsBuecher.getChildren().add(status);

		//Das BUtton Neues Buch ist immer greifbar. Andere Buttons (Enfernen, Ausleihen oder Zurpckgeben) werden nur bei der Auswahl eines Buches aktiviert
		tvBuecher.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BuchFX>() {
			@Override
			public void changed(ObservableValue<? extends BuchFX> arg0, BuchFX arg1, BuchFX arg2) {
				if(arg2 != null) {
					bearbeitenBuch.setDisable(false);
					entfernenBuch.setDisable(false);
					if(!arg2.getModellBuch().isEntlehnt()) {
						ausleihenBuch.setDisable(false);
						ruckgabeBuch.setDisable(true);
						status.setText("");
					}					
					if(arg2.getModellBuch().isEntlehnt()) {
						ausleihenBuch.setDisable(true);
						ruckgabeBuch.setDisable(false);
						/*
						 * Falls das Buch entlehtn ist, eine Suche nach Buch ISBN wird dürchegeführt, um 
						 * auf das Rückgabe Datum und die Details des Besuchers zuzugreifen
						 */
						Ausleihe ausleihe = null;
						ausleihe = getAusleihe(arg2.getModellBuch().getIsbn());	
						ausleihe_besucher_id = ausleihe.getBesucher().getBesucherId();
						status.setText("Entlehnt bis zum\n" + formatDate(ausleihe.getBis()));
						/*
						 * Falls das Buch überfällig ist, wird das Status Label rot angezeigt
						 * Ansonnst wird es Schwartz angezeigt
						 */
						if(LocalDate.now().compareTo(ausleihe.getBis()) > 0)
							status.setStyle("-fx-font-size: 13; -fx-text-fill: red; -fx-font-weight: bold; -fx-underline: true; "
									+ "-fx-text-alignment: center");
						else
							status.setStyle("-fx-font-size: 13; -fx-text-fill: black; -fx-font-weight:bold; -fx-underline: true; "
									+ "-fx-text-alignment: center");
					}
					/*
					 * Suche vom Titelblatt des Buches
					 * Falls die Methode null returns, ein Vordefiniertes Standard Bildpath wird verwendet
					 */
					if(readBild(arg2.getModellBuch().getTitelblatt()) == null) {
						arg2.getModellBuch().setTitelblatt(".\\resources\\titelblatt_generic.jpg");						
					}	
					if(readBild(arg2.getModellBuch().getTitelblatt()) != null) {
						if(arg2.getModellBuch().getTitelblatt().equals(".\\resources\\titelblatt_generic.jpg")) {
							iv.setImage(gen_cover);
							return;
						} else {
							iv.setImage(readBild(arg2.getModellBuch().getTitelblatt()));
						}
					}
				} else {
					bearbeitenBuch.setDisable(true);
					entfernenBuch.setDisable(true);
					ausleihenBuch.setDisable(true);
					ruckgabeBuch.setDisable(true);
					status.setText("");
					iv.setImage(gen_cover);
				}
				return;
			}
		});

		//Beim doppelklick wird ein Buchdetails fenster geöffnet
		tvBuecher.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouse) {
				if(mouse.getButton().equals(MouseButton.PRIMARY) && mouse.getClickCount() == 2) {
					if(tvBuecher.getSelectionModel().getSelectedItem() != null) {
						Optional<ButtonType> r = new Buchdetails(tvBuecher.getSelectionModel().getSelectedItem().
								getModellBuch()).showAndWait();
						if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
							buecherSuchen();
							themenSuchen();
						}
					}
				}
			}
		});

		//SetOnAction für die Buttons neuesBuch, bearbeitenBuch, entfernenBuch, ausleihen und rückgabe
		neuesBuch.setOnAction(e -> {
			Buch buch = new Buch();
			Optional<ButtonType> r = new Buchdetails(buch).showAndWait();
			if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {				
				buecherSuchen();
				themenSuchen();
			}				
		});

		//Bearbeitung von Buchdaten
		bearbeitenBuch.setOnAction(e ->{
			Optional<ButtonType> r = new Buchdetails(tvBuecher.getSelectionModel().getSelectedItem().
					getModellBuch()).showAndWait();
			if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
				buecherSuchen();
				themenSuchen();
			}

		});

		//Entfernen von Bücher
		entfernenBuch.setOnAction(e ->{			
			if(!tvBuecher.getSelectionModel().getSelectedItem().getModellBuch().isEntlehnt()) {				
				Alert bestaetigung= new Alert(AlertType.CONFIRMATION);
				bestaetigung.setTitle("Achtung");
				bestaetigung.setContentText("Möchten Sie das Buch wirklich löschen?");
				Optional<ButtonType> r = bestaetigung.showAndWait();
				/*
				 * Buch Entfernen wird das Buch Feld isGeloescht auf true setzen
				 */
				if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
					try {
						Datenbank.deleteBuch(tvBuecher.getSelectionModel().getSelectedItem().getModellBuch());
						buecherSuchen();
					} catch (SQLException e1) {
						new Alert(AlertType.ERROR, e1.toString()).showAndWait();
					}
				}
				return;
			}			
			new Alert(AlertType.ERROR, "Ausgeliehene Bücher dürfen nicht gelöscht werden").showAndWait();
			e.consume();
			return;

		});

		//Ausleihe von Bücher
		ausleihenBuch.setOnAction(e ->{
			Buch buch = tvBuecher.getSelectionModel().getSelectedItem().getModellBuch();
			//Buch Ausleihen wird ein Ausleihedetails Fenster anzeigen 
			Optional<ButtonType> r = new Ausleihedetails(buch).showAndWait();
			if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
				buecherSuchen();
				besucherSuchen();
			}
		});

		/*
		 * Rückgabe von Bücher wird die Ausleihe erledigen und das Buch isEntlehnt Feld
		 * auf false setzen.
		 */		
		ruckgabeBuch.setOnAction(e ->{
			Buch buch = tvBuecher.getSelectionModel().getSelectedItem().getModellBuch();
			/*
			 * Bei der Rückgabe von Bücher werden die Ausleihebemerkugen 
			 * eingetragen
			 */
			TextInputDialog tid = new TextInputDialog();			
			tid.setTitle("Rückgabe");
			tid.setHeaderText("Bemerkungen?");
			Optional <String> r = tid.showAndWait();			
			if(r.isPresent() && r.get().length() > 0) {
				try {
					Datenbank.setAusleiheBemerkugen(buch.getIsbn(), r.get());
				} catch (SQLException e1) {
					new Alert(AlertType.ERROR, "Fehler bei eingabe von Bemerkungen").showAndWait();
				}
			}			
			try {
				/*
				 *Ausleihe erledigen(Set erledigt auf true)
				 *Dann set Buch isEntlehnt auf false
				 */
				Datenbank.ausleiheErledigen(buch.getIsbn());
				Datenbank.setBuchEntlehnt(buch, false);
			} catch (SQLException e1) {				
				new Alert(AlertType.ERROR, "Fehler bei der Rückgabe vom Buch").showAndWait();
				e.consume();
				return;
			}
			buecherSuchen();
			status.setText("");
		});	

		//Buch Tab
		AnchorPane anchorBuch = new AnchorPane();

		AnchorPane.setTopAnchor(maSucheBuecher, 10d);
		AnchorPane.setLeftAnchor(maSucheBuecher, 10d);		
		AnchorPane.setRightAnchor(maSucheBuecher, 10d);
		AnchorPane.setBottomAnchor(maSucheBuecher, 10d);

		AnchorPane.setTopAnchor(buttonsBuecher, 60d);
		AnchorPane.setLeftAnchor(buttonsBuecher, 10d);
		AnchorPane.setBottomAnchor(buttonsBuecher, 10d);	

		AnchorPane.setTopAnchor(tvBuecher, 60d);
		AnchorPane.setLeftAnchor(tvBuecher, 205d);
		AnchorPane.setRightAnchor(tvBuecher, 10d);
		AnchorPane.setBottomAnchor(tvBuecher, 10d);	

		anchorBuch.getChildren().addAll(maSucheBuecher, buttonsBuecher, tvBuecher);

		//Tab, Buttons und Tableview für die Verwaltung und Darstellung von Besucher der Bibliothek		
		Tab besucherVerwaltung = new Tab("Besucher");
		Label lbl_suche1 = new Label("Suche nach:");

		//Buttons
		Button neuenBesucher = new Button("Neuer Besucher");
		Button bearbeitenBesucher = new Button("Besucher bearbeiten");
		Button entfernenBesucher = new Button("Besucher entfernen");
		bearbeitenBesucher.setDisable(true);
		entfernenBesucher.setDisable(true);	
		VBox buttonsBesucher = new VBox(10, neuenBesucher, bearbeitenBesucher, entfernenBesucher);
		buttonsBesucher.setPrefWidth(140);

		//Suche Optionen Besucher		
		RadioButton rb_besucherId = new RadioButton("ID");
		RadioButton rb_besucherName = new RadioButton("NACHNAME");
		ToggleGroup tg2 = new ToggleGroup();
		rb_besucherId.setToggleGroup(tg2);
		rb_besucherName.setToggleGroup(tg2);			
		HBox hb_radioButtonsBesucher = new HBox(15, lbl_suche1, rb_besucherId, rb_besucherName);
		hb_radioButtonsBesucher.setPadding(new Insets(5));	

		//Besucher Tab Suchen Button
		Button suchen1 = new Button("suchen");
		suchen1.setOnAction(e ->{	
			//Falls kein input von Benutzer, werden alle Besucher gelesen
			if(besucherSuche_input.getLength() == 0) {
				besucherSuchen();
				e.consume();
				return;
			} else {
				if(rb_besucherId.isSelected()) {
					//NumberFormatkontrol falls der Besucher eine ID suche dürchführen will
					try {
						Long.parseLong(besucherSuche_input.getText());						
					}catch(NumberFormatException e1){
						new Alert(AlertType.ERROR, "Nur Zahlen sind erlaubt").showAndWait();
						besucherSuche_input.clear();
						e.consume();
						return;
					}					
					besucherSuchen(Besuchersuche.ID);
					e.consume();
					return;
				}
				if(rb_besucherName.isSelected()) {
					besucherSuchen(Besuchersuche.NAME);
					e.consume();
					return;
				}
			}
			new Alert(AlertType.ERROR, "Besucher ID oder NAME auswählen").showAndWait();
			e.consume();
			return;
		});

		//Node behälter für die Suche Elemente Radiobuttons, Textfeld und Button "Suchen"
		HBox maSucheBesucher = new HBox(13, hb_radioButtonsBesucher, besucherSuche_input, suchen1);
		maSucheBesucher.setPadding(new Insets(15, 0, 3, 0));

		//BesucherSuche input TextFeld size und "Suchen" vorhandene Text
		besucherSuche_input.setPrefWidth(250);
		besucherSuche_input.setOnMouseClicked(e ->{
			if(besucherSuche_input.getText().equals("Suchen"))
				besucherSuche_input.clear();
		});	

		//Besuchersuche input TextFeld Enter Taste eventHaendler
		besucherSuche_input.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if(arg0.getCode().equals(KeyCode.ENTER))
					if(besucherSuche_input.getText().length() == 0)
						besucherSuchen();
					else if(rb_besucherId.isSelected()) {
						try {
							Long.parseLong(besucherSuche_input.getText());						
						} catch(NumberFormatException e1){
							new Alert(AlertType.ERROR, "Nur Zahlen sind erlaubt").showAndWait();
							besucherSuche_input.clear();
							return;
						}
						besucherSuchen(Besuchersuche.ID);
					}
					else if(rb_besucherName.isSelected()) {
						besucherSuchen(Besuchersuche.NAME);
					}
					else {
						new Alert(AlertType.ERROR, "Besucher ID oder NAME auswählen").showAndWait();						
						return;
					}
			}
		});

		//TableColumns und TableView Besucher
		TableColumn<BesucherFX, Long> colBesucherid = new TableColumn<>("Besucher ID");
		colBesucherid.setCellValueFactory(new PropertyValueFactory<>("besucherId"));
		TableColumn<BesucherFX, String> colVorname = new TableColumn<>("Vorname");
		colVorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		TableColumn<BesucherFX, String> colNachname = new TableColumn<>("Nachname");
		colNachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));		
		TableColumn<BesucherFX, String> colGebdatum = new TableColumn<>("Geburtsdatum");
		colGebdatum.setCellValueFactory(new PropertyValueFactory<>("gebDatum"));
		TableColumn<BesucherFX, String> colTel = new TableColumn<>("Telefonnummer");
		colTel.setCellValueFactory(new PropertyValueFactory<>("besucherTelefon"));
		TableColumn<BesucherFX, String> colAdresse = new TableColumn<>("Adresse");
		colAdresse.setCellValueFactory(new PropertyValueFactory<>("besucherAdresse"));		

		TableView<BesucherFX> tvBesucher = new TableView<>(obsListBesucher);
		tvBesucher.getColumns().addAll(colBesucherid, colVorname, colNachname, colGebdatum,
				colTel, colAdresse);
		tvBesucher.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);	

		//Width Property für die TableColumns
		colBesucherid.prefWidthProperty().bind(tvBesucher.widthProperty().divide(tvBesucher.getColumns().size()).subtract(5));
		colVorname.prefWidthProperty().bind(tvBesucher.widthProperty().divide(tvBesucher.getColumns().size()));
		colNachname.prefWidthProperty().bind(tvBesucher.widthProperty().divide(tvBesucher.getColumns().size()));
		colGebdatum.prefWidthProperty().bind(tvBesucher.widthProperty().divide(tvBesucher.getColumns().size()).subtract(15));
		colTel.prefWidthProperty().bind(tvBesucher.widthProperty().divide(tvBesucher.getColumns().size()));
		colAdresse.prefWidthProperty().bind(tvBesucher.widthProperty().divide(tvBesucher.getColumns().size()).add(12));	

		//Style Property für die TableColumns
		colBesucherid.setStyle("-fx-alignment: CENTER;");
		colVorname.setStyle("-fx-alignment: CENTER;");
		colNachname.setStyle("-fx-alignment: CENTER;");
		colGebdatum.setStyle("-fx-alignment: CENTER;");
		colTel.setStyle("-fx-alignment: CENTER;");
		colAdresse.setStyle("-fx-alignment: CENTER;");

		//Schaltung von Knopfe anhängig von Auswahl in der Tabelle
		tvBesucher.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BesucherFX>() {
			@Override
			public void changed(ObservableValue<? extends BesucherFX> arg0, BesucherFX arg1, BesucherFX arg2) {
				if(arg2 != null) {
					bearbeitenBesucher.setDisable(false);
					entfernenBesucher.setDisable(false);
					//System.out.println(arg2.getBesucherId());
				} else {
					bearbeitenBesucher.setDisable(true);
					entfernenBesucher.setDisable(true);					
				}
				return;
			}
		});

		//Beim doppelklick wird ein Buchdetails fenster geöffnet
		tvBesucher.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouse) {
				if(mouse.getButton().equals(MouseButton.PRIMARY) && mouse.getClickCount() == 2) {
					if(tvBesucher.getSelectionModel().getSelectedItem() != null) {
						Optional<ButtonType> r = new Besucherdetails(tvBesucher.getSelectionModel().getSelectedItem().
								getModellBesucher()).showAndWait();
						if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
							besucherSuchen();
						}
					}
				}
				buecherSuchen();
			}
		});

		//SetOnAction für neuenBesucher, beabeitenBesucher, entferenBesucher
		neuenBesucher.setOnAction(e -> {
			Besucher besucher = new Besucher();			
			Optional<ButtonType> r = new Besucherdetails(besucher).showAndWait();
			if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {				
				besucherSuchen();					
			}
		});

		//Bearbeiten von Besucher
		bearbeitenBesucher.setOnAction(e ->{
			Optional<ButtonType> r = new Besucherdetails(tvBesucher.getSelectionModel().getSelectedItem().
					getModellBesucher()).showAndWait();
			if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {				
				besucherSuchen();
			}
			buecherSuchen();			
		});	

		//Entfernen von Besucher
		entfernenBesucher.setOnAction(e ->{
			ArrayList<Ausleihe> oeffeneAusleihen = new ArrayList<>();
			try {
				oeffeneAusleihen = Datenbank.readBesucherAusleihen(tvBesucher.getSelectionModel().
						getSelectedItem().getBesucherId());
			} catch (SQLException e2) {				
				e2.printStackTrace();
			}
			//Nur Besucher ohne Bücher dürfen gelöscht werden
			if(oeffeneAusleihen.isEmpty()) {
				Alert bestaetigung = new Alert(AlertType.CONFIRMATION);
				bestaetigung.setTitle("Achtung");
				bestaetigung.setContentText("Möchten Sie der Besucher wirklich löschen?");
				Optional<ButtonType> r = bestaetigung.showAndWait();
				if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
					try {
						Datenbank.deleteBesucher(tvBesucher.getSelectionModel().getSelectedItem().getModellBesucher());
						besucherSuchen();
					} catch (SQLException e1) {
						new Alert(AlertType.ERROR, e1.toString()).showAndWait();
						e.consume();
						return;
					}
				}
			} else {
				new Alert(AlertType.ERROR, "Besucher mit ausgeliehene Bücher dürfen nicht gelöscht werden").showAndWait();
				e.consume();
				return;
			}
		});

		//Besucher Tab
		AnchorPane anchorBesucher = new AnchorPane();		
		AnchorPane.setTopAnchor(maSucheBesucher, 10d);
		AnchorPane.setLeftAnchor(maSucheBesucher, 10d);		
		AnchorPane.setRightAnchor(maSucheBesucher, 10d);
		AnchorPane.setBottomAnchor(maSucheBesucher, 10d);

		AnchorPane.setTopAnchor(buttonsBesucher, 60d);
		AnchorPane.setLeftAnchor(buttonsBesucher, 10d);

		AnchorPane.setTopAnchor(tvBesucher, 60d);
		AnchorPane.setLeftAnchor(tvBesucher, 150d);
		AnchorPane.setRightAnchor(tvBesucher, 10d);
		AnchorPane.setBottomAnchor(tvBesucher, 10d);

		anchorBesucher.getChildren().addAll(maSucheBesucher, buttonsBesucher, tvBesucher);		

		//Tab, Buttons und Tableview für die Darstellung von Statistiken
		Tab statistiken = new Tab("Statistiken");
		Label lbl = new Label("Filter: ");
		lbl.setPadding(new Insets(5));
		ComboBox<String> filter = new ComboBox<>(FXCollections.observableArrayList("Alle Ausleihen","Entlehnte Bücher", "Überfällige Bücher"));
		filter.getSelectionModel().select(0);

		//Zeigt Statistiken nach ComboBox auswahl
		Button statsAnzeigen = new Button("Anzeigen");		
		statsAnzeigen.setOnAction(e ->{
			if(filter.getSelectionModel().getSelectedItem().equals("Entlehnte Bücher")) {
				statistikenAnzeigen(Statistiksuche.ENTLEHNTE);
				e.consume();
				return;				
			}
			if(filter.getSelectionModel().getSelectedItem().equals("Überfällige Bücher")) {
				statistikenAnzeigen(Statistiksuche.UEBERFAELLIG);
				e.consume();
				return;	
			}
			if(filter.getSelectionModel().getSelectedItem().equals("Alle Ausleihen")) {
				statistikenAnzeigen(Statistiksuche.ALLE);
				e.consume();
				return;	
			}			
		});		

		//TableColumns und TableView
		TableColumn<AusleiheFX, Integer> colAusleiheID = new TableColumn<>("ID");	
		TableColumn<AusleiheFX, String> colIsbnStats = new TableColumn<>("ISBN");				
		TableColumn<AusleiheFX, String> colTitelStats = new TableColumn<>("Titel");		
		TableColumn<AusleiheFX, String> colAutorStats = new TableColumn<>("Autor");	
		TableColumn<AusleiheFX, String> colVonStats = new TableColumn<>("Entlehnt von");
		TableColumn<AusleiheFX, String> colEntlehntStats = new TableColumn<>("Entlehnt bis");
		TableColumn<AusleiheFX, String> colBesucherName = new TableColumn<>("Besucher");
		TableColumn<AusleiheFX, Long> colBesucherID = new TableColumn<>("Besucher ID");
		TableColumn<AusleiheFX, String> colBemerkungen = new TableColumn<>("Bemerkungen");

		colAusleiheID.setCellValueFactory(new PropertyValueFactory<>("ausleihe_ID"));
		colIsbnStats.setCellValueFactory(new PropertyValueFactory<>("buch_isbn"));
		colTitelStats.setCellValueFactory(new PropertyValueFactory<>("buch_tiltel"));
		colAutorStats.setCellValueFactory(new PropertyValueFactory<>("buch_autor"));
		colEntlehntStats.setCellValueFactory(new PropertyValueFactory<>("entlehnt_bis"));
		colVonStats.setCellValueFactory(new PropertyValueFactory<>("entlehnt_von"));
		colBesucherName.setCellValueFactory(new PropertyValueFactory<>("besucher_name"));
		colBesucherID.setCellValueFactory(new PropertyValueFactory<>("aus_besucherId"));
		colBemerkungen.setCellValueFactory(new PropertyValueFactory<>("bemerkungen"));		

		TableView<AusleiheFX> tvStats = new TableView<>(obsListAusleihen);
		tvStats.getColumns().addAll(colAusleiheID, colIsbnStats, colTitelStats, colAutorStats, colVonStats, colEntlehntStats,
				colBesucherName, colBesucherID, colBemerkungen);

		//Width Property für TableColumns
		colAusleiheID.prefWidthProperty().bind(tvStats.widthProperty().divide(tvStats.getColumns().size()).subtract(60));
		colIsbnStats.prefWidthProperty().bind(tvStats.widthProperty().divide(tvStats.getColumns().size()).subtract(10));
		colTitelStats.prefWidthProperty().bind(tvStats.widthProperty().divide(tvStats.getColumns().size()).add(40));
		colAutorStats.prefWidthProperty().bind(tvStats.widthProperty().divide(tvStats.getColumns().size()));
		colEntlehntStats.prefWidthProperty().bind(tvStats.widthProperty().divide(tvStats.getColumns().size()).subtract(10));
		colBesucherName.prefWidthProperty().bind(tvStats.widthProperty().divide(tvStats.getColumns().size()));
		colBesucherID.prefWidthProperty().bind(tvStats.widthProperty().divide(tvStats.getColumns().size()).add(7));
		colBemerkungen.prefWidthProperty().bind(tvStats.widthProperty().divide(tvStats.getColumns().size()).add(53));

		//Style von TableColumns
		colAusleiheID.setStyle("-fx-alignment: CENTER;");
		colIsbnStats.setStyle("-fx-alignment: CENTER;");
		colTitelStats.setStyle("-fx-alignment: CENTER;");
		colAutorStats.setStyle("-fx-alignment: CENTER;");
		colEntlehntStats.setStyle("-fx-alignment: CENTER;");
		colBesucherName.setStyle("-fx-alignment: CENTER;");
		colBesucherID.setStyle("-fx-alignment: CENTER;");
		colBemerkungen.setStyle("-fx-alignment: CENTER;");

		//Box für die Suche Node Elemente
		HBox hbFilter = new HBox(10, lbl, filter, statsAnzeigen);
		hbFilter.setPadding(new Insets(15, 0, 3, 0));
		VBox vbStats = new VBox(10, hbFilter, tvStats);
		vbStats.setPadding(new Insets(10));

		//Stats Tab
		AnchorPane anchorStats = new AnchorPane();

		AnchorPane.setTopAnchor(hbFilter, 10d);
		AnchorPane.setLeftAnchor(hbFilter, 10d);		
		AnchorPane.setRightAnchor(hbFilter, 10d);
		AnchorPane.setBottomAnchor(hbFilter, 10d);

		AnchorPane.setTopAnchor(tvStats, 60d);
		AnchorPane.setLeftAnchor(tvStats, 10d);
		AnchorPane.setRightAnchor(tvStats, 10d);
		AnchorPane.setBottomAnchor(tvStats, 10d);

		anchorStats.getChildren().addAll(hbFilter, tvStats);

		katalog.setContent(anchorBuch);					
		besucherVerwaltung.setContent(anchorBesucher);
		statistiken.setContent(anchorStats);

		//Main Rootnode
		TabPane root = new TabPane(katalog, besucherVerwaltung, statistiken);
		root.tabMinWidthProperty().bind(root.widthProperty().divide(root.getTabs().size()).subtract(20));
		root.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		//Style von Cursor in beziehung des Status Label
		status.setOnMouseEntered(new EventHandler<Event>() {			
			@Override
			public void handle(Event arg0) {
				root.setCursor(Cursor.HAND);				
			}
		});
		status.setOnMouseExited(new EventHandler<Event>() {			
			@Override
			public void handle(Event arg0) {
				root.setCursor(Cursor.DEFAULT);				
			}
		});

		//Bücher, Besucher und Themen aus der Datenbank holen
		buecherSuchen();
		besucherSuchen();
		themenSuchen();		

		//SetOnMouse Klicked RadioButtons(ISBN, Titel, Autor) versus ComboBox von Themen
		rb_isbn.setOnMouseClicked(e -> cbThemen.getSelectionModel().select(0));
		rb_titel.setOnMouseClicked(e -> cbThemen.getSelectionModel().select(0));
		rb_autor.setOnMouseClicked(e -> cbThemen.getSelectionModel().select(0));

		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Bibliotheksverwaltungssystem");
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	

	//Alle Bücher suchen
	private void buecherSuchen() {				
		try {
			ArrayList<Buch> alBuecher = Datenbank.readBuecher();
			obsListBuecher.clear();
			for(Buch einBuch : alBuecher) {				
				obsListBuecher.add(new BuchFX(einBuch));
			}
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}							
	}

	/**
	 * Bücher nach ISBN, Titel oder Autor suchen
	 * @param auswahl
	 */
	private void buecherSuchen(Buchsuche auswahl) {		
		switch(auswahl) {
		case ISBN:
			try {				
				ArrayList<Buch> alBuecher = Datenbank.readIsbnBuecher(buchSuche_input.getText());
				obsListBuecher.clear();
				for(Buch einBuch : alBuecher) {
					obsListBuecher.add(new BuchFX(einBuch));
				}				
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
			break;			
		case AUTOR:
			try {				
				ArrayList<Buch> alBuecher = Datenbank.readAutorBuecher(buchSuche_input.getText());
				obsListBuecher.clear();
				for(Buch einBuch : alBuecher) {
					obsListBuecher.add(new BuchFX(einBuch));
				}				
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
			break;		
		case TITEL:
			try {				
				ArrayList<Buch> alBuecher = Datenbank.readTitelBuecher(buchSuche_input.getText());
				obsListBuecher.clear();
				for(Buch einBuch : alBuecher) {
					obsListBuecher.add(new BuchFX(einBuch));
				}				
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
			break;
		default:
			try {				
				ArrayList<Buch> alBuecher = Datenbank.readThemaBuecher(cbThemen.getSelectionModel().
						getSelectedItem().getThema_ID());
				obsListBuecher.clear();
				for(Buch einBuch : alBuecher) {
					obsListBuecher.add(new BuchFX(einBuch));
				}				
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
			break;			
		}
	}

	/**
	 * Suche alle Besucher
	 */
	private void besucherSuchen() {
		try {
			ArrayList<Besucher> alBesucher = Datenbank.readBesucher();
			obsListBesucher.clear();
			for(Besucher einBesucher : alBesucher) {
				obsListBesucher.add(new BesucherFX(einBesucher));
			}
		} catch (SQLException e) {			
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
	}

	/**
	 * Suche von Besucher nach ID oder Nachname
	 * @param auswahl 
	 */
	private void besucherSuchen(Besuchersuche auswahl) {
		switch(auswahl) {
		case ID:
			try {
				Besucher besucher = Datenbank.getSingleBesucher(Long.parseLong(besucherSuche_input.getText()));
				if(besucher == null) {
					new Alert(AlertType.ERROR, "ID-Nummer nicht bekannt").showAndWait();					
					break;
				}
				obsListBesucher.clear();
				obsListBesucher.add(new BesucherFX(besucher));
			} catch (NumberFormatException | SQLException e) {	
				new Alert(AlertType.ERROR, e.toString()).showAndWait();				
			}			
			break;
		default:
			try {
				ArrayList<Besucher> alBesucher = Datenbank.readBesucher(besucherSuche_input.getText());
				obsListBesucher.clear();
				for(Besucher einBesucher : alBesucher) {
					obsListBesucher.add(new BesucherFX(einBesucher));
				}
			} catch (SQLException e) {
				new Alert(AlertType.ERROR, e.toString()).showAndWait();				
			}			
			break;
		}
	}

	//Alle Themen suchen
	private void themenSuchen() {
		try {
			ArrayList<Thema> alThemen = Datenbank.readThemen();
			obsListThemen.clear();
			obsListThemen.add(new Thema(0, "THEMA"));
			obsListThemen.addAll(alThemen);
			cbThemen.getSelectionModel().select(0);	
		} catch(SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
	}

	/**
	 * Methode für die Unterschiedliche Statistiken Filter Optionen :
	 * Enlehnte, Überfällige oder Alle
	 * @param auswahl 
	 */
	private void statistikenAnzeigen(Statistiksuche auswahl) {
		switch(auswahl) {
		case ENTLEHNTE:
			try {
				ArrayList<Ausleihe> alAusleihen = Datenbank.readAktiveAusleihen();				
				obsListAusleihen.clear();
				for(Ausleihe eineAusleihe : alAusleihen) {
					obsListAusleihen.add(new AusleiheFX(eineAusleihe));
				}				
			} catch (SQLException e) {				
				new Alert(AlertType.ERROR, e.toString()).showAndWait();
			}
			break;
		case UEBERFAELLIG:
			try {
				ArrayList<Ausleihe> alAusleihen = Datenbank.readUeberfaellige();
				obsListAusleihen.clear();
				for(Ausleihe eineAusleihe : alAusleihen) {
					obsListAusleihen.add(new AusleiheFX(eineAusleihe));
				}				
			}catch (SQLException e) {				
				new Alert(AlertType.ERROR, e.toString()).showAndWait();
			}
			break;
		default:
			try {
				ArrayList<Ausleihe> alAusleihen = Datenbank.readAlleAusleihen();				
				obsListAusleihen.clear();
				for(Ausleihe eineAusleihe : alAusleihen) {
					obsListAusleihen.add(new AusleiheFX(eineAusleihe));
				}				
			} catch (SQLException e) {				
				new Alert(AlertType.ERROR, e.toString()).showAndWait();
			}
		}
	}

	/**
	 * Suche von Besucher einer Ausleihe nach AUSLEIHE_BUCH_ISBN 
	 * @param buchISBN
	 * @return Aktive Ausleihe Object nach Buch ISBN
	 */
	private Ausleihe getAusleihe(String buchISBN) {
		Ausleihe ausleihe = new Ausleihe();
		try {
			ausleihe = Datenbank.readSingleAusleihe(buchISBN);
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
		return ausleihe;
	}

	/**
	 * Suche von Titelblatt
	 * @param bildPath
	 * @return Image falls der Bildpath gültig ist. Ansonsten returns null
	 */
	private Image readBild(String bildPath) {			
		Image i = null;
		if(bildPath != null && bildPath.length() > 0) {
			try {
				InputStream stream = new FileInputStream(bildPath);
				i = new Image(stream);
				stream.close();
				return i;
			} catch (IOException e) {
				new Alert(AlertType.ERROR, "Titelblatt nicht gefunden").showAndWait();				
			}
		}
		return null;
	}

	/**
	 * Format von Datum
	 * @param date
	 * @return a Formated LocalDate
	 */
	private String formatDate(LocalDate date) {		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
		String formatedDate = date.format(formatter);		
		return formatedDate;
	}
	
}
