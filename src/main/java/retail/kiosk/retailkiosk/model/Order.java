package retail.kiosk.retailkiosk.model;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.io.IOException;

import static retail.kiosk.retailkiosk.config.Config.*;


public class Order {
    private static int id = 0;
    private List<Item> items;
    private boolean isConfirmed;
    private PromotionDiscountOptions promotionDiscountOptions;
    private PromotionDiscount promotionDiscount;

    public Order() {
        promotionDiscountOptions = PromotionDiscountOptions.NONE;
        items = new LinkedList<Item>();
        setConfirmed(false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        Order.id = id;
    }

    public PromotionDiscountOptions getPromotionDiscountOptions() {
        return promotionDiscountOptions;
    }

    public void setPromotionDiscountOptions(PromotionDiscountOptions promotionDiscountOptions) {
        this.promotionDiscountOptions = promotionDiscountOptions;
    }

    public PromotionDiscount getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(PromotionDiscount promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    private void setConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    private void increase1Id() {
        setId(getId() + 1);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) throws OrderException, OrderPriceExceededException {
        if (isConfirmed())
            throw new OrderException("The order has already been committed!! You cannot add a new item!!");
        if (item.isSoldOut())
            throw new OrderException("The item is sold out!! You cannot add it to your order!!");
        if (getTotalGrossCost() + item.getGrossPrice() > ORDER_GROSS_PRICE_MAX)
            throw new OrderPriceExceededException("Sorry!! Your order cannot exceed " + ORDER_GROSS_PRICE_MAX
                    + " " + CURRENCY_CODE + " !!");

        items.add(item);
        item.increase1ExpectedPurchase();
    }

    public void removeItem(int index) throws OrderException {
        if (isConfirmed())
            throw new OrderException("The order has already been committed!! You cannot remove an item!!");

        items.get(index).decrease1ExpectedPurchase();
        items.remove(index);
    }

    public double getTotalGrossCost() {
        double total = 0;

        for (Item item : items) {
            total += item.getGrossPrice();
        }
        return total;
    }

    public double getTotalNetCost() {
        double total = 0;

        for (Item item : items) {
            total += item.getNetPrice();
        }
        return total;
    }

    public double getTotalTaxesCost() {
        double total = 0;

        for (Item item : items) {
            total += item.getGrossPrice() - item.getNetPrice();
        }
        return total;
    }

    public double getTotalDiscountCost() {
        return getTotalGrossCost() * getDiscount() / 100;
    }

    public double getDiscount() {
        if (getPromotionDiscount() != null)
            return getPromotionDiscount().getDiscount();
        return 0.0;
    }
    public double getTotalGrossCostWithDiscount() {
        if (getPromotionDiscount() != null)
            return getTotalGrossCost() - getTotalDiscountCost();
        return getTotalGrossCost();
    }

    @Override
    public String toString() {
		final String DIVISOR_LINE = "\n_____________________________________________\n";
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuilder text = new StringBuilder();

		if (getPromotionDiscount() != null)
            text.append("Discount promotion code: ").append(getPromotionDiscount().getName());
        else
            text.append("No discount promotion to apply");
        text.append("\n_____________________________________________");
        for (Item item : items) {
            text.append("\n").append(item);
        }
        text.append(DIVISOR_LINE);
		text.append("TOTAL: ");
		text.append(String.format("%7s", df.format(getTotalGrossCostWithDiscount()))).append(" " + CURRENCY_CODE + "\n");
		text.append("Taxes: ");
		text.append(String.format("%7s", df.format(getTotalTaxesCost()))).append(" " + CURRENCY_CODE);

		if (getPromotionDiscount() != null) {
            text.append("\nDisc.: ");
            text.append(String.format("%7s", df.format(getTotalDiscountCost()))).append(" " + CURRENCY_CODE);
            text.append("   ").append(getDiscount()).append(" %");
        }

		text.append(DIVISOR_LINE);
        return text.toString();
    }

    public void commit() throws OrderException, ItemException {
        if (isConfirmed()) {
            throw new OrderException("The order has already been committed!!");
        } else if (getItems().size() == 0) {
            throw new OrderException("Your order is empty. You cannot commit it!!");
        }
        setConfirmed(true);
        increase1Id();

        for (Item item : items) {
            item.decrease1ExpectedPurchase();
            item.decrease1Stock();
        }
        printReceipt();
    }

    private void printReceipt() throws OrderException {
        // When opening the file, we take advantage of try(...) so it closes the resource for us when it is needed
        try (var out = new PrintWriter(Paths.get(ORDER_RECEIPT_FILE_PATH, ORDER_RECEIPT_FILE_NAME).toString()
                    , StandardCharsets.UTF_8);) {
            out.println("*** JAP Merch's Retail Kiosk ***\n");
            out.println("|| No. Order: " + id + " ||\n");
            out.println(this);
        } catch (IOException e) {
            throw new OrderException("Cannot print the order's receipt!! Error: " + e.getMessage());
        }
    }
}
