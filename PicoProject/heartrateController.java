package hellofx;

import java.io.IOException;
import javafx.concurrent.Service;
import java.util.concurrent.ExecutionException;

import org.atsign.client.api.AtClient;
import org.atsign.common.AtException;
import org.atsign.common.AtSign;
import org.atsign.common.KeyBuilders;
import org.atsign.common.KeyBuilders.SelfKeyBuilder;
import org.atsign.common.Keys.PublicKey;
import org.atsign.common.Keys.SelfKey;
import org.bouncycastle.pqc.crypto.falcon.FalconSigner;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import javafx.scene.control.Alert.AlertType;
import javafx.event.EventHandler;
import javafx.scene.control.Alert.AlertType;

public class heartrateController {
    private Stage stage;
    private Scene scene;
    private static AtClient atClient3 = null;
    private static AtClient atClient5 = null;
    private static int counter = 0;
    private static int calculatedHeartData;

    @FXML
    private Button backscene3;

    @FXML
    private Text heartrateval;

    @FXML
    private Label avgheartratelabel;

    @FXML
    private Text displayavg;

    @FXML
    private Button saveval;

    @FXML
    private Button startmeasure;

    @FXML
    private AnchorPane heartrateroot;

    @FXML
    private LineChart<Number, Number> chartdata;

    @FXML
    private Label lastSavedBpm;

    @FXML
    private Text showlastsaved;

    /*
     * retrieves the data stored in the picoboard and displays it as a bpm as well
     * as plots each session on a graph for a visual representation all using a
     * separate thread.
     * 
     */

    public class HeartrateService extends Service<String> {

        protected Task createTask() {

            return new Task<Void>() {
                @Override
                public Void call() throws Exception {
                    AtSign mysign = new AtSign(atsignsigninController.ATSIGN_STR_MACHINE);

                    try {
                        atClient5 = AtClient.withRemoteSecondary("root.atsign.org:64", mysign, true);

                    } catch (AtException e) {
                        System.err.println("Failed to connect to remote server " + e);
                        e.printStackTrace();
                    }

                    // using to plot on the graph
                    XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

                    for (int i = 0; i < 1; i++) {
                        if (isCancelled()) {
                            break;
                        }
                        try {

                            String command = "plookup:bypassCache:true:" + "heartRateSensor"
                                    + atsignsigninController.ATSIGN_STR_MACHINE;
                            Thread.sleep(2000);
                            String value = atClient5.executeCommand(command, false).data;
                            System.out.println("\n \n\n\n\n\n\n\n\n\n\n" + value + "\n \n\n\n\n\n\n\n\n\n\n");
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    heartrateval.setText(value); // displays the bpm and plots the heartrate on whenever
                                                                 // the mainthread is free
                                    counter++;
                                    calculatedHeartData = Integer.parseInt(value);

                                    series.getData()
                                            .add(new XYChart.Data<Number, Number>(counter, calculatedHeartData));
                                }
                            });
                        } catch (InterruptedException | AtException e) {
                            System.err.println("Failed to  get key ");
                            e.printStackTrace();
                        }

                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            chartdata.getData().add(series); // show the graph when done
                        }
                    });

                    return null;
                }
            };
        }
    }

    HeartrateService job = new HeartrateService();

    /*
     * this function saves your heartrate data to the same atsign that you used to
     * log into the heartrate dashboard.
     * It uses a selfkey meaning that the data is encrypted and accomplishes that
     * goal of privatly owning your medical data without a third party
     */

    private void saveheartratedata() {
        AtSign mysign = new AtSign(atsignsigninController.ATSIGN_STR);

        try {
            atClient3 = AtClient.withRemoteSecondary("root.atsign.org:64", mysign, true);

        } catch (AtException e) {
            System.err.println("Failed to connect to remote server " + e);
            e.printStackTrace();
        }
        SelfKey sk = new KeyBuilders.SelfKeyBuilder(mysign).key("lastsaved").build();
        String response = null;

        try {
            response = atClient3.put(sk, Integer.toString(calculatedHeartData)).get(); // puting the lastcalculated
                                                                                       // heartrate data to your atsign
            showlastsaved.setText(Integer.toString(calculatedHeartData));
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Failed to put key " + e);
            e.printStackTrace();
        }
        System.out.println(response);
    }

    /*
     * the event backsecondscene returns the user to the login screen by reloading
     * the resource files
     */
    @FXML
    void backsecondscene(ActionEvent event) throws IOException {
        Parent currscene = FXMLLoader.load(getClass().getResource("atsignsignin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(currscene);
        String css = this.getClass().getResource("secondscene.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();

    }

    /*
     * the event saveheartrate executes the function saveheartratedata as well as
     * display a alert notifying the user
     * that their last bpm was saved under their own atsign.
     */
    @FXML
    void saveheartrate(ActionEvent event) {
        saveheartratedata();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("BPM successfully saved to:" + atsignsigninController.ATSIGN_STR);
        alert.show();
    }
    /*
     * event that runs the thread to retrieve the data from the heartrate monitor
     * that the user registered in the log in screen
     */

    @FXML
    void startheartrate(ActionEvent event) throws ExecutionException {
        if (job.isRunning()) {
            job.cancel();
            heartrateval.setText("0"); // clears last calculated bpm and clear the graph
            chartdata.getData().clear();
            counter = 0;

        } else {
            job.restart();
        }

    }
}
