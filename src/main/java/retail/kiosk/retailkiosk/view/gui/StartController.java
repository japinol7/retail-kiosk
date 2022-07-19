package retail.kiosk.retailkiosk.view.gui;

import javafx.fxml.FXML;

public class StartController extends Controller {

    public StartController() {
        super();
    }

    @FXML
    private void handleStartButton() {
        app.getKiosk().createOrder();
        app.goToScene("PromotionDiscountView.fxml");
    }
}
