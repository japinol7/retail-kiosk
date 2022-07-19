package retail.kiosk.retailkiosk.view.cmd;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import retail.kiosk.retailkiosk.controller.Kiosk;
import retail.kiosk.retailkiosk.model.PromotionDiscount;
import retail.kiosk.retailkiosk.model.PromotionDiscountOptions;
import retail.kiosk.retailkiosk.model.Item;
import retail.kiosk.retailkiosk.model.PackSet;

import static retail.kiosk.retailkiosk.config.Config.log;

public class CmdApp {

    private final Scanner scanner;
    Kiosk kiosk;

    public CmdApp() throws Exception {
        super();
        kiosk = new Kiosk();
        scanner = new Scanner(System.in);
    }

    public void handleStart() {
        System.out.println("\n·············JAP Merch's Retail Kiosk ···············");
        System.out.println("\n\tPress a key and <ENTER> to start your order!!");
        System.out.println("\n·····················································");
        scanner.next();
        kiosk.createOrder();
    }

    public void handlePromotionDiscount() {

        do {
            System.out.println("\n············ Choose promotion discount ··············");
            System.out.println("\n\t1. NO promotion discount.");
            System.out.println("\n\t2. Promotion discount.");
            System.out.println("\n\n\t3. Go back.");
            System.out.println("···································");
            try {
                int choice = scanner.nextInt();
                if (choice == 1) {
                    kiosk.setPromotionDiscountOptions(PromotionDiscountOptions.NONE);
                    break;
                } else if (choice == 2) {
                    handlePromotionDiscountInput();
                    break;
                } else if (choice == 3) {
                    handleStart();
                } else {
                    System.err.println("[ERROR] Your option is incorrect!! Try again!!");
                }
            } catch (InputMismatchException e) {
                System.err.println("[ERROR] You must type a number!!");
                scanner.next();
            }
        } while (true);
    }

    public void handlePromotionDiscountInput() {
        boolean continueLoop = true;
        do {
            System.out.println("\n············ Enter promotion discount ··············");
            System.out.println("\n\n\t1. Go back.");
            System.out.println("···································");
            try {
                    kiosk.setPromotionDiscountOptions(PromotionDiscountOptions.DISCOUNT);
                    String inputPromotion = scanner.next();
                    if (Objects.equals(inputPromotion, "1")) {
                        handlePromotionDiscount();
                        continueLoop = false;
                        break;
                    }
                    inputPromotion = inputPromotion.toUpperCase();
                    System.out.println("Enter promotion code: ");
                    if (kiosk.getPromotionDiscountNames().contains(inputPromotion)) {
                        System.out.println("Promotion seledcted: " + inputPromotion);
                        PromotionDiscount promotionDiscount = null;
                        for (PromotionDiscount pDiscount : kiosk.getPromotionDiscounts()) {
                            if (Objects.equals(pDiscount.getName(), inputPromotion)) {
                                promotionDiscount = pDiscount;
                                log.info(promotionDiscount.toString());
                                kiosk.applyPromotionDiscountToOrder(promotionDiscount);
                                continueLoop = false;
                                break;
                            }
                        }
                    } else
                        System.out.println("This is not a valid promotion discount: " + inputPromotion + "\n" +
                                "Try again or just make an order without a discount");
            } catch (InputMismatchException e) {
                System.err.println("[ERROR] You must type a number!!");
                scanner.next();
            }
        } while (continueLoop);
    }

