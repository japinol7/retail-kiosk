package retail.kiosk.retailkiosk.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Timer;

import retail.kiosk.retailkiosk.controller.Kiosk;
import static retail.kiosk.retailkiosk.config.Config.log;


public class App extends Application {

    private Stage primaryStage;
    private Kiosk kiosk;
    private View currentView;
    private Timer timer = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JAP RetailKiosk. A self-ordering kiosk");
        this.primaryStage.setResizable(false);
        kiosk = new Kiosk();
        setCurrentView(View.START);
        goToScene("StartView.fxml");
    }

    @Override
    public void stop() throws Exception {
        timer.cancel();
        timer.purge();
        super.stop();
    }

    public void timerStop() throws Exception {
        timer.cancel();
        timer.purge();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Kiosk getKiosk() {
        return kiosk;
    }

    public void setKiosk(Kiosk kiosk) {
        this.kiosk = kiosk;
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void goToScene(String layout) {
        try {
            log.info("Load root layout from fxml file: " + layout);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource(layout));
            Region rootLayout = loader.load();

            // Give the controller access to the main app
            Controller.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}