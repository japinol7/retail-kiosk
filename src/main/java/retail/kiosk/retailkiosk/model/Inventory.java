package retail.kiosk.retailkiosk.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static retail.kiosk.retailkiosk.config.Config.INVENTORY_FILE_NAME;
import static retail.kiosk.retailkiosk.config.Config.log;


public class Inventory {
    // Represent all the distinct item's ids in the inventory. It is used as a cache
    private final Set<Integer> itemIds;
    ArrayList<Item> inventory;

    public Inventory() throws Exception {
        this(INVENTORY_FILE_NAME);
    }

    public Inventory(String fileName) throws Exception {
        inventory = new ArrayList<Item>();
        itemIds = new HashSet<Integer>();
        loadInventory(fileName);
    }

    public Set<Integer> getItemIds() {
        return itemIds;
    }

    private void loadInventory(String fileName) throws Exception {
        List<String> list = new ArrayList<String>();
        Item item;

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            list = stream.collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String itemList : list) {
            String[] elements = itemList.split("---");
            if (itemList.toUpperCase().equals("EOF")) {
                log.info("EOF line found. Stop importing inventory.");
                break;
            }
            try {
                item = switch (elements[1].toUpperCase()) {
                    case "ACTION_FIGURE_MOVABLE" -> createActionFigureMovable(elements);
                    case "ACTION_FIGURE" -> createActionFigure(elements);
                    case "ACTION_FIGURE_ACCESSORY" -> createActionFigureAccessory(elements);
                    case "BEVERAGE" -> createBeverage(elements);
                    case "STICKER" -> createSticker(elements);
                    case "CLOTHES_HAT" -> createClothesHat(elements);
                    case "PACK_SET" -> createPackSet(elements);
                    default -> null;
                };
                // If the item violates any constraint it will not be added to the inventory
                assert item != null;
                checkItemConstraints(item);
                // Add the item to the inventory because no constraint has been violated
                inventory.add(item);
                // Add the new id to itemIds, that represents all the distinct ids in the inventory.
                itemIds.add(item.getId());
            } catch (Exception e) {
                System.err.println("Error loading inventory item: " + e.getMessage());
            }
        }
    }

    private void checkItemConstraints(Item item) throws Exception {
        // Check if the item's id is not 0
        if (item.getId() == 0) {
            throw new Exception("The item's id cannot be 0!!");
        }
        // Check if the item's id is unique in the inventory.
        if (itemIds.contains(item.getId())) {
            throw new Exception("There's already one item in the inventory with id " + item.getId() + ". The id must be unique!!");
        }
        // When the item is a PackSet, check if it has at least 2 elements.
        if (item instanceof PackSet && ((PackSet) item).getItems().size() < 2) {
            throw new ItemException("A pack set must have at least two items. PackSet id: " + item.getId() + " !!");
        }
    }

    private ActionFigure createActionFigure(String[] items) throws Exception {
        return new ActionFigure(Integer.parseInt(items[0]), items[2], items[3],
                items[5], Double.parseDouble(items[6]), Double.parseDouble(items[7]),
                items[8], Boolean.parseBoolean(items[9]), Integer.parseInt(items[10]));
    }

    private ActionFigureMovable createActionFigureMovable(String[] items) throws Exception {
        return new ActionFigureMovable(Integer.parseInt(items[0]), items[2], items[3],
                items[5], Double.parseDouble(items[6]), Double.parseDouble(items[7]),
                items[8], Boolean.parseBoolean(items[9]), Integer.parseInt(items[10]));
    }

    private ActionFigureAccessory createActionFigureAccessory(String[] items) throws Exception {
        return new ActionFigureAccessory(Integer.parseInt(items[0]), items[2], items[3],
                items[5], Boolean.parseBoolean(items[4]), Double.parseDouble(items[6]),
                Double.parseDouble(items[7]), items[8], Boolean.parseBoolean(items[9]), Integer.parseInt(items[10]));
    }

    private Beverage createBeverage(String[] items) throws Exception {
        return new Beverage(Integer.parseInt(items[0]), items[2], items[3], Integer.parseInt(items[4]),
                Double.parseDouble(items[5]), Boolean.parseBoolean(items[6]), items[7],
                Double.parseDouble(items[8]), Double.parseDouble(items[9]), items[10],
                Boolean.parseBoolean(items[11]), Integer.parseInt(items[12]));
    }

    private Sticker createSticker(String[] items) throws Exception {
        return new Sticker(Integer.parseInt(items[0]), items[2], items[3], Integer.parseInt(items[4]),
                items[5], Double.parseDouble(items[6]), Double.parseDouble(items[7]),
                items[8], Boolean.parseBoolean(items[9]), Integer.parseInt(items[10]));
    }

    private ClothesHat createClothesHat(String[] items) throws Exception {
        return new ClothesHat(Integer.parseInt(items[0]), items[2], items[3],
                Boolean.parseBoolean(items[4]), items[5], Double.parseDouble(items[6]),
                Double.parseDouble(items[7]), items[8], Boolean.parseBoolean(items[9]), Integer.parseInt(items[10]));
    }

    private PackSet createPackSet(String[] items) throws Exception {
        List<Item> packSetItems = new ArrayList<Item>();

        // Convert the string containing the items' ids to add to the pack set to a list of items
        List<String> packSetItemsIds = new ArrayList<String>(Arrays.asList(items[7].split("\\s*,\\s*")));
        for (String itemId : packSetItemsIds) {
            for (Item item : inventory) {
                if (item.getId() == Integer.parseInt(itemId)) {
                    packSetItems.add(item);
                    break;
                }
            }
        }

        // Check if all the items on the pack set have been found. They must appear in the inventory before the pack set
        if (packSetItemsIds.size() != packSetItems.size()) {
            throw new ItemException("Some items on the pack set do not exist. Remember to put them " +
                    "before the pack set. PackSet id: " + items[0] + "!!");
        }

        return new PackSet(Integer.parseInt(items[0]), items[2], items[3],
                items[4], Double.parseDouble(items[5]), Double.parseDouble(items[6]), packSetItems);
    }

    public ArrayList<Item> getList() {
        return inventory;
    }

    public void setList(ArrayList<Item> inventory) {
        this.inventory = inventory;

        // Since the list of items of the inventory has changed, the cache for the distinct ids also has to be remade
        itemIds.clear();
        for (Item item : this.inventory) {
            itemIds.add(item.getId());
        }
    }

}
