package retail.kiosk.retailkiosk.model;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static retail.kiosk.retailkiosk.config.Config.*;


public class PromotionDiscount {
    private int id = 0;
    private String name;
    private double discount;

    public PromotionDiscount(String name, double discount) {
        this.id += 1;
        this.name = name;
        this.discount = discount;
        log.info(this.toStringVerbose());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String toStringVerbose() {
		final String ATTRIBUTE_PREFIX = "\n    > ";
 		StringBuilder text = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0.00");

		DecimalFormatSymbols custom = new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(custom);

		text.append(getName()).append(" ... ");
		text.append(df.format(getDiscount()));
		text.append(" %");

        text.append(ATTRIBUTE_PREFIX + "Id: ").append(getId());
        text.append(ATTRIBUTE_PREFIX + "Name: ").append(getName());
        text.append(ATTRIBUTE_PREFIX + "Discount: ").append(getDiscount()).append(" %");
		return text.toString();
    }

    public String toString() {
 		StringBuilder text = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0.00");

		DecimalFormatSymbols custom = new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(custom);

		text.append(getName()).append(" ... ");
		text.append(df.format(getDiscount())).append(" %");
		return text.toString();
    }
}
