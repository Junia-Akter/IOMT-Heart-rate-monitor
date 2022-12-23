package hellofx;

import org.atsign.client.api.AtClient;
import org.atsign.common.AtException;
import org.atsign.common.AtSign;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import javafx.scene.control.Alert.AlertType;
import javafx.event.EventHandler;

/*
 * This is the signin scene where the user enters their registered atsign and the heartrate sensor's atsign they wish to connect with and share data.
 */

public class atsignsigninController {
    private Scene scene;
    private Stage stage;
    public static String ATSIGN_STR;
    public static String ATSIGN_STR_MACHINE;
    Alert a = new Alert(AlertType.NONE);

    @FXML
    private Button backmainscene;

    @FXML
    private AnchorPane scene2;

    @FXML
    private TextField useratsigntextview;

    @FXML
    Button loginconfirm;

    @FXML
    private TextField machineatsigntextview;

    EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            // set alert type

            a.setAlertType(AlertType.ERROR);
            a.setContentText(
                    "Please check the user atsign as well as the machine's atsign. Go back to home and try again.");

            // show the dialog
            a.show();
        }
    };

    /* this function is the event for the backbutton to go back to the
     homescreen. It ultimately loads the resource of the css file for the mainscene as the result of the action.
      */
    @FXML
    void gobacktosignin(ActionEvent event) throws IOException {
        Parent currscene = FXMLLoader.load(getClass().getResource("Mainscene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(currscene);
        String css = this.getClass().getResource("firstscene.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();

    }
/*this function is the event for a successful onboarding of the user's atsign and the heartrate machine's atsign. Once the user successfully enters their information they should be taken to their own heartrate dashboard. If not an eror message will be displayed prompting them to reenter the correct information.
*/
    @FXML
    void heartratedashboard(ActionEvent event) throws IOException {
        String ROOT_URL = "root.atsign.org:64";
        String tempsign = useratsigntextview.getText();
        String tempmachinesign = machineatsigntextview.getText();
        System.out.println(tempmachinesign);
        AtSign atSign = new AtSign(tempsign); // instanace of our atsign from our keys folder
        AtSign atSign_machine = new AtSign(tempmachinesign);
        AtClient atClient_user = null;
        AtClient atClient_machine = null;

        try {
            atClient_user = AtClient.withRemoteSecondary(ROOT_URL, atSign); // creating our atclient
            atClient_machine = AtClient.withRemoteSecondary(ROOT_URL, atSign_machine);
            System.out.println("HELLOOOOO");
            ATSIGN_STR = tempsign;
            ATSIGN_STR_MACHINE = tempmachinesign;

            Parent currscene = FXMLLoader.load(getClass().getResource("heartratescene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(currscene);
            String css = this.getClass().getResource("thirdscene.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();

        } catch (AtException e) {
            loginconfirm.setOnAction(event1);

        }

    }

}