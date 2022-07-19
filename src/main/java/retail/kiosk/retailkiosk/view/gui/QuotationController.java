package retail.kiosk.retailkiosk.view.gui;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import retail.kiosk.retailkiosk.model.Item;
import retail.kiosk.retailkiosk.model.OrderPriceExceededException;
import retail.kiosk.retailkiosk.model.PackSet;
import retail.kiosk.retailkiosk.model.OrderException;

import static retail.kiosk.retailkiosk.config.Config.*;

public class QuotationController extends Controller {

    static final double SPLIT_POSITION = 0.536096256684492;

    @FXML
    private SplitPane splitPane;

    @FXML
    private Pane packSetTable;

    @FXML
    private ListView<Item> listOrder;

    @FXML
    private Label labelTotal;
    @FXML
    private Label labelDiscount;
    @FXML
    private Label labelDiscountLabel;

    public QuotationController() {
        super();
    }

    @FXML
    private void initialize() throws Exception {
        app.timerStop();
        splitPane.setDividerPositions(SPLIT_POSITION);
        displayOrder();
        goToScreen();
    }

    private void goToScreen() {
        switch (Controller.app.getCurrentView()) {
            case ACTION_FIGURES:
                displayItemsOptionsByCategory(app.getKiosk().getInventoryPerCategory("ACTION_FIGURE"));
                break;
            case STICKERS:
                displayItemsOptionsByCategory(app.getKiosk().getInventoryPerCategory("STICKER"));
                break;
            case BEVERAGES:
                displayItemsOptionsByCategory(app.getKiosk().getInventoryPerCategory("BEVERAGE"));
                break;
            case CLOTHES_HATS:
                displayItemsOptionsByCategory(app.getKiosk().getInventoryPerCategory("CLOTHES_HAT"));
                break;
            case PACK_SETS:
                displayItemsOptionsByCategory(app.getKiosk().getInventoryPerCategory("PACK_SET"));
                break;
            case PROMOTION_DISCOUNT:
                break;
            case PACK_SET:
                try {
                    Pane n = (Pane) FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CategoriesView.fxml")));
                    for (Node component : n.getChildren()) {
                        component.setOnMouseReleased(
                                event -> {
                                    event.consume();
                                    VBox option = (VBox) event.getSource();
                                    switch (option.getId()) {
                                        case "action_figures" -> app.setCurrentView(View.ACTION_FIGURES);
                                        case "beverages" -> app.setCurrentView(View.BEVERAGES);
                                        case "stickers" -> app.setCurrentView(View.STICKERS);
                                        case "clothes_hats" -> app.setCurrentView(View.CLOTHES_HATS);
                                        case "pack_sets" -> app.setCurrentView(View.PACK_SETS);
                                    }
                                    goToScreen();
                                }

                        );
                    }
                    packSetTable.getChildren().clear();
                    packSetTable.getChildren().addAll(n);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void displayItemsOptionsByCategory(List<Item> currentList) {
        ObservableList<Node> nodeList = FXCollections.observableArrayList();
        splitPane.setDividerPositions(SPLIT_POSITION);

        for (Item itemInventoryList : currentList) {
            try {
                Pane n = (Pane) FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ItemOption.fxml")));
                Image iv = new Image("file:" + itemInventoryList.getImageSrc(), 300, 300, false, false);
                ((ImageView) n.getChildren().get(0)).setImage(iv);
                ((Label) ((HBox) n.getChildren().get(1)).getChildren().get(0)).setText(itemInventoryList.getName());

                // Add a tooltip with the toString text for each item of the category.
                // If it is a pack set also display its items
                Tooltip tooltip = null;
                if (itemInventoryList instanceof PackSet) {
                    tooltip = new Tooltip(((PackSet) itemInventoryList).getItemsWithHeaderString());
                } else {
                    tooltip = new Tooltip(itemInventoryList.toString());
                }
                tooltip.setShowDelay(null);
                tooltip.setFont(new Font(16.0));
                ((Label) ((HBox) n.getChildren().get(1)).getChildren().get(0)).setTooltip(tooltip);

                if (itemInventoryList.isSoldOut()) {
                    ((Label) ((HBox) n.getChildren().get(1)).getChildren().get(1)).setText("Sold out");
                    n.setStyle("-fx-background-color:#ff0000; -fx-opacity:0.5; -fx-border-color:#ff0000");
                } else {
                    DecimalFormat df = new DecimalFormat("0.00");
                    ((Label) ((HBox) n.getChildren().get(1)).getChildren().get(1)).setText(df.format(itemInventoryList.getGrossPrice()) + " ·");
                    n.setOnMouseReleased(event -> {
                        try {
                            app.getKiosk().addItem2Order(itemInventoryList);
                            displayOrder();
                            goToScreen();
                        } catch (OrderException e) {
                            e.printStackTrace();
                        } catch (OrderPriceExceededException e) {
                            String msg = "Sorry!! Your order cannot exceed " + ORDER_GROSS_PRICE_MAX + " " + CURRENCY_CODE + " !!";
                            log.warning(msg);
                            msg += WARNING_ORDER_PRICE_EXCEEDED_MSG;
                            Alert alert = new Alert(AlertType.WARNING, msg);
                            alert.showAndWait();
                        }
                        event.consume();
                    });
                }
                nodeList.add(n);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        packSetTable.getChildren().clear();
        packSetTable.getChildren().addAll(nodeList);
    }

    private void displayOrder() {
        DecimalFormat df = new DecimalFormat("0.00");
        ObservableList<Item> linesOrder = FXCollections.observableArrayList();

        linesOrder.setAll(app.getKiosk().getItemsOrder());
        listOrder.setItems(linesOrder);
        if (app.getKiosk().getOrderDiscount() > 0.0) {
            labelTotal.setText(df.format(app.getKiosk().getOrderTotalGrossCostWithDiscount()) + " " + CURRENCY_CODE);
            labelDiscountLabel.setVisible(true);
            labelDiscount.setVisible(true);
            labelDiscount.setText(df.format(app.getKiosk().getOrderDiscount()) + " %");
        } else {
            labelTotal.setText(df.format(app.getKiosk().getOrderTotalGrossCost()) + " " + CURRENCY_CODE);
            labelDiscountLabel.setVisible(false);
            labelDiscount.setVisible(false);
        }
    }

    @FXML
    private void handleAcceptButton() {
        splitPane.setDividerPositions(SPLIT_POSITION);
        Alert alert;

        if (app.getKiosk().getItemsOrder().size() == 0) {
            alert = new Alert(AlertType.ERROR, "Your order is empty!!", ButtonType.OK);
            alert.showAndWait();
        } else {
            alert = new Alert(AlertType.INFORMATION, "Is this order correct?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                try {
                    app.getKiosk().commitOrder();
                    app.goToScene("ThanksView.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @FXML
    private void handleCancelButton() {
        Alert alert = new Alert(AlertType.WARNING, "Do you want to cancel the current order?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES)
            app.goToScene("StartView.fxml");
    }

    @FXML
    private void handleRandomButton() throws OrderException, OrderPriceExceededException {
        splitPane.setDividerPositions(SPLIT_POSITION);
        Item item = null;
        List<Item> inventory = new LinkedList<Item>();
        Random rnd = new Random();
        int soldOutCount = 0;

        inventory = app.getKiosk().getInventoryPerCategory("ALL_BUT_PACK_SET");

        for (Item currentItem : inventory) {
            if (currentItem.isSoldOut()) {
                soldOutCount++;
            }
        }
        if (soldOutCount >= inventory.size()) {
            Alert alert = new Alert(AlertType.WARNING, "Sorry, All items are sold out!!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Get a random item of the inventory that it is not sold out and to not exceed order price limit
        int tries = 0;
        boolean cannotAddRandomItem = false;
        while (item == null) {
            tries += 1;
            if (tries > MAX_RANDOM_ITEM_TRIES) {
                cannotAddRandomItem = true;
                String msg = "Cannot add random item. Max tries exceeded:" + MAX_RANDOM_ITEM_TRIES +  "!";
                log.warning(msg);
                break;
            }
            item = inventory.get(rnd.nextInt(inventory.size()));
            if (item.isSoldOut()) {
                cannotAddRandomItem = true;
                item = null;
                continue;
            }
            try {
                app.getKiosk().addItem2Order(item);
            } catch (OrderPriceExceededException e) {
                item = null;
            }
        }

        displayOrder();

        if (cannotAddRandomItem) {
            Alert alert = new Alert(AlertType.WARNING, "Cannot add random item this time. Try again");
            alert.showAndWait();
        }

        // If the item it is sold out after the addition, we need to refresh the current view of items
        if (item != null && item.isSoldOut())
            goToScreen();
    }

    @FXML
    private void handleBackButton() {
        splitPane.setDividerPositions(SPLIT_POSITION);
        if (app.getCurrentView() != View.PACK_SET) {
            app.setCurrentView(View.PACK_SET);
            goToScreen();
        } else {
            app.setCurrentView(View.PROMOTION_DISCOUNT);
            app.goToScene("PromotionDiscountView.fxml");
        }
    }

    @FXML
    private void handleItemSelectedFromListView() throws OrderException {
        splitPane.setDividerPositions(SPLIT_POSITION);
        if (listOrder.getSelectionModel().getSelectedItem() != null) {
            String msg = "";
            // If it is a pack set also display its items
            if (listOrder.getSelectionModel().getSelectedItem() instanceof PackSet) {
                msg += ((PackSet) listOrder.getSelectionModel().getSelectedItem()).getItemsWithHeaderString() + "\n\n";
            }
            msg += "Do you want to remove " + listOrder.getSelectionModel().getSelectedItem().getName() + "?";
            Alert alert = new Alert(AlertType.WARNING, msg, ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                app.getKiosk().removeItemFromOrder(listOrder.getSelectionModel().getSelectedIndex());
                displayOrder();
                goToScreen();
            }
        }
        listOrder.getSelectionModel().clearSelection(listOrder.getSelectionModel().getSelectedIndex());
    }
}
