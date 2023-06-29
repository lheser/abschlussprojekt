package klassen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class Buchdetails extends Dialog<ButtonType> {

	private double fieldsWidth = 300;	
	private ArrayList<Thema> themen = new ArrayList<>();
	private ObservableList<Thema> obsListThemen = FXCollections.observableArrayList();	
	private String genericPath = ".\\resources\\titelblatt_generic.jpg";
	private final int zeichenMax = 500;
	private int zeichenCounter = 0;

	public Buchdetails(Buch buch) {
		this.setTitle("Details des Buches");
		try {
			themen = Datenbank.readThemen();			
			obsListThemen.addAll(themen);			
		} catch (SQLException e1) {
			new Alert(AlertType.ERROR, e1.toString()).showAndWait();
		}

		ImageView iv = new ImageView();	
		
		//Spalte links für die Labels
		Label isbn = new Label("ISBN");
		Label titel = new Label("Titel");
		Label autor = new Label("Autor");		
		Label thema = new Label("Thema");
		Label jahr = new Label("Erscheinungsjahr");
		Label preis = new Label("Preis");
		Label verlag = new Label("Verlag");
		Label titelblatt = new Label("Titelblatt");
		Label beschreibung = new Label("Beschreibung");

		//Spalte Rechts. Buch Daten werden falls vorhanden angezeigt
		TextField isbn_in = new TextField();	
		if(buch.getIsbn()!= null && buch.getIsbn().length() > 0) {
			isbn_in.setText(buch.getIsbn());
			isbn_in.setEditable(false);				
			isbn_in.setStyle("-fx-opacity: 0.65;");						
		}			
		isbn_in.setPrefWidth(fieldsWidth);
		TextField autor_in = new TextField(buch.getAutor());
		autor_in.setPrefWidth(fieldsWidth);
		TextField titel_in = new TextField(buch.getTitel());
		titel_in.setPrefWidth(fieldsWidth);

		//Themen Auswahl
		ComboBox<Thema> cbThemen = new ComboBox<>(obsListThemen);	
		if(buch.getThema()!= null)
			//TODO aendern
			for(Thema einThema : themen) {
				if(einThema.getThema_ID() == buch.getThema().getThema_ID()) {
					buch.getThema().setBezeichnung(einThema.getBezeichnung());
					cbThemen.getSelectionModel().select(buch.getThema());
				}
			}
		
		cbThemen.setVisibleRowCount(8);		
		
		//Erzeugung ein neues Thema
		Button neuesThema = new Button("Neues Thema");		
		neuesThema.setOnAction(e -> {
			TextInputDialog tid = new TextInputDialog();
			tid.setTitle("Neues Thema");
			tid.setHeaderText("Neues Thema eingeben");
			Optional <String> r = tid.showAndWait();
			if(r.isPresent()) {
				//Falls das Thema unbekannt ist
				if(isNeuesThema(r.get()) && r.get().length() > 0) {	
					Thema t = new Thema(0, r.get());
					try {
						Datenbank.insertThema(t);
						themen.clear();
						themen = Datenbank.readThemen();
						obsListThemen.clear();
						obsListThemen.addAll(themen);						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}					
				} else {	
					//Meldung falls das Thema schon bekannt ist
					new Alert(AlertType.ERROR, "Das Thema" + r.get() + " ist schon bekannt").showAndWait();	
				}
			}
		});
		
		TextField jahr_in = new TextField(Integer.toString(buch.getJahr()));
		jahr_in.setPrefWidth(fieldsWidth);
		TextField verlag_in = new TextField(buch.getVerlag());
		verlag_in.setPrefWidth(fieldsWidth);
		TextField preis_in = new TextField(Double.toString(buch.getPreis()));		
		preis_in.setPrefWidth(fieldsWidth);	

		/**
		 * BildLabel für das Titelblatt
		 * Falls das Lable geklickt wird, wird ein FileChooserfenster angezeigt
		 */
		Label bild = new Label();	
		bild.setOnMouseClicked(e ->{
			FileChooser fch = new FileChooser();
			fch.setTitle("Bild auswählen");
			fch.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image file", "*.png", "*.jpg"));
			File f = fch.showOpenDialog(null);
			if(f != null) {
				buch.setTitelblatt(f.getAbsolutePath());
				iv.setImage(readBild(buch.getTitelblatt()));
			}			
			e.consume();
			return;
		});	
		
		/**
		 * Suche und anzaige von Titelblatt. Falls nich gefunden
		 * wird ein generisches Bild angezeigt
		 */
		if(readBild(buch.getTitelblatt()) == null)
			buch.setTitelblatt(genericPath);		
		iv.setImage(readBild(buch.getTitelblatt()));
		iv.setPreserveRatio(true);		
		iv.setFitHeight(200);
		bild.setGraphic(iv);
		//Sytle von Cursor in Bezeihung zum Bild Label 
		bild.setStyle("-fx-cursor: hand");

		//Text area für die Beshcreibung
		TextArea txtArea = new TextArea();
		txtArea.setText(buch.getBeschreibung());
		if(buch.getBeschreibung() != null && buch.getBeschreibung().length() > 0)
			zeichenCounter = buch.getBeschreibung().length();
		txtArea.setPrefSize(fieldsWidth, 100);
		txtArea.setWrapText(true);	
		txtArea.setTextFormatter(new TextFormatter<String>(write -> write.getControlNewText().
				length() <= zeichenMax ? write : null));

		//Counter für die Zeichenzahl. Maximal 500 sind erlaubt
		Label charCount = new Label(zeichenCounter + "/" + zeichenMax);
		charCount.setPadding(new Insets(5, 0, 0, 0));
		
		//Listener um die Länge von der Beschreibung zum kontrollieren
		txtArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				charCount.setText(arg2.length() + "/" + zeichenMax);
				//Alert falls die Beschreibung 500 Zeichen lang ist
				if(arg2.length() >= zeichenMax) {
					new Alert(AlertType.WARNING, "Maximal 500 Zeichen erlaubt").showAndWait();
				}
			}
		});

		VBox txtAreaBox = new VBox(txtArea, charCount);

		//GridPane fur alle Labels und TextFelder
		GridPane gp = new GridPane();
		gp.setVgap(10);
		gp.setHgap(10);
		gp.add(isbn, 0, 1);
		gp.add(titel, 0, 2);
		gp.add(autor, 0, 3);
		gp.add(thema, 0, 4);
		gp.add(jahr, 0, 5);
		gp.add(verlag, 0, 6);
		gp.add(preis, 0, 7);
		gp.add(titelblatt, 0, 8);		
		gp.add(beschreibung, 0, 9);

		gp.add(isbn_in, 1, 1);		
		gp.add(titel_in, 1, 2);
		gp.add(autor_in, 1, 3);
		gp.add(cbThemen, 1, 4);
		gp.add(neuesThema, 3, 4);
		gp.add(jahr_in, 1, 5);
		gp.add(verlag_in, 1, 6);
		gp.add(preis_in, 1, 7);
		gp.add(bild, 1, 8);		
		gp.add(txtAreaBox, 1, 9, 3, 9);

		this.getDialogPane().setContent(gp);
		ButtonType speichern = new ButtonType("Speichern", ButtonData.OK_DONE);
		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(speichern, abbrechen);	

		//Kontrol von Eingegebene Daten
		Button s = (Button) this.getDialogPane().lookupButton(speichern);
		s.addEventFilter(ActionEvent.ACTION, e ->{		
			//letter detector
			Pattern detectLetter = Pattern.compile("\\D");
			Pattern detectIsbn = Pattern.compile("^(?:978)");
			if(isbn_in.getText() == null || isbn_in.getText().length() == 0) {
				new Alert(AlertType.ERROR, "ISBN eingeben").showAndWait();
				e.consume();
				return;
			}
			if(detectLetter.matcher(isbn_in.getText()).find()){
				new Alert(AlertType.ERROR, "ISBN darf nur Zahlen enthalten").showAndWait();
				e.consume();
				return;				
			}
			if(isbn_in.getText().length() != 13) {
				new Alert(AlertType.ERROR, "ISBN muss 13 Ziffern lang sein").showAndWait();
				e.consume();
				return;
			}
			if(titel_in.getText() == null || titel_in.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Titel eingeben").showAndWait();
				e.consume();
				return;
			}
			if(autor_in.getText() == null || autor_in.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Autor eingeben").showAndWait();
				e.consume();
				return;
			}
			if(cbThemen.getSelectionModel().getSelectedItem() == null) {
				new Alert(AlertType.ERROR, "Thema auswählen").showAndWait();
				e.consume();
				return;
			}
			if(jahr_in.getText() == null || jahr_in.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Erscheinungsjahr eingeben").showAndWait();
				e.consume();
				return;
			}
			try {
				int j = Integer.parseInt(jahr_in.getText());
			}catch(NumberFormatException e1) {
				new Alert(AlertType.ERROR, "Jahr eingabe darf nur Zahlen erhalten").showAndWait();
				e.consume();
				return;
			}
			if(verlag_in.getText() == null || verlag_in.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Verlag eingeben").showAndWait();
				e.consume();
				return;
			}
			if(preis_in.getText() == null || preis_in.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Preis eingeben").showAndWait();
				e.consume();
				return;
			}
			try {
				double p = Double.parseDouble(preis_in.getText());
			}catch(NumberFormatException e1) {
				new Alert(AlertType.ERROR, "Preis eingabe darf nur Zahlen erhalten. Bei Gleitkommazahl '.' verwenden").showAndWait();
				e.consume();
				return;
			}
			if(txtArea.getText() == null) {
				txtArea.setText("");
			}
			if(txtArea.getText().length() > 500) {
				new Alert(AlertType.ERROR, "Beshcreibung darf maximal 500 zeichen erhalten").showAndWait();
				e.consume();
				return;
			}
		});

		this.setResultConverter(new Callback<ButtonType, ButtonType>(){
			@Override
			public ButtonType call(ButtonType arg0) {
				if(arg0 == speichern) {										
					buch.setTitel(titel_in.getText());
					buch.setAutor(autor_in.getText());					
					buch.setThema(cbThemen.getSelectionModel().getSelectedItem());
					buch.setJahr(Integer.parseInt(jahr_in.getText()));
					buch.setVerlag(verlag_in.getText());
					buch.setPreis(Double.parseDouble(preis_in.getText()));
					buch.setBeschreibung(txtArea.getText());
					buch.setTitelblatt(buch.getTitelblatt());
					try {
						if(buch.getIsbn() == null) {
							buch.setIsbn(isbn_in.getText());
							Datenbank.insertBuch(buch);
						}
						else {
							Datenbank.updateBuch(buch);
						}						
					}
					catch(SQLException e) {
						System.out.println(e);
						new Alert(AlertType.ERROR, e.toString()).showAndWait();
						return null;
					}
				}
				return arg0;
			}
		});
	}
	
	/**
	 * Lies ein Bild aus einem BildPath
	 * @param bildPath
	 * @return ein Bild falls das Path gültig ist. Ansonst returns null
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
	 * Kontrolliert ob das Thema neu ist
	 * @param neuesThema
	 * @return True falls das Theme neu ist, false falls schon vorgegeben
	 */
	private boolean isNeuesThema(String neuesThema) {
		for(Thema einThema : themen) {
			if(neuesThema.toUpperCase().equals(einThema.getBezeichnung().toUpperCase())) {
				return false;
			}
		}
		return true;
	}
}
