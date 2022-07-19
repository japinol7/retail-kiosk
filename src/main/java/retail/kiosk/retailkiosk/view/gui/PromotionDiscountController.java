package retail.kiosk.retailkiosk.view.gui;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import retail.kiosk.retailkiosk.model.PromotionDiscountOptions;
import static retail.kiosk.retailkiosk.config.Config.TIME_TO_GO_MENU_BACK;

public class PromotionDiscountController extends Controller {

    public PromotionDiscountController() {
        super();
    }

    @FXML
    private void initialize() {
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
    private void handlePromotionDiscountSelection(MouseEvent e) {
        VBox option = (VBox) e.getSource();
        boolean isPromotionDiscount = false;

        if (option.getId().compareTo("promotionDiscount") == 0) {
            app.getKiosk().setPromotionDiscountOptions(PromotionDiscountOptions.DISCOUNT);
            isPromotionDiscount = true;
        } else {
            app.getKiosk().setPromotionDiscountOptions(PromotionDiscountOptions.NONE);
        }

        app.getTimer().cancel();
        app.getTimer().purge();

        if (isPromotionDiscount) {
            app.setCurrentView(View.PROMOTION_DISCOUNT_INPUT);
            app.goToScene("PromotionDiscountInputView.fxml");
        } else {
            app.setCurrentView(View.PACK_SET);
            app.goToScene("QuotationView.fxml");
        }
    }

    @FXML
    private void handleBackButton() {
        app.setCurrentView(View.PROMOTION_DISCOUNT);
        app.goToScene("StartView.fxml");
    }
}