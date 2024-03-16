package retail.kiosk.retailkiosk.config;

import java.util.logging.Logger;

import retail.kiosk.retailkiosk.model.ActionFigureMovable;

public abstract class Config {
    public static Logger log = Logger.getLogger(ActionFigureMovable.class.getName());
    public static final int ITEM_NAME_LEN_MAX = 50;
    public static final int ORDER_GROSS_PRICE_MAX = 5000;
    public static final String CURRENCY_CODE = "EUR";
    public static final int TIME_TO_GO_MENU_BACK = 300_000;
    public static final int TIME_TO_GO_MENU_BACK_LONG = 400_000;
    public static final int MAX_RANDOM_ITEM_TRIES = 1500;
    public static final String APP_NAME = "retail-kiosk";
    public static final String VERSION = "1.0.2";
    public static final String LOG_START_APP_MSG = "Start app " + APP_NAME + " version: " + VERSION;
    public static final String LOG_END_APP_MSG = "End app " + APP_NAME + " version: " + VERSION;
    public static final String ORDER_RECEIPT_FILE_PATH = "output";
    public static final String INVENTORY_FILE_NAME = "dataset/inventory.txt";
    public static final String PROMOTION_FILE_NAME = "dataset/promotion_discount.txt";
    public static final String ORDER_RECEIPT_FILE_NAME = "order_receipt.txt";
    public static final String WARNING_ORDER_PRICE_EXCEEDED_MSG = """

                                Advice: Just make two orders.\s
                                Do not leave without the merch. you need to be happy""";
}
