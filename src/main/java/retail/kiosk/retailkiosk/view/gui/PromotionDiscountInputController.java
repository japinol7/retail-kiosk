package retail.kiosk.retailkiosk.view.gui;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import retail.kiosk.retailkiosk.model.PromotionDiscount;

import static retail.kiosk.retailkiosk.config.Config.TIME_TO_GO_MENU_BACK_LONG;
import static retail.kiosk.retailkiosk.config.Config.log;

public class PromotionDiscountInputController extends Controller {

    public TextField inputPromotionName;

    public PromotionDiscountInputController() {
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
        }, TIME_TO_GO_MENU_BACK_LONG);
    }

    @FXML
    private void handleBackButton() {
        app.setCurrentView(View.PROMOTION_DISCOUNT);
        app.goToScene("PromotionDiscountView.fxml");
    }

    @FXML
    private void handleApplyDiscountButton() {
        String inputPromotion = inputPromotionName.getText().toUpperCase();
        log.info(inputPromotion);
        if (app.getKiosk().getPromotionDiscountNames().contains(inputPromotion)) {
            log.info(inputPromotion);
            PromotionDiscount promotionDiscount = null;
            for (PromotionDiscount pDiscount : app.getKiosk().getPromotionDiscounts()) {
                if (Objects.equals(pDiscount.getName(), inputPromotion)) {
                    promotionDiscount = pDiscount;
                    log.info(promotionDiscount.toString());
                    app.getKiosk().applyPromotionDiscountToOrder(promotionDiscount);
                    break;
                }
            }
        } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "This is not a valid promotion discount.\n" +
                        "Try again or just make an order without a discount");
                alert.showAndWait();
                inputPromotionName.clear();
                return;
        }
        app.setCurrentView(View.PACK_SET);
        app.goToScene("QuotationView.fxml");
    }

}
