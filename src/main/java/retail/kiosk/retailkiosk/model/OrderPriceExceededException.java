package retail.kiosk.retailkiosk.model;

public class OrderPriceExceededException extends Exception {

    public OrderPriceExceededException() {
        super();
    }

    public OrderPriceExceededException(String msg) {
        super(msg);
    }
}
