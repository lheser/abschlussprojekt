package klassen;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI_Generator extends Application {

	@Override
	public void start(Stage primaryStage) {
		
		
		VBox vb = new VBox();
		ComboBox<String> option = new ComboBox<>(FXCollections.observableArrayList("Bücher", "Besucher"));
		Button generate = new Button("Generate");
		
		vb.getChildren().add(option);
		vb.getChildren().add(generate);
		
		primaryStage.setScene(new Scene(vb));		
		primaryStage.setTitle("Generator");
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
}