    public void handleOrder() {
        List<Item> list;

        do {
            System.out.println("\n······ Choose a category ······      " +
                               "········· Other options ·········");
            System.out.println("\n\t1. Action Figures" +
                              "\t\t\t\t\t5. Display your order");
            System.out.println("\n\t2. Stickers" +
                              "\t\t\t\t\t\t\t6. Remove an item from your order");
            System.out.println("\n\t3. Beverages" +
                               "\t\t\t\t\t\t7. Commit your order");
            System.out.println("\n\t4. Clothes - Hats" +
                               "\t\t\t\t\t8. Go back");
            System.out.println("\n\t9. PackSets");
            System.out.println("······································································");
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        list = kiosk.getInventoryPerCategory("ACTION_FIGURE");
                        handleAddItem2Order(list);
                        break;
                    case 2:
                        list = kiosk.getInventoryPerCategory("STICKER");
                        handleAddItem2Order(list);
                        break;
                    case 3:
                        list = kiosk.getInventoryPerCategory("BEVERAGE");
                        handleAddItem2Order(list);
                        break;
                    case 4:
                        list = kiosk.getInventoryPerCategory("CLOTHES_HAT");
                        handleAddItem2Order(list);
                        break;
                    case 9:
                        list = kiosk.getInventoryPerCategory("PACK_SET");
                        handleAddItem2Order(list);
                        break;
                    case 5:
                        if (!kiosk.isOrderEmpty())
                            System.out.println(kiosk.showOrder());
                        else
                            System.out.println("Your order is empty.");
                        break;
                    case 6:
                        if (!kiosk.isOrderEmpty())
                            handleRemoveItemFromOrder();
                        else
                            System.out.println("Cannot remove and item from an empty order.");
                        break;
                    case 7:
                        try {
                            kiosk.commitOrder();
                            System.out.println("Take the receipt and your order. Thanks!");
                            break;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 8:
                        handlePromotionDiscount();
                        break;
                    default:
                        System.err.println("[ERROR] Wrong option!! Try again!!");
                        break;
                }

            } catch (InputMismatchException e) {
                System.err.println("[ERROR] You must type a number!!");
                scanner.next();
            }
        } while (true);
    }

    private void handleAddItem2Order(List<Item> list) {
        if (!list.isEmpty()) {
            do {
                printItems(list);
                System.out.println("Choose the number of the item you want to add ('0' to go back):");
                try {
                    int choice = scanner.nextInt();
                    choice--;
                    if (choice >= 0 && choice < list.size()) {
                        try {
                            kiosk.addItem2Order(list.get(choice));
                            System.out.println("You have just added "
                                            + list.get(choice).getName() + " to your order!!");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    } else if (choice != -1) {
                        System.err.println("[ERROR] Your option is incorrect!! Try again!!");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.err.println("[ERROR] You must type a number!!");
                    scanner.next();
                }
            } while (true);

        } else {
            System.out.println("There are no items for this category!!");
        }
    }

    private void printItems(List<Item> list) {
        AtomicInteger index = new AtomicInteger();
        list.forEach((item) -> {
            System.out.print(index.incrementAndGet() + ". ");
            System.out.println(item);
            System.out.print(item instanceof PackSet ? ((PackSet) item).getItemsString("\t") + "\n" : "");
        });
    }

    private void handleRemoveItemFromOrder() {
        List<Item> list = kiosk.getItemsOrder();

        if (!list.isEmpty()) {
            do {
                printItems(list);
                System.out.println("Choose the number of the item you want to remove ('0' to go back):");
                try {
                    int choice = scanner.nextInt();
                    choice--;
                    if (choice >= 0 && choice < list.size()) {
                        try {
                            String itemName = list.get(choice).getName();
                            kiosk.removeItemFromOrder(choice);
                            System.out.println("You have just removed " + itemName + " from your order!!");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    } else if (choice != -1) {
                        System.err.println("[ERROR] Your option is incorrect!! Try again!!");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.err.println("[ERROR] You must type a number!!");
                    scanner.next();
                }
            } while (true);

        } else {
            System.out.println("There are no items for this category!!");
        }
    }

    public static void main(String[] args) throws Exception {
        CmdApp program = new CmdApp();

        do {
            program.handleStart();
            program.handlePromotionDiscount();
            program.handleOrder();
        } while (true);
    }
}