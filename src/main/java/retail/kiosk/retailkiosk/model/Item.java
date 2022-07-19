package retail.kiosk.retailkiosk.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static retail.kiosk.retailkiosk.config.Config.*;


public abstract class Item {
    private int id;
    private String name, description, imageSrc;
    private double netPrice, tax, weight, length, height, width;
    private int ageRangeMin, ageRangeMax;
    private boolean isMovable;
    private boolean isSpecialEdition;
    private String material;
    private int stock, expectedPurchase;

    public Item() throws ItemException {
        this(0, "Dummy", "Dummy description", "./", 0.1, 0, "", false, false, 0);
        setExpectedPurchase(0);
    }

    public Item(int id, String name, String description, String imageSrc, double netPrice, double tax, String material,
                boolean isMovable, boolean isSpecialEdition, int stock) throws ItemException {
        setId(id);
        setName(name);
        setDescription(description);
        setImageSrc(imageSrc);
        setNetPrice(netPrice);
        setTax(tax);
        setMaterial(material);
        setStock(stock);
        setExpectedPurchase(0);
        setMovable(isMovable);
        setSpecialEdition(isSpecialEdition);
        log.info(this.toStringVerbose());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) throws ItemException {
        // Check if it was not set previously.
        if (getId() != 0) {
            throw new ItemException("The id can only be set once. Its current value is: " + getId() + "!!");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws ItemException {
        if (name.length() > ITEM_NAME_LEN_MAX)
            throw new ItemException("The name cannot be longer than " + ITEM_NAME_LEN_MAX + " characters!!");
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public double getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(double netPrice) throws ItemException {
        if (netPrice < 0) {
            throw new ItemException("Net price cannot be a negative value!!");
        }
        this.netPrice = netPrice;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) throws ItemException {
        if (tax < 0) {
            throw new ItemException("Tax cannot be a negative value!!");
        }
        this.tax = tax;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) throws ItemException {
        this.material = material;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public boolean isSpecialEdition() {
        return isSpecialEdition;
    }

    public void setSpecialEdition(boolean isSpecialEdition) {
        this.isSpecialEdition = isSpecialEdition;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getAgeRangeMin() {
        return ageRangeMin;
    }

    public void setAgeRangeMin(int ageRangeMin) {
        this.ageRangeMin = ageRangeMin;
    }

    public int getAgeRangeMax() {
        return ageRangeMax;
    }

    public void setAgeRangeMax(int ageRangeMax) {
        this.ageRangeMax = ageRangeMax;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) throws ItemException {
        if (stock < 0)
            throw new ItemException("Stock cannot be a negative value!!");
        this.stock = stock;
    }

    public int getExpectedPurchase() {
        return expectedPurchase;
    }

    public void setExpectedPurchase(int expectedPurchase) {
        this.expectedPurchase = expectedPurchase;
    }

    public void decrease1Stock() throws ItemException {
        try {
            setStock(getStock() - 1);
        } catch (ItemException e) {
            throw new ItemException("This item is sold out!! You cannot decrease 1 unit.");
        }
    }

    public void decrease1ExpectedPurchase() {
        setExpectedPurchase(getExpectedPurchase() - 1);
    }

    public void increase1ExpectedPurchase() {
        setExpectedPurchase(getExpectedPurchase() + 1);
    }

    public boolean isSoldOut() {
        return getStock() == 0 || (getStock() - getExpectedPurchase()) <= 0;
    }

    public double getGrossPrice() {
        double tax = getTax();
        if (tax == 0)
            return getNetPrice();
        return (getNetPrice() * tax) + getNetPrice();
    }

    public String toStringVerbose() {
		final String ATTRIBUTE_PREFIX = "\n    > ";
 		StringBuilder text = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0.00");

		DecimalFormatSymbols custom = new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(custom);

		text.append(getName()).append(" ... ");
		text.append(df.format(getGrossPrice()));
		text.append(" " + CURRENCY_CODE);

        text.append(ATTRIBUTE_PREFIX + "Id: ").append(getId());
        text.append(ATTRIBUTE_PREFIX + "Name: ").append(getName());
        text.append(ATTRIBUTE_PREFIX + "Description: ").append(getDescription());
        text.append(ATTRIBUTE_PREFIX + "Class name: ").append(this.getClass().getName());
        text.append(ATTRIBUTE_PREFIX + "Image Src: ").append(getImageSrc());
        text.append(ATTRIBUTE_PREFIX + "Net Price: ").append(df.format(getNetPrice()));
        text.append(ATTRIBUTE_PREFIX + "Tax: ").append(df.format(getTax()));
        text.append(ATTRIBUTE_PREFIX + "Material: ").append(getMaterial());
        text.append(ATTRIBUTE_PREFIX + "Stock: ").append(getStock());
        text.append(ATTRIBUTE_PREFIX + "Is movable: ").append(isMovable());
        text.append(ATTRIBUTE_PREFIX + "Is special edition: ").append(isSpecialEdition());
		return text.toString();
    }

    public String toString() {
 		StringBuilder text = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0.00");

		DecimalFormatSymbols custom = new DecimalFormatSymbols();
		custom.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(custom);

		text.append(getName()).append(" ... ");
		text.append(df.format(getGrossPrice()));
		text.append(" " + CURRENCY_CODE);
		return text.toString();
    }
}