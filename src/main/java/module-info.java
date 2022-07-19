module retail.kiosk.retailkiosk.view.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens retail.kiosk.retailkiosk.view.gui to javafx.fxml;
    exports retail.kiosk.retailkiosk.view.gui;
}