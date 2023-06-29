package klassen;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GUI_Besucher extends Application {	
	
	private enum Buchsuche{
		ISBN, AUTOR, TITEL, THEMA;		
	}
	private ObservableList<Thema> obsListThemen = FXCollections.observableArrayList();
	private ObservableList<BuchFX> obsListBuecher = FXCollections.observableArrayList();
	private ComboBox<Thema> cbThemen = new ComboBox<>(obsListThemen);	
	private TextField buchSuche_input = new TextField("Suchen");
	
	@SuppressWarnings("exports")
	@Override
	public void start(Stage primaryStage) {

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

		cbThemen.setOnMouseClicked( e-> {
			rb_isbn.setSelected(false);
			rb_titel.setSelected(false);
			rb_autor.setSelected(false);
			if(buchSuche_input.getText().length() > 0)
				buchSuche_input.clear();
		});	

		buchSuche_input.setPrefWidth(250);
		buchSuche_input.setOnMouseClicked(e ->{
			if(buchSuche_input.getText().equals("Suchen"))
				buchSuche_input.clear();
		});
//TODO comentar
		buchSuche_input.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if(arg0.getCode().equals(KeyCode.ENTER)) {
					if(buchSuche_input.getText().length() == 0 && cbThemen.getSelectionModel().getSelectedItem().
							getThema_ID() == 0) {
						buecherSuchen();
						return;
					} else {
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
						if(rb_isbn.isSelected()) {
							buecherSuchen(Buchsuche.ISBN);	
							return;
						}
						new Alert(AlertType.ERROR, "ISBN, TITEL, AUTOR oder THEMA auswählen").showAndWait();
						return;
					}
				}
			}
		});

		Button suchen = new Button("suchen");
		suchen.setOnAction(e ->{		
			//Falls suche TextFeld input is leer, werden alle Bücher gesucht und angezeigt
			if(buchSuche_input.getLength() == 0 && cbThemen.getSelectionModel().getSelectedItem().getThema_ID() == 0) {
				buecherSuchen();
				e.consume();
				return;
			}
			//Ansonnst wird nach der Auswahl die Sucher dürchgefürt
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
				//Meldung falls der Besucher nichts ausgewählt hat
				new Alert(AlertType.ERROR, "ISBN, TITEL, AUTOR oder THEMA auswählen").showAndWait();
				e.consume();
				return;
			}
		});

		//Behälter für die Sucheelemente
		HBox maSucheBuecher = new HBox(13, hb_radioButtonsBuch, cbThemen, buchSuche_input, suchen);
		maSucheBuecher.setPadding(new Insets(15, 0, 3, 0));

		//tableColumns für die Darstellung von BucherFX Objekte
		//TableColumns und TableView
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

		//tableview für die Ergebnisse der Suche
		TableView<BuchFX> tvBuecher = new TableView<>(obsListBuecher);
		tvBuecher.getColumns().addAll(colIsbn, colTitel, colAutor, colThema, colJahr,colEntlehnt);

		//Width Property für TableColumns
		colIsbn.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()).subtract(15));
		colTitel.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()).add(90));
		colAutor.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()));
		colJahr.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()).subtract(55));
		colThema.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()));
		colEntlehnt.prefWidthProperty().bind(tvBuecher.widthProperty().divide(tvBuecher.getColumns().size()).subtract(20));

		//Style von TableColumns
		colIsbn.setStyle("-fx-alignment: CENTER;");
		colTitel.setStyle("-fx-alignment: CENTER;");
		colAutor.setStyle("-fx-alignment: CENTER;");
		colJahr.setStyle("-fx-alignment: CENTER;");
		colThema.setStyle("-fx-alignment: CENTER;");
		colEntlehnt.setStyle("-fx-alignment: CENTER;");	

		//Beim Doppelklickt wird ein Detailsfenster des Buches angezeigt
		tvBuecher.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouse) {
				if(mouse.getButton().equals(MouseButton.PRIMARY) && mouse.getClickCount() == 2) {
					if(tvBuecher.getSelectionModel().getSelectedItem() != null) {
						VisitorBuchdetails b = new VisitorBuchdetails(tvBuecher.getSelectionModel().getSelectedItem().getModellBuch());
						b.showAndWait();
					}
				}

			}
		});

		AnchorPane anchorBuch = new AnchorPane();

		AnchorPane.setTopAnchor(maSucheBuecher, 10d);
		AnchorPane.setLeftAnchor(maSucheBuecher, 10d);		
		AnchorPane.setRightAnchor(maSucheBuecher, 10d);
		AnchorPane.setBottomAnchor(maSucheBuecher, 10d);

		AnchorPane.setTopAnchor(tvBuecher, 60d);
		AnchorPane.setLeftAnchor(tvBuecher, 10d);
		AnchorPane.setRightAnchor(tvBuecher, 10d);
		AnchorPane.setBottomAnchor(tvBuecher, 10d);

		anchorBuch.getChildren().addAll(maSucheBuecher, tvBuecher);

		themenSuchen();

		primaryStage.setScene(new Scene(anchorBuch));		
		primaryStage.setTitle("Besucher");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
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

	//Bücher nach ISBN, Titel oder Autor suchen
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
}
