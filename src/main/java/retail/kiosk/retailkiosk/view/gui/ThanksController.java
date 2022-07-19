package retail.kiosk.retailkiosk.view.gui;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;

import static retail.kiosk.retailkiosk.config.Config.TIME_TO_GO_MENU_BACK;

public class ThanksController extends Controller {

    public ThanksController() {
        super();
    }

    @FXML
    private void initialize() throws Exception {
        app.timerStop();
        app.setTimer(new Timer());
        app.getTimer().schedule(new TimerTask() {

            @Override
            public void run() {
                Platform.runLater(() -> {
                    app.setCurrentView(View.START);
                    app.goToScene("StartView.fxml");
                });

            }
        }, TIME_TO_GO_MENU_BACK);
    }

    @FXML
    private void handleBackButton() throws Exception {
        app.timerStop();
        app.setCurrentView(View.START);
        app.goToScene("StartView.fxml");
    }

}