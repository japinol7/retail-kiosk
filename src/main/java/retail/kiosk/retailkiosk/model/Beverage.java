package retail.kiosk.retailkiosk.model;

import java.text.DecimalFormat;

import static retail.kiosk.retailkiosk.config.Config.CURRENCY_CODE;

public class Beverage extends Item {

    private int centiliters;
    private boolean isFizzy;
    private double alcoholVolume;

    public Beverage() throws ItemException {
        super();
        setCentiliters(33);
        setFizzy(true);
        setAlcoholVolume(0);
    }

    public Beverage(int id, String name, String description, int centiliters, double alcoholVolume,
                    boolean isFizzy, String imageSrc, double grossPrice, double tax,
                    String material, boolean isSpecialEdition, int stock) throws ItemException {
        super(id, name, description, imageSrc, grossPrice, tax, material, false, isSpecialEdition, stock);
        setCentiliters(centiliters);
        setAlcoholVolume(alcoholVolume);
        setFizzy(isFizzy);
    }

    public int getCentiliters() {
        return centiliters;
    }

    public void setCentiliters(int centiliters) throws ItemException {
        if (centiliters <= 0)
            throw new ItemException("Centiliters must be greater than zero!!");
        else
            this.centiliters = centiliters;
    }

    public double getAlcoholVolume() {
        return alcoholVolume;
    }

    public void setAlcoholVolume(double alcoholVolume) throws ItemException {
        if (alcoholVolume < 0)
            throw new ItemException("Alcohol by volume must be a positive number or zero!!");
        else
            this.alcoholVolume = alcoholVolume;
    }

    public boolean isFizzy() {
        return isFizzy;
    }

    public void setFizzy(boolean isFizzy) {
        this.isFizzy = isFizzy;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return getName() + " (" + getCentiliters() + " cl) ... " + df.format(getGrossPrice())
                + " " + CURRENCY_CODE;
    }
}
