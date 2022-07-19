package retail.kiosk.retailkiosk.view.gui;

public abstract class Controller {

    protected static App app;

    public static void setApp(App app) {
        Controller.app = app;
    }
}
