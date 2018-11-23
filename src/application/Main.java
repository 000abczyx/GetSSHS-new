package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("GameLayout.fxml"));
			
			BorderPane root = (BorderPane)loader.load();
			Scene scene = new Scene(root,500,700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			
			primaryStage.setTitle("GetSSHS");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			Actuator controller = loader.getController();
			controller.setScene(scene);
			GameManager gameManager = new GameManager(controller);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
