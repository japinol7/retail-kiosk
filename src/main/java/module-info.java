module retail.kiosk.retailkiosk.view.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires org.junit.jupiter.api;

    opens retail.kiosk.retailkiosk.view.gui to javafx.fxml;

    exports retail.kiosk.retailkiosk.view.gui;
    exports retail.kiosk.retailkiosk.model;
}
