package retail.kiosk.retailkiosk.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import retail.kiosk.retailkiosk.model.*;
import retail.kiosk.retailkiosk.model.Inventory;

import static retail.kiosk.retailkiosk.config.Config.PROMOTION_FILE_NAME;
import static retail.kiosk.retailkiosk.config.Config.log;

public class Kiosk {

    private Order order;
    private final Inventory inventory;
    private final Set<String> promotionDiscountNames;
    ArrayList<PromotionDiscount> promotionDiscounts;

    public Kiosk() throws Exception {
        this("");
    }

    public Kiosk(String fileNameInventory) throws Exception {
        if (fileNameInventory.isEmpty()) {
            inventory = new Inventory();
        } else {
            inventory = new Inventory(fileNameInventory);
        }
        promotionDiscounts = new ArrayList<PromotionDiscount>();
        promotionDiscountNames = new HashSet<String>();
        loadPromotionDiscount();
    }

    public ArrayList<PromotionDiscount> getPromotionDiscounts() {
        return promotionDiscounts;
    }

    public Set<String> getPromotionDiscountNames() {
        return promotionDiscountNames;
    }

    public void createOrder() {
        // If this is not the first order and the last order has already been created but has not been committed,
        // decreases 1 expected purchase for all the items of the last order.
        if (order != null && !order.isConfirmed()) {
            for (Item item : getItemsOrder())
                item.decrease1ExpectedPurchase();
        }
        order = new Order();
    }

    public void setPromotionDiscountOptions(PromotionDiscountOptions promotionDiscountOptions) {
        order.setPromotionDiscountOptions(promotionDiscountOptions);
    }

    public void addItem2Order(Item item) throws OrderException, OrderPriceExceededException {
        order.addItem(item);
    }

    public void removeItemFromOrder(int index) throws OrderException {
        order.removeItem(index);
    }

    public List<Item> getItemsOrder() {
        return order.getItems();
    }

    public boolean isOrderEmpty() {
        return order.getItems().size() == 0;
    }

    public String showOrder() {
        StringBuilder text = new StringBuilder();
        text.append("\n*********This is your order***********\n");
        text.append(order);
        return text.toString();
    }

    public double getOrderTotalGrossCost() {
        return order.getTotalGrossCost();
    }

    public double getOrderTotalGrossCostWithDiscount() {
        return order.getTotalGrossCostWithDiscount();
    }

    public double getOrderDiscount() {
        return order.getDiscount();
    }

    public void commitOrder() throws Exception {
        order.commit();
        if (order.getPromotionDiscount() != null) {
            log.info("Remove promotion discount: " + order.getPromotionDiscount().getName());
            promotionDiscounts.remove(order.getPromotionDiscount());
            promotionDiscountNames.remove(order.getPromotionDiscount().getName());
        }
    }

    public void applyPromotionDiscountToOrder(PromotionDiscount promotionDiscount) {
        order.setPromotionDiscount(promotionDiscount);
    }

    public List<Item> getInventoryPerCategory(String category) {
        List<Item> res = new ArrayList<Item>();
        String categoryUCase = category.toUpperCase();

        for (Item item : inventory.getList()) {
            switch (categoryUCase) {
                case "ACTION_FIGURE":
                    if (item instanceof ActionFigure || item instanceof ActionFigureMovable
                            || item instanceof ActionFigureAccessory) {
                        res.add(item);
                    }
                    break;
                case "STICKER":
                    if (item instanceof Sticker) {
                        res.add(item);
                    }
                    break;
                case "BEVERAGE":
                    if (item instanceof Beverage) {
                        res.add(item);
                    }
                    break;
                case "CLOTHES_HAT":
                    if (item instanceof ClothesHat) {
                        res.add(item);
                    }
                    break;
                case "PACK_SET":
                    if (item instanceof PackSet) {
                        res.add(item);
                    }
                    break;
                case "ALL_BUT_PACK_SET":
                    if (item instanceof ActionFigure || item instanceof ActionFigureMovable
                            || item instanceof ActionFigureAccessory
                            || item instanceof Sticker
                            || item instanceof Beverage
                            || item instanceof ClothesHat) {
                        res.add(item);
                    }
                    break;
                default:
                    res.add(item);
                    break;
            }
        }
        res.sort(Comparator.comparing(Item::getName));
        return res;
    }

    private void loadPromotionDiscount() throws Exception {
        List<String> list = new ArrayList<String>();
        PromotionDiscount item;

        try (Stream<String> stream = Files.lines(Paths.get(PROMOTION_FILE_NAME))) {
            list = stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String itemList : list) {
            String[] elements = itemList.split("---");
            if (itemList.toUpperCase().equals("EOF")) {
                log.info("EOF line found. Stop importing promotion discounts.");
                break;
            }
            try {
                item = new PromotionDiscount(elements[0], Double.parseDouble(elements[1]));
                // If the item violates any constraint the promotion discount won't be created
                assert item != null;
                checkPromotionDiscountConstraints(item);
                // Add the item to the promotion discounts because no constraint has been violated
                promotionDiscounts.add(item);
                // Add the new id to itemIds, that represents all the distinct ids in the promotion discounts.
                promotionDiscountNames.add(item.getName());
            } catch (Exception e) {
                System.err.println("Error loading promotion discount: " + e.getMessage());
            }
        }
    }

    private void checkPromotionDiscountConstraints(PromotionDiscount item) throws Exception {
        // Check if the promotion name already exists.
        if (promotionDiscountNames.contains(item.getName())) {
            throw new Exception("There's already one promotion discount with this name "
                                + item.getName() + ". The promotion discount name must be unique!!");
        }
    }

}
