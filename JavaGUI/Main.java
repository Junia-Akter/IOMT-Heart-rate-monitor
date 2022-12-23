package hellofx;

import java.io.IOException;

import javax.imageio.IIOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

 /*
  * need to start the appplication. The start function loads the starting scene.
  */
public class Main extends Application{
    Parent root;
    @Override
    public void start(Stage primaryStage) throws IOException {
        root= FXMLLoader.load(getClass().getResource("Mainscene.fxml"));
        Scene scene = new Scene(root);
        String css= this.getClass().getResource("firstscene.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
    
        primaryStage.show();
    }
  
  
 
  
  
 
 
 public static void main(String[] args) {
        launch(args);
    
    }
}
