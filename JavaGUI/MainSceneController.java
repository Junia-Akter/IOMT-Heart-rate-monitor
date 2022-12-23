package hellofx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.Desktop;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.net.URI;
import java.net.URISyntaxException;

/*
 * Starting screen of the application. Allows the user to continue by entering the applicable atsigns 
 * as well as have an option to signup for one
 * 
 */
public class MainSceneController {
    private Stage stage;
    private Scene scene;
    private Parent currscene;
    @FXML
    private AnchorPane root;

    @FXML
    private Button signin;

    @FXML
    private Button signup;
   
    @FXML
    private Label welcomeheart;

    @FXML
    /*
     * event to be taken to the login in screen.Loads resources
     */
    void siginscene(ActionEvent event) throws IOException{
        Parent currscene=FXMLLoader.load(getClass().getResource("atsignsignin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(currscene);
        String css= this.getClass().getResource("secondscene.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();

    }
    /*
     * event to be taken to the atsign web page to signup for a atsign.
     */
    @FXML
    void opensignuplink(ActionEvent event) throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://my.atsign.com/choose-atsign/032ad787b11f1fc6e8942cb8509445805eaed3195889bc47d28c5f176c569401"));
        

    }

}