package klassen;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class VisitorBuchdetails extends Dialog <ButtonType> {
	
	private String genericPath = ".\\resources\\titelblatt_unknown.jpg";
	
	public VisitorBuchdetails(Buch buch) {
		this.setTitle("Details des Buches");
		
		//Linke Spalte mit Items
		ImageView iv = new ImageView();	
		Label status = new Label("Status");
		Label isbn = new Label("ISBN");
		Label titel = new Label("Titel");
		Label autor = new Label("Autor");		
		Label thema = new Label("Thema");
		Label jahr = new Label("Jahr");
		Label verlag = new Label("Verlag");
		Label titelblatt = new Label("Titelblatt");
		Label beschreibung = new Label("Beschreibung");
		
		status.setStyle("-fx-font-weight: bold");
		isbn.setStyle("-fx-font-weight: bold");
		titel.setStyle("-fx-font-weight: bold");
		autor.setStyle("-fx-font-weight: bold");
		thema.setStyle("-fx-font-weight: bold");
		jahr.setStyle("-fx-font-weight: bold");
		verlag.setStyle("-fx-font-weight: bold");
		titelblatt.setStyle("-fx-font-weight: bold");
		beschreibung.setStyle("-fx-font-weight: bold");
		
		//Rechte Spalte Buchdaten
		Label status_in = new Label(buch.isEntlehnt()? "ENTLEHNT" : "VERFÜGBAR");
		Label isbn_in = new Label(buch.getIsbn());
		Label titel_in = new Label(buch.getTitel());
		Label autor_in = new Label(buch.getAutor());		
		Label thema_in = new Label(buch.getThema().getBezeichnung());
		Label jahr_in = new Label(Integer.toString(buch.getJahr()));		
		Label verlag_in = new Label(buch.getVerlag());
		Label titelblatt_in = new Label();
		
		//Bild wird gesucht. Falls nicht gefunden generic Bildpath wird verwendet
		if(readBild(buch.getTitelblatt()) == null)
			buch.setTitelblatt(genericPath);		
		iv.setImage(readBild(buch.getTitelblatt()));
		iv.setPreserveRatio(true);		
		iv.setFitHeight(250);
		titelblatt_in.setGraphic(iv);
		Label beschreibung_in = new Label(buch.getBeschreibung());
		beschreibung_in.setPadding(new Insets(15, 0, 0, 0));
		beschreibung_in.setPrefWidth(350);
		beschreibung_in.setStyle("-fx-text-alignment: justify");
		beschreibung_in.setWrapText(true);
		
		//GridPane für alle Elemente
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(15));
		gp.setVgap(10);
		gp.setHgap(25);
		gp.add(status, 0, 0);
		gp.add(isbn, 0, 1);
		gp.add(titel, 0, 2);
		gp.add(autor, 0, 3);
		gp.add(thema, 0, 4);
		gp.add(jahr, 0, 5);
		gp.add(verlag, 0, 6);
		gp.add(titelblatt, 0, 7);		
		gp.add(beschreibung, 0, 8);
		
		gp.add(status_in, 1, 0);
		gp.add(isbn_in, 1, 1);		
		gp.add(titel_in, 1, 2);
		gp.add(autor_in, 1, 3);
		gp.add(thema_in, 1, 4);
		gp.add(jahr_in, 1, 5);
		gp.add(verlag_in, 1, 6);
		gp.add(titelblatt_in, 1, 7);		
		gp.add(beschreibung_in, 1, 8);	
		
		this.getDialogPane().setContent(gp);
		ButtonType ok = new ButtonType("Schließen", ButtonData.OK_DONE);		
		this.getDialogPane().getButtonTypes().addAll(ok);	
	}
	
	/**
	 * Suche dem Buchtitelblatt
	 * @param bildPath
	 * @return ein Image falls der Path gültig ist. Ansonst null
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
						
			}
		}
		return null;
	}
}
